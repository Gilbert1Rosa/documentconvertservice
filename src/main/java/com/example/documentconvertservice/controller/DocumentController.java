package com.example.documentconvertservice.controller;

import com.example.documentconvertservice.dto.DocumentDTO;
import com.example.documentconvertservice.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.DocumentUtil;

import java.io.IOException;

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
                @RequestParam(value = "file") MultipartFile file,
                @RequestParam(value = "start_page", required = false, defaultValue = "0") Integer startPage,
                @RequestParam(value = "end_page", required = false, defaultValue = "0") Integer endPage,
                @RequestParam(value = "new_group", required = false, defaultValue = "false") Boolean isNewGroup
            ) throws IOException {
        DocumentDTO document = DocumentUtil.multipartToDocument(file, startPage, endPage);
        exportService.saveFile(document, isNewGroup);

        return ResponseEntity.ok(document);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ioe) {
        return ResponseEntity.badRequest().build();
    }
}
