package com.example.tendersystem.proposal;

import com.example.tendersystem.tender.TenderService;
import com.example.tendersystem.tender.dto.ProposalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Tender Proposals", description = "Operations related to managing tender proposals")
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

  @Operation(summary = "Get user's proposals", description = "Retrieve all proposals made by a specific user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of proposals", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @GetMapping("/my/{userId}")
  public ResponseEntity<List<ProposalResponseDto>> getMyProposals(
      @Parameter(description = "ID of the user to retrieve proposals for") @PathVariable Long userId) {
    List<TenderProposal> proposals = tenderProposalService.getMyProposals(userId);
    List<ProposalResponseDto> tenderDtos = tenderProposalService.convertToDtoList(proposals);
    return ResponseEntity.ok(tenderDtos);
  }

  @Operation(summary = "Accept a proposal", description = "Accept a specific proposal by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Proposal accepted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Proposal not found", content = @Content),
      @ApiResponse(responseCode = "409", description = "Proposal cannot be accepted due to conflict", content = @Content)
  })
  @PatchMapping("/{proposalId}/status")
  public ResponseEntity<ProposalResponseDto> acceptProposal(
      @Parameter(description = "ID of the proposal to accept") @PathVariable Long proposalId) {
    Optional<TenderProposal> optionalProposal = tenderProposalService.getTenderProposalById(proposalId);

    if (optionalProposal.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    TenderProposal proposal = optionalProposal.get();

    try {
      boolean proposalAccepted = tenderService.acceptProposal(proposal);
      ProposalResponseDto proposalDto = tenderProposalService.convertToDto(proposal);
      if (proposalAccepted) {
        return ResponseEntity.ok(proposalDto);
      }

      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
