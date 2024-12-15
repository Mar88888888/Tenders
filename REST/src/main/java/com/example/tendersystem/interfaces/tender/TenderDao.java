package com.example.tendersystem.interfaces.tender;

import com.example.tendersystem.interfaces.GenericDao;
import com.example.tendersystem.tender.Tender;

import java.time.LocalDate;
import java.util.List;

public interface TenderDao extends GenericDao<Tender, Long> {
  List<Tender> findByOwner(Long ownerId);

  List<Tender> findActiveTenders();

  List<Tender> findByKeywords(List<String> keyword);

  List<Tender> findFilteredTenders(LocalDate date, Double minSum, Double maxSum, int offset, int limit);
}
