package com.example.tendersystem.service;

import com.example.tendersystem.model.Tender;
import com.example.tendersystem.model.TenderProposal;
import com.example.tendersystem.repository.TenderProposalRepository;
import com.example.tendersystem.utils.UserUtils;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TenderProposalService {

  private final TenderProposalRepository tenderProposalRepository;
  private final UserUtils userUtils;

  public TenderProposalService(TenderProposalRepository tenderProposalRepository, UserUtils userUtils) {
    this.tenderProposalRepository = tenderProposalRepository;
    this.userUtils = userUtils;
  }

  public List<TenderProposal> getAllTenderProposals() {
    return tenderProposalRepository.findAll();
  }

  public Optional<TenderProposal> getTenderProposalById(Long id) {
    return tenderProposalRepository.findById(id);
  }

  public List<TenderProposal> getMyProposals() {
    return tenderProposalRepository.findAll().stream()
        .filter(tender -> tender.getProposer().getId() == userUtils.getCurrentUser().getId()).toList();
  }

  public TenderProposal createTenderProposal(TenderProposal proposal, Tender tender) {
    Long newId = tenderProposalRepository.findAll().stream()
        .mapToLong(TenderProposal::getId)
        .max()
        .orElse(0L) + 1;
    proposal.setProposer(userUtils.getCurrentUser());
    proposal.setSubmittedDate(new Date());
    proposal.setTender(tender);
    proposal.setId(newId);

    return tenderProposalRepository.save(proposal);
  }

  public void deleteTenderProposalById(Long id) {
    tenderProposalRepository.deleteById(id);
  }

  public void deleteAllProposalsByTenderId(Long tenderId) {
    tenderProposalRepository.deleteByTenderId(tenderId);
  }

  public List<TenderProposal> getProposalsByTenderId(Long tenderId) {
    return tenderProposalRepository.findByTenderId(tenderId);
  }
}
