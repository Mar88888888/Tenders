package com.example.tendersystem.proposal;

import com.example.tendersystem.interfaces.proposal.ProposalDao;
import com.example.tendersystem.tender.TenderService;
import com.example.tendersystem.tender.dto.CreateProposalDto;
import com.example.tendersystem.tender.dto.ProposalResponseDto;
import com.example.tendersystem.user.User;
import com.example.tendersystem.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProposalService {

  @Autowired
  private ProposalDao proposalDao;
  @Autowired
  private UserService userService;
  @Autowired
  private TenderService tenderService;

  public ProposalService() {
  }

  public List<Proposal> getAllTenderProposals() {
    return proposalDao.findAll();
  }

  public Optional<Proposal> getTenderProposalById(Long id) {
    return proposalDao.read(id);
  }

  public List<Proposal> getMyProposals(Long ownerId) {
    return proposalDao.findByProposer(ownerId);
  }

  public Proposal createTenderProposal(CreateProposalDto proposalDto, Long tenderId) {
    tenderService.getTenderById(tenderId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tender not found with id " + tenderId));

    User proposer = userService.getUserById(proposalDto.getProposerId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "User not found with id " + proposalDto.getProposerId()));

    Proposal tenderProposal = new Proposal();
    tenderProposal.setDescription(proposalDto.getDescription());
    tenderProposal.setSubmittedDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

    tenderProposal.setPrice(BigDecimal.valueOf(proposalDto.getPrice()));
    tenderProposal.setTenderId(tenderId);
    tenderProposal.setProposerId(proposer.getId());

    Long proposalId = proposalDao.create(tenderProposal);
    tenderProposal.setId(proposalId);

    return tenderProposal;
  }

  public void deleteTenderProposalById(Long id) {
    proposalDao.delete(id);
  }

  public List<Proposal> getProposalsByTenderId(Long tenderId) {
    return proposalDao.findByTender(tenderId);
  }

  public ProposalResponseDto convertToDto(Proposal proposal) {
    ProposalResponseDto dto = new ProposalResponseDto();
    dto.setId(proposal.getId());
    dto.setDescription(proposal.getDescription());
    dto.setPrice(proposal.getPrice().doubleValue());
    dto.setProposerId(proposal.getProposerId());
    dto.setTenderId(proposal.getTenderId());
    dto.setStatus(proposal.getStatus());
    return dto;
  }

  public List<ProposalResponseDto> convertToDtoList(List<Proposal> tenders) {
    return tenders.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

}
