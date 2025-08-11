package com.example.documentconvertservice.dto;

import java.util.List;

public class DocumentDetails {
    private List<Document> documents;
    private Double totalSize;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Double getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Double totalSize) {
        this.totalSize = totalSize;
    }
}
