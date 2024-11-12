package com.example.tendersystem.proposal;

import com.example.tendersystem.tender.Tender;
import com.example.tendersystem.user.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class TenderProposalService {

  @Autowired
  private TenderProposalRepository tenderProposalRepository;

  @Autowired
  private UserUtils userUtils;

  public TenderProposalService() {
  }

  public List<TenderProposal> getAllTenderProposals() {
    return StreamSupport.stream(tenderProposalRepository.findAll().spliterator(), false).toList();
  }

  public Optional<TenderProposal> getTenderProposalById(Long id) {
    return tenderProposalRepository.findById(id);
  }

  public List<TenderProposal> getMyProposals() {
    return getAllTenderProposals().stream()
        .filter(tender -> tender.getProposer().getId().equals(userUtils.getCurrentUser().getId())).toList();
  }

  public TenderProposal createTenderProposal(TenderProposal proposal, Tender tender) {
    proposal.setProposer(userUtils.getCurrentUser());
    proposal.setSubmittedDate(new Date());
    proposal.setTender(tender);
    return tenderProposalRepository.save(proposal);
  }

  public void deleteTenderProposalById(Long id) {
    tenderProposalRepository.deleteById(id);
  }

  public List<TenderProposal> getProposalsByTenderId(Long tenderId) {
    return getAllTenderProposals().stream()
        .filter(tender -> tender.getTender().getId().equals(tenderId)).toList();
  }

}
