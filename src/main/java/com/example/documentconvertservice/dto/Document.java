package com.example.documentconvertservice.dto;

import java.beans.Transient;

public class Document {

    public enum DocumentType {
        PDF,
        DOC,
        DOCX,
        UNKNOWN,
        TIFF
    }

    private String documentId;
    private DocumentType type;
    private String name;
    private byte[] content;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
