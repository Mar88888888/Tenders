package com.example.tendersystem.tender;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TenderRepository extends JpaRepository<Tender, Long> {
  @Query("SELECT t FROM Tender t WHERE " +
      "(:date IS NULL OR t.createdDate = :date) AND " +
      "(:minSum IS NULL OR t.expectedPrice >= :minSum) AND " +
      "(:maxSum IS NULL OR t.expectedPrice <= :maxSum)")
  Page<Tender> findByDateAndSumRange(@Param("date") LocalDate date,
      @Param("minSum") Double minSum,
      @Param("maxSum") Double maxSum,
      Pageable pageable);
}
