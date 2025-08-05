package com.example.documentconvertservice.controller;

import com.example.documentconvertservice.dto.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @GetMapping("/download")
    public ResponseEntity<?> downloadDocument() {
        return ResponseEntity.ok("");
    }

    @PatchMapping("/upload")
    public ResponseEntity<?> uploadDocument(
                @RequestParam("file") MultipartFile file
            ) {
        Document document = new Document();
        return ResponseEntity.ok(document);
    }
}
