package com.example.tendersystem.service;

import com.example.tendersystem.model.Tender;
import com.example.tendersystem.repository.TenderRepository;
import org.springframework.stereotype.Service;
import com.example.tendersystem.utils.UserUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TenderService {

  private final TenderRepository tenderRepository;
  private final UserUtils userUtils;

  public TenderService(
      TenderRepository tenderRepository,
      UserUtils userUtils) {
    this.tenderRepository = tenderRepository;
    this.userUtils = userUtils;
  }

  public List<Tender> getAllTenders() {
    return tenderRepository.findAll();
  }

  public List<Tender> getActiveTenders() {
    return tenderRepository.findAll().stream().filter(tender -> tender.isActive()).toList();
  }

  public List<Tender> getMyTenders() {
    return tenderRepository.findAll().stream()
        .filter(tender -> tender.getOwner().getId() == userUtils.getCurrentUser().getId()).toList();
  }

  public Optional<Tender> getTenderById(Long id) {
    return tenderRepository.findById(id);
  }

  public Tender createTender(Tender tender) {
    Long newId = tenderRepository.findAll().stream()
        .mapToLong(Tender::getId)
        .max()
        .orElse(0L) + 1;
    tender.setOwner(userUtils.getCurrentUser());
    tender.setCreatedDate(new Date());
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

  public List<Tender> searchTenders(String keyword) {
    String lowerKeyword = keyword.toLowerCase();

    return tenderRepository.searchByKeyword(lowerKeyword);
  }
}
