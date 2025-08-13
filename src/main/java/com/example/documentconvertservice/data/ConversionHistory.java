package com.example.documentconvertservice.data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class ConversionHistory {

    private UUID conversionId;

    private Set<Document> documents;

    private Integer totalPages;

    private LocalDate conversionDate;

    public LocalDate getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(LocalDate conversionDate) {
        this.conversionDate = conversionDate;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public UUID getConversionId() {
        return conversionId;
    }

    public void setConversionId(UUID conversionId) {
        this.conversionId = conversionId;
    }
}
