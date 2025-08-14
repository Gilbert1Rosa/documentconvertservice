package com.example.documentconvertservice.data;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID conversionId;

    @OneToOne
    private User user;

    private Integer totalPages;

    private LocalDateTime conversionDate;
}
