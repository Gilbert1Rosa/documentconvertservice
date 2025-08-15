package com.example.documentconvertservice.controller;

import com.example.documentconvertservice.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/conversion-history")
    public ResponseEntity<?> getConversionHistory(
            Pageable pageable
    ) {
        return ResponseEntity.ok(reportService.getConversionHistory(pageable));
    }
}
