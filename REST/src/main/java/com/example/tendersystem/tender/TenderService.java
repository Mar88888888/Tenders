package com.example.tendersystem.tender;

import com.example.tendersystem.interfaces.proposal.ProposalDao;
import com.example.tendersystem.interfaces.tender.TenderDao;
import com.example.tendersystem.proposal.Proposal;
import com.example.tendersystem.proposal.ProposalStatus;
import com.example.tendersystem.tender.dto.CreateTenderDto;
import com.example.tendersystem.tender.dto.TenderResponseDto;
import com.example.tendersystem.user.UserService;
import com.example.tendersystem.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenderService {

  @Autowired
  private TenderDao tenderDao;
  @Autowired
  private ProposalDao proposalDao;
  @Autowired
  private UserService userService;

  public List<Tender> getAllTenders() {
    return tenderDao.findActiveTenders();
  }

  public List<Tender> getActiveTenders() {
    return tenderDao.findActiveTenders();
  }

  public List<Tender> getFilteredTenders(LocalDate date, Double minSum, Double maxSum, int page, int size) {
    int offset = Math.max(page - 1, 0) * size;
    return tenderDao.findFilteredTenders(date, minSum, maxSum, offset, size);
  }

  public List<Tender> getUserTenders(Long id) {
    return tenderDao.findByOwner(id);
  }

  public Optional<Tender> getTenderById(Long id) {
    return tenderDao.read(id);
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
    tender.setOwnerId(owner.getId());
    tender.setActive(false);
    tender.setCreatedDate(new Date());

    Long tenderId = tenderDao.create(tender);
    tender.setId(tenderId);
    return tender;
  }

  public boolean updateTenderStatus(Long tenderId, boolean newStatus) {
    Optional<Tender> optionalTender = tenderDao.read(tenderId);

    Tender tender = optionalTender
        .orElseThrow(() -> new IllegalArgumentException("Tender with ID " + tenderId + " not found"));

    if (tender.isActive() != newStatus) {
      tender.setActive(newStatus);
      tenderDao.update(tender);
      return true;
    }

    return false;
  }

  public void deleteTender(Long id) {
    tenderDao.delete(id);
  }

  public List<Tender> searchTenders(String lowerKeyword) {
    List<String> keywords = Arrays.asList(lowerKeyword.split("\\s+"));
    return tenderDao.findByKeywords(keywords);
  }

  @Transactional()
  public boolean acceptProposal(Proposal proposal) throws Exception {
    Optional<Tender> optionalTender = tenderDao.read(proposal.getTenderId());

    Tender tender = optionalTender
        .orElseThrow(() -> new IllegalArgumentException("Tender with ID " + proposal.getTenderId() + " not found"));

    tender.setActive(false);

    if (tender.getAcceptedProposalId() != null) {
      throw new IllegalStateException("Tender with ID " + proposal.getTenderId() + " already has an accepted proposal");
    }

    tender.setAcceptedProposalId(proposal.getId());
    tenderDao.update(tender);

    proposal.setStatus(ProposalStatus.ACCEPTED);
    proposalDao.update(proposal);

    // if (true) {
    // throw new RuntimeException("Simulated exception for rollback");
    // }

    List<Proposal> proposals = proposalDao.findByTender(proposal.getTenderId());

    for (Proposal p : proposals) {
      if (!p.getId().equals(proposal.getId())) {
        p.setStatus(ProposalStatus.REJECTED);
        proposalDao.update(p);
      }
    }

    return true;
  }

  public TenderResponseDto convertToDto(Tender tender) {
    TenderResponseDto dto = new TenderResponseDto()
        .setId(tender.getId())
        .setTitle(tender.getTitle())
        .setDescription(tender.getDescription())
        .setExpectedPrice(tender.getExpectedPrice())
        .setActive(tender.isActive())
        .setCreatedDate(tender.getCreatedDate())
        .setOwnerId(tender.getOwnerId())
        .setAcceptedProposalId(tender.getAcceptedProposalId() == null ? null : tender.getAcceptedProposalId())
        .setKeywords(tender.getKeywords());
    return dto;
  }

  public List<TenderResponseDto> convertToDtoList(List<Tender> tenders) {
    return tenders.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }
}
