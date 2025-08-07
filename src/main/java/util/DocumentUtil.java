package util;

import com.example.documentconvertservice.dto.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class DocumentUtil {

    public static Document multipartToDocument(MultipartFile file) throws IOException {

        if (file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("No file name");
        }

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
        } else if (name.toLowerCase().endsWith(".tiff")) {
            type = Document.DocumentType.TIFF;
        } else {
            type = Document.DocumentType.UNKNOWN;
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
        return document;
    }

    private static byte[] convertDocxToPdfData(byte[] docxFileData) {
        return new byte[3];
    }

    private static byte[] convertDocToPdfData(byte[] docFileData) {
        return new byte[3];
    }
}
