package com.example.tendersystem.service;

import com.example.tendersystem.model.Tender;
import com.example.tendersystem.repository.TenderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenderService {

  private final TenderRepository tenderRepository;

  public TenderService(TenderRepository tenderRepository) {
    this.tenderRepository = tenderRepository;
  }

  public List<Tender> getAllTenders() {
    return tenderRepository.findAll();
  }

  public Optional<Tender> getTenderById(Long id) {
    return tenderRepository.findById(id);
  }

  public Tender createTender(Tender tender) {
    Long newId = tenderRepository.findAll().stream()
        .mapToLong(Tender::getId)
        .max()
        .orElse(0L) + 1;

    tender.setId(newId);
    return tenderRepository.save(tender);
  }

  public void toggleActive(Long id) {
    Tender tender = tenderRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Tender not found"));
    tender.setActive(!tender.isActive());
    tenderRepository.save(tender);
  }

  public void deleteTender(Long id) {
    tenderRepository.deleteById(id);
  }
}
