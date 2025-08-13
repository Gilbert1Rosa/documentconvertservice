package com.example.documentconvertservice.service;

import com.example.documentconvertservice.dto.Document;
import com.example.documentconvertservice.dto.DocumentDetails;
import com.example.documentconvertservice.websocket.ProgressWebSocketHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.print.Doc;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private ProgressWebSocketHandler webSocketHandler;

    private static final List<Document> documents = new ArrayList<>();

    public InputStream getExport(int resolution) throws IOException {

        if (documents.isEmpty()) {
            throw new IllegalArgumentException("No documents uploaded");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("TIFF").next();

        try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {
            writer.setOutput(imageOutputStream);

            ImageWriteParam params = writer.getDefaultWriteParam();
            params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            params.setCompressionType("Deflate");
            writer.prepareWriteSequence(null);

            int tiffIndex = 0;
            int totalPages = 0;

            for (Document document : documents) {
                switch (document.getType()) {
                    case PDF -> {
                        try (PDDocument pdDocument = PDDocument.load(document.getContent())) {
                            totalPages += pdDocument.getNumberOfPages();
                        }
                    }
                    case TIFF -> {
                        ImageReader reader = ImageIO.getImageReadersByFormatName("TIFF").next();
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(document.getContent());
                        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream)) {
                            reader.setInput(imageInputStream);
                            totalPages += reader.getNumImages(true);
                        }
                    }
                }
            }

            for (Document document : documents) {
                webSocketHandler.sendProgress((double) tiffIndex / totalPages * 100);
                if (document.getType() == Document.DocumentType.TIFF) {
                    tiffIndex = readTIFF(document, writer, params, tiffIndex);
                } else if (document.getType() == Document.DocumentType.PDF) {
                    tiffIndex = readPDF(document, writer, params, tiffIndex, resolution);
                }
            }

            writer.endWriteSequence();
        }

        webSocketHandler.sendProgress(100);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public void saveFile(Document document, boolean isNewGroup) {
        if (isNewGroup) {
            documents.clear();
        }

        documents.add(document);
    }

    public DocumentDetails getDocuments() {
        DocumentDetails details = new DocumentDetails();
        double totalSize = 0.0;

        for (Document document : documents) {
            totalSize += document.getContent().length;
        }

        details.setDocuments(documents);
        details.setTotalSize(totalSize);
        return details;
    }

    private int readTIFF(Document document, ImageWriter writer, ImageWriteParam params, int tiffIndex) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByFormatName("TIFF").next();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(document.getContent());
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream)) {
            reader.setInput(imageInputStream);

            int numImages = reader.getNumImages(true);

            for (int imageIndex = 0; imageIndex < numImages; imageIndex++) {
                BufferedImage image = reader.read(imageIndex);
                writer.writeInsert(tiffIndex, new IIOImage(image, null, null), params);
                tiffIndex++;
            }
        }

        return tiffIndex;
    }

    private int readPDF(Document document, ImageWriter writer, ImageWriteParam params, int tiffIndex, int resolution) throws IOException {
        try (PDDocument pdDocument = PDDocument.load(document.getContent())) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            int pageCount = pdDocument.getNumberOfPages();

            int start = document.getStartPage();
            int stop = Math.min(document.getEndPage(), pageCount - 1);

            if (start < 0 || stop < 0) {
                throw new IllegalArgumentException("Start and stop page cannot be negative");
            }

            for (int pageIndex = start; pageIndex <= stop; pageIndex++) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(
                        pageIndex,
                        resolution,
                        ImageType.RGB
                );
                writer.writeInsert(tiffIndex, new IIOImage(bufferedImage, null, null), params);
                tiffIndex++;
            }
        }
        return tiffIndex;
    }
}
