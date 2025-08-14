package com.example.documentconvertservice.service;

import com.example.documentconvertservice.data.ConversionHistory;
import com.example.documentconvertservice.repository.ConversionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ConversionHistoryRepository conversionHistoryRepository;

    public Page<ConversionHistory> getConversionHistory(Pageable pageable) {
        return conversionHistoryRepository.findAll(pageable);
    }
}
