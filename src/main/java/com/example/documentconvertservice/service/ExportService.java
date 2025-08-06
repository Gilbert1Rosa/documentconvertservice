package com.example.documentconvertservice.service;

import com.example.documentconvertservice.dto.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportService {

    private static final List<Document> documents = new ArrayList<>();

    public InputStream getExport(int resolution) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("TIFF").next();

        for (Document document : documents) {
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            try (PDDocument pdDocument = PDDocument.load(document.getContent())) {
                PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);

                int pageCount = pdDocument.getNumberOfPages();

                writer.setOutput(imageOutputStream);

                ImageWriteParam params = writer.getDefaultWriteParam();
                params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

                params.setCompressionType("Deflate");
                writer.prepareWriteSequence(null);

                for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                    BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(
                            pageIndex,
                            resolution,
                            ImageType.RGB
                    );
                    writer.writeInsert(pageIndex, new IIOImage(bufferedImage, null, null), params);
                }

                writer.endWriteSequence();
            }
            imageOutputStream.close();
        }

        byte[] outputBytes = outputStream.toByteArray();
        return new ByteArrayInputStream(outputBytes);
    }

    public void saveFile(Document document) {
        documents.add(document);
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
