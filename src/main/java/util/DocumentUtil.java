package util;

import com.example.documentconvertservice.dto.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class DocumentUtil {



    public static Document multipartToDocument(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        Document document = new Document();
        document.setDocumentId(UUID.randomUUID().toString());
        document.setName(name);

        Document.DocumentType type;
        if (name.toLowerCase().endsWith(".pdf")) {
            type = Document.DocumentType.PDF;
        } else if (name.toLowerCase().endsWith(".doc")) {
            type = Document.DocumentType.DOC;
        } else if (name.toLowerCase().endsWith(".docx")) {
            type = Document.DocumentType.DOCX;
        } else {
            type = Document.DocumentType.UNKNOWN;
        }

        switch(type) {
            case DOC -> {
                document.setContent(convertDocToPdfData(file.getBytes()));
            }
            case DOCX -> {
                document.setContent(convertDocXToPdfData(file.getBytes()));
            }
            case PDF -> {
                document.setContent(file.getBytes());
            }
            case UNKNOWN -> {
                throw new IllegalArgumentException("File type not accepted");
            }
        }

        document.setType(type);
        return document;
    }

    private static byte[] convertDocXToPdfData(byte[] docFileData) throws Exception {
        InputStream docxInputStream = new ByteArrayInputStream(docFileData);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (XWPFDocument document = new XWPFDocument(docxInputStream);
             OutputStream pdfOutputStream = byteArrayOutputStream) {
            com.itextpdf.text.Document pdfDocument = new com.itextpdf.text.Document();
            PdfWriter.getInstance(pdfDocument, pdfOutputStream);
            pdfDocument.open();

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                pdfDocument.add(new Paragraph(paragraph.getText()));
            }
            pdfDocument.close();
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] convertDocToPdfData(byte[] docFileData) {
        return new byte[3];
    }
}
