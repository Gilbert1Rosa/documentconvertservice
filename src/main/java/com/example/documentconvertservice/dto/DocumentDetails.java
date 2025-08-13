package com.example.documentconvertservice.dto;

import java.util.List;

public class DocumentDetails {
    private List<DocumentDTO> documents;
    private Double totalSize;

    public List<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDTO> documents) {
        this.documents = documents;
    }

    public Double getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Double totalSize) {
        this.totalSize = totalSize;
    }
}
