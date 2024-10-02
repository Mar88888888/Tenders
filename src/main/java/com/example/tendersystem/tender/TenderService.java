package com.example.tendersystem.tender;

import com.example.tendersystem.proposal.TenderProposal;
import com.example.tendersystem.user.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class TenderService {

  @Autowired
  private TenderRepository tenderRepository;
  @Autowired
  private UserUtils userUtils;

  public List<Tender> getAllTenders() {
    return StreamSupport.stream(tenderRepository.findAll().spliterator(), false)
        .toList();
  }

  public List<Tender> getActiveTenders() {
    return getAllTenders().stream()
        .filter(Tender::isActive).toList();
  }

  public List<Tender> getMyTenders() {
    return getAllTenders().stream()
        .filter(tender -> tender.getOwner().getId().equals(userUtils.getCurrentUser().getId())).toList();
  }

  public Optional<Tender> getTenderById(Long id) {
    return tenderRepository.findById(id);
  }

  public Tender createTender(Tender tender) {
    tender.setOwner(userUtils.getCurrentUser());
    tender.setCreatedDate(new Date());
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

  public List<Tender> searchTenders(String lowerKeyword) {
    return getAllTenders().stream()
        .filter(tender -> tender.getTitle().toLowerCase().contains(lowerKeyword) ||
            tender.getDescription().toLowerCase().contains(lowerKeyword) ||
            tender.getKeywords().stream()
                .anyMatch(k -> k.toLowerCase().contains(lowerKeyword)))
        .toList();
  }

  public String acceptProposal(TenderProposal proposal, String username) {
    Tender tender = proposal.getTender();

    if (!tender.getOwner().getUsername().equals(username)) {
      return "redirect:/tenders/" + tender.getId() + "?error=UnauthorizedAccess";
    }
    if (tender.getAcceptedProposal() != null) {
      return "redirect:/tenders/" + tender.getId() + "?error=ProposalAlreadyAccepted";
    }
    tender.setAcceptedProposal(proposal);
    tenderRepository.save(tender);

    return "redirect:/tenders/" + tender.getId() + "?success=ProposalAccepted";
  }
}
