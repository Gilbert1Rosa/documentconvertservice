package com.example.documentconvertservice.repository;

import com.example.documentconvertservice.data.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversionHistoryRepository extends PagingAndSortingRepository<ConversionHistory, UUID>, JpaRepository<ConversionHistory, UUID> {
}
