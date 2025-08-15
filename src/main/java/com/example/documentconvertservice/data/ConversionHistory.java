package com.example.documentconvertservice.data;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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

    @ManyToOne
    @JsonIncludeProperties({"username"})
    private User user;

    private Integer pagesConverted;

    private LocalDateTime conversionDate;
}
