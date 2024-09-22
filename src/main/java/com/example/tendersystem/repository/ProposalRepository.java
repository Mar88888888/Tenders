package com.example.tendersystem.repository;

import com.example.tendersystem.model.TenderProposal;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProposalRepository {
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

  public TenderProposal save(TenderProposal tender) {
    proposals.add(tender);
    return tender;
  }

  public void deleteById(Long id) {
    proposals.removeIf(tender -> tender.getId().equals(id));
  }
}
