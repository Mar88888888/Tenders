package com.example.tendersystem.proposal;

import com.example.tendersystem.tender.TenderService;
import com.example.tendersystem.tender.dto.ProposalResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProposalController implements ProposalControllerApi {

  private final TenderService tenderService;
  private final ProposalService tenderProposalService;

  public ProposalController(TenderService tenderService, ProposalService tenderProposalService) {
    this.tenderService = tenderService;
    this.tenderProposalService = tenderProposalService;
  }

  @Override
  public ResponseEntity<List<ProposalResponseDto>> getMyProposals(Long userId) {
    List<Proposal> proposals = tenderProposalService.getMyProposals(userId);
    List<ProposalResponseDto> tenderDtos = tenderProposalService.convertToDtoList(proposals);
    return ResponseEntity.ok(tenderDtos);
  }

  @Override
  public ResponseEntity<ProposalResponseDto> acceptProposal(Long proposalId) {
    Optional<Proposal> optionalProposal = tenderProposalService.getTenderProposalById(proposalId);

    if (optionalProposal.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    Proposal proposal = optionalProposal.get();

    try {
      boolean proposalAccepted = tenderService.acceptProposal(proposal);
      ProposalResponseDto proposalDto = tenderProposalService.convertToDto(proposal);

      if (proposalAccepted) {
        return ResponseEntity.ok(proposalDto);
      }

      return ResponseEntity.status(HttpStatus.CONFLICT).build();

    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
