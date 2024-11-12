package com.example.tendersystem.tender;

import com.example.tendersystem.proposal.TenderProposal;
import com.example.tendersystem.tender.dto.CreateTenderDto;
import com.example.tendersystem.tender.dto.TenderResponseDto;
import com.example.tendersystem.user.UserService;
import com.example.tendersystem.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TenderService {

  @Autowired
  private TenderRepository tenderRepository;
  @Autowired
  private UserService userService;

  public List<Tender> getAllTenders() {
    return StreamSupport.stream(tenderRepository.findAll().spliterator(), false)
        .toList();
  }

  public List<Tender> getFilteredTenders(LocalDate date, Double minSum, Double maxSum, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<Tender> tenderPage;
    if (date != null || minSum != null || maxSum != null) {
      tenderPage = tenderRepository.findByDateAndSumRange(date, minSum, maxSum, pageable);
    } else {
      tenderPage = tenderRepository.findAll(pageable);
    }
    return tenderPage.getContent();
  }

  public List<Tender> getActiveTenders() {
    return getAllTenders().stream()
        .filter(Tender::isActive).toList();
  }

  public List<Tender> getMyTenders(Long id) {
    return getAllTenders().stream()
        .filter(tender -> tender.getOwner().getId().equals(id)).toList();
  }

  public Optional<Tender> getTenderById(Long id) {
    return tenderRepository.findById(id);
  }

  public Tender createTender(CreateTenderDto createTenderDto) {
    Optional<User> optionalOwner = userService.getUserById(createTenderDto.getOwnerId());

    if (optionalOwner.isEmpty()) {
      throw new IllegalArgumentException("Owner with ID " + createTenderDto.getOwnerId() + " not found");
    }

    User owner = optionalOwner.get();

    Tender tender = new Tender();
    tender.setTitle(createTenderDto.getTitle());
    tender.setDescription(createTenderDto.getDescription());
    tender.setExpectedPrice(createTenderDto.getExpectedPrice());
    tender.setKeywords(createTenderDto.getKeywords());
    tender.setOwner(owner);
    tender.setActive(false);
    tender.setCreatedDate(new Date());
    return tenderRepository.save(tender);
  }

  public boolean updateTenderStatus(Long tenderId, boolean newStatus) {
    Optional<Tender> tenderOptional = tenderRepository.findById(tenderId);

    if (tenderOptional.isEmpty()) {
      throw new IllegalArgumentException("Tender with ID " + tenderId + " not found");
    }

    if (tenderOptional.isPresent()) {
      Tender tender = tenderOptional.get();
      if (tender.isActive() != newStatus) {
        tender.setActive(newStatus);
        tenderRepository.save(tender);
        return true;
      }
    }

    return false;
  }

  public void deleteTender(Long id) {
    tenderRepository.deleteById(id);
  }

  public List<Tender> searchTenders(String lowerKeyword) {
    return getAllTenders().stream()
        .filter(tender -> tender.getTitle().toLowerCase().contains(lowerKeyword) ||
            tender.getDescription().toLowerCase().contains(lowerKeyword) ||
            tender.getKeywords().stream()
                .anyMatch(k -> k.toLowerCase().contains(lowerKeyword)))
        .toList();
  }

  public boolean acceptProposal(TenderProposal proposal) {
    Tender tender = proposal.getTender();

    if (tender.getAcceptedProposal() != null) {
      return false;
    }

    tender.setAcceptedProposal(proposal);
    tenderRepository.save(tender);
    return true;
  }

  public TenderResponseDto convertToDto(Tender tender) {
    TenderResponseDto dto = new TenderResponseDto()
        .setId(tender.getId())
        .setTitle(tender.getTitle())
        .setDescription(tender.getDescription())
        .setExpectedPrice(tender.getExpectedPrice())
        .setActive(tender.isActive())
        .setOwnerId(tender.getOwner().getId())
        .setAcceptedProposalId(tender.getAcceptedProposal() == null ? null : tender.getAcceptedProposal().getId())
        .setKeywords(tender.getKeywords());
    return dto;
  }

  public List<TenderResponseDto> convertToDtoList(List<Tender> tenders) {
    return tenders.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }
}
