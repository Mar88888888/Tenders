package com.example.tendersystem.repository;

import com.example.tendersystem.model.TenderProposal;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TenderProposalRepository {
  private List<TenderProposal> proposals = new ArrayList<>();

  public List<TenderProposal> findAll() {
    return proposals;
  }

  public Optional<TenderProposal> findById(Long id) {
    return proposals.stream().filter(proposal -> proposal.getId().equals(id)).findFirst();
  }

  public List<TenderProposal> findByProposerId(Long id) {
    return proposals.stream().filter(proposal -> proposal.getProposer().getId().equals(id)).toList();
  }

  public List<TenderProposal> findByTenderId(Long tenderId) {
    return proposals.stream()
        .filter(tenderProposal -> tenderProposal.getTender().getId().equals(tenderId))
        .collect(Collectors.toList());
  }

  public TenderProposal save(TenderProposal tenderProposal) {
    findById(tenderProposal.getId()).ifPresentOrElse(existingProposal -> {
      existingProposal.setDescription(tenderProposal.getDescription());
      existingProposal.setPrice(tenderProposal.getPrice());
      existingProposal.setProposer(tenderProposal.getProposer());
      existingProposal.setTender(tenderProposal.getTender());
    }, () -> {
      proposals.add(tenderProposal);
    });

    return tenderProposal;
  }

  public void deleteById(Long id) {
    proposals.removeIf(tender -> tender.getId().equals(id));
  }

  public void deleteByTenderId(Long tenderId) {
    proposals.removeIf(proposal -> proposal.getTender().getId().equals(tenderId));
  }
}
