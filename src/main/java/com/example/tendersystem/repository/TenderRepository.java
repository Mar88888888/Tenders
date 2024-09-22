package com.example.tendersystem.repository;

import com.example.tendersystem.model.Tender;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TenderRepository {
  private List<Tender> tenders = new ArrayList<>();

  public List<Tender> findAll() {
    return tenders;
  }

  public Optional<Tender> findById(Long id) {
    return tenders.stream().filter(tender -> tender.getId().equals(id)).findFirst();
  }

  public Tender save(Tender tender) {
    findById(tender.getId()).ifPresent(existingTender -> {
      tenders.remove(existingTender);
    });
    tenders.add(tender);
    return tender;
  }

  public void deleteById(Long id) {
    tenders.removeIf(tender -> tender.getId().equals(id));
  }
}
