package com.example.documentconvertservice.controller;

import com.example.documentconvertservice.dto.Document;
import com.example.documentconvertservice.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private ExportService exportService;

    @GetMapping
    public ResponseEntity<?> getDocuments() {
        return ResponseEntity.ok(exportService.getDocuments());
    }

    @GetMapping("/export")
    public ResponseEntity<?> downloadDocument(
            @RequestParam("resolution") Integer resolution
    ) throws IOException {
        InputStreamResource resource = new InputStreamResource(exportService.getExport(resolution));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename("export.tiff")
                        .build()
        );
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
                @RequestParam("file") MultipartFile file
            ) throws IOException {
        Document document = new Document();
        document.setDocumentId(UUID.randomUUID().toString());
        document.setContent(file.getBytes());
        document.setName(file.getName());
        document.setType(Document.DocumentType.PDF);

        exportService.saveFile(document);

        return ResponseEntity.ok(document);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ioe) {
        return ResponseEntity.badRequest().build();
    }
}
