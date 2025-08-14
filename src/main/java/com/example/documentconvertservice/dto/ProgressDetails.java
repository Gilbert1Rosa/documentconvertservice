package com.example.documentconvertservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressDetails {
    private String documentId;

    private int progress;
}
