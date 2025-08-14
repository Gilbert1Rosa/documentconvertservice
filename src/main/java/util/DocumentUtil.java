package util;

import com.example.documentconvertservice.dto.DocumentDTO;
import com.example.documentconvertservice.data.DocumentType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class DocumentUtil {

    public static DocumentDTO multipartToDocument(MultipartFile file, int startPage, int endPage) throws IOException {
        if (file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("No file name");
        }

        if (startPage < 0) {
            throw new IllegalArgumentException("Negative start page not allowed");
        }

        if (endPage < 0) {
            throw new IllegalArgumentException("Negative end page not allowed");
        }

        if (startPage > endPage) {
            throw new IllegalArgumentException("Start page cannot be higher than end page");
        }

        String name = file.getOriginalFilename();
        DocumentDTO document = new DocumentDTO();
        document.setDocumentId(UUID.randomUUID().toString());
        document.setName(name);

        DocumentType type;

        if (name.toLowerCase().endsWith(".pdf")) {
            type = DocumentType.PDF;
        } else if (name.toLowerCase().endsWith(".doc")) {
            type = DocumentType.DOC;
        } else if (name.toLowerCase().endsWith(".docx")) {
            type = DocumentType.DOCX;
        } else if (name.toLowerCase().endsWith(".tiff")) {
            type = DocumentType.TIFF;
        } else {
            type = DocumentType.UNKNOWN;
        }

        switch(type) {
            case DOC -> {
                document.setContent(convertDocToPdfData(file.getBytes()));
            }
            case DOCX -> {
                document.setContent(convertDocxToPdfData(file.getBytes()));
            }
            case PDF, TIFF -> {
                document.setContent(file.getBytes());
            }
            case UNKNOWN -> {
                throw new IllegalArgumentException("File type not accepted");
            }
        }

        document.setType(type);
        document.setStartPage(startPage);
        document.setEndPage(endPage);

        return document;
    }

    public static int getNumberOfPages(DocumentDTO document) throws IOException {
        int pages = 0;

        if (document.getType() == DocumentType.PDF) {
            try (PDDocument pdDocument = PDDocument.load(document.getContent())) {
                pages = pdDocument.getNumberOfPages();
            }
        }

        return pages;
    }

    private static byte[] convertDocxToPdfData(byte[] docxFileData) {
        return new byte[3];
    }

    private static byte[] convertDocToPdfData(byte[] docFileData) {
        return new byte[3];
    }
}
