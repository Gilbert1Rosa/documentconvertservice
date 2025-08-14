package com.example.documentconvertservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDetails {
    private List<DocumentDTO> documents;
    private Double totalSize;
}
