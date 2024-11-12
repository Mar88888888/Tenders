package com.example.tendersystem.proposal;

import com.example.tendersystem.tender.Tender;
import com.example.tendersystem.tender.TenderService;
import com.example.tendersystem.tender.dto.CreateProposalDto;
import com.example.tendersystem.tender.dto.ProposalResponseDto;
import com.example.tendersystem.user.User;
import com.example.tendersystem.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TenderProposalService {

  @Autowired
  private TenderProposalRepository tenderProposalRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private TenderService tenderService;

  public TenderProposalService() {
  }

  public List<TenderProposal> getAllTenderProposals() {
    return StreamSupport.stream(tenderProposalRepository.findAll().spliterator(), false).toList();
  }

  public Optional<TenderProposal> getTenderProposalById(Long id) {
    return tenderProposalRepository.findById(id);
  }

  public List<TenderProposal> getMyProposals(Long ownerId) {
    return getAllTenderProposals().stream()
        .filter(tender -> tender.getProposer().getId().equals(ownerId)).toList();
  }

  public TenderProposal createTenderProposal(CreateProposalDto proposalDto, Long tenderId) {
    Tender tender = tenderService.getTenderById(tenderId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tender not found with id " + tenderId));

    User proposer = userService.getUserById(proposalDto.getProposerId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "User not found with id " + proposalDto.getProposerId()));

    TenderProposal tenderProposal = new TenderProposal();
    tenderProposal.setDescription(proposalDto.getDescription());
    tenderProposal.setSubmittedDate(new Date());
    tenderProposal.setPrice(proposalDto.getPrice());
    tenderProposal.setTender(tender);
    tenderProposal.setProposer(proposer);

    return tenderProposalRepository.save(tenderProposal);
  }

  public void deleteTenderProposalById(Long id) {
    tenderProposalRepository.deleteById(id);
  }

  public List<TenderProposal> getProposalsByTenderId(Long tenderId) {
    return getAllTenderProposals().stream()
        .filter(tender -> tender.getTender().getId().equals(tenderId)).toList();
  }

  public ProposalResponseDto convertToDto(TenderProposal proposal) {
    ProposalResponseDto dto = new ProposalResponseDto();
    dto.setId(proposal.getId());
    dto.setDescription(proposal.getDescription());
    dto.setPrice(proposal.getPrice());
    dto.setProposerId(proposal.getProposer().getId());
    dto.setTenderId(proposal.getTender().getId());
    return dto;
  }

  public List<ProposalResponseDto> convertToDtoList(List<TenderProposal> tenders) {
    return tenders.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

}
