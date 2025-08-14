package com.example.documentconvertservice.dto;

import com.example.documentconvertservice.data.DocumentType;
import lombok.*;

import java.beans.Transient;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDTO {

    private String documentId;
    private DocumentType type;
    private String name;
    private byte[] content;
    private int startPage;
    private int endPage;


    @Transient
    public byte[] getContent() {
        return content;
    }
}
