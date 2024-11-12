package com.example.tendersystem.proposal;

import com.example.tendersystem.tender.TenderService;
import com.example.tendersystem.tender.dto.ProposalResponseDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/proposals")
public class TenderProposalController {

  @Value("${app.url}")
  private String appUrl;

  private final TenderService tenderService;
  private final TenderProposalService tenderProposalService;

  public TenderProposalController(TenderService tenderService, TenderProposalService tenderProposalService) {
    this.tenderService = tenderService;
    this.tenderProposalService = tenderProposalService;
  }

  @GetMapping("/my/{userId}")
  public ResponseEntity<List<ProposalResponseDto>> getMyProposals(@PathVariable Long userId) {
    List<TenderProposal> proposals = tenderProposalService.getMyProposals(userId);
    List<ProposalResponseDto> tenderDtos = tenderProposalService.convertToDtoList(proposals);
    return ResponseEntity.ok(tenderDtos);
  }

  @PutMapping("/{proposalId}/status")
  public ResponseEntity<String> acceptProposal(@PathVariable Long proposalId) {
    Optional<TenderProposal> optionalProposal = tenderProposalService.getTenderProposalById(proposalId);

    if (optionalProposal.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proposal not found");
    }

    TenderProposal proposal = optionalProposal.get();

    try {
      boolean proposalAccepted = tenderService.acceptProposal(proposal);

      if (proposalAccepted) {
        return ResponseEntity.ok("Proposal accepted successfully");
      }

      return ResponseEntity.ok("Tender already has accepted proposal");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

}
