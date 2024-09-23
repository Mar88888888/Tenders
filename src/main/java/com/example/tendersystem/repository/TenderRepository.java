package com.example.tendersystem.repository;

import com.example.tendersystem.model.Tender;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

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
    findById(tender.getId()).ifPresentOrElse(existingTender -> {
      existingTender.setTitle(tender.getTitle());
      existingTender.setDescription(tender.getDescription());
      existingTender.setActive(tender.isActive());
      existingTender.setOwner(tender.getOwner());
      existingTender.setKeywords(tender.getKeywords());
    }, () -> {
      tenders.add(tender);
    });

    return tender;
  }

  public void deleteById(Long id) {
    tenders.removeIf(tender -> tender.getId().equals(id));
  }

  public List<Tender> searchByKeyword(String lowerKeyword) {
    return tenders.stream()
        .filter(tender -> tender.getTitle().toLowerCase().contains(lowerKeyword) ||
            tender.getDescription().toLowerCase().contains(lowerKeyword) ||
            Arrays.stream(tender.getKeywords())
                .anyMatch(k -> k.toLowerCase().contains(lowerKeyword)))
        .collect(Collectors.toList());
  }
}
