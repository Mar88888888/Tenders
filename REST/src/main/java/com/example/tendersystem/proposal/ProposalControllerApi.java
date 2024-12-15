package com.example.tendersystem.proposal;

import com.example.tendersystem.tender.dto.ProposalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tender Proposal Management", description = "Operations related to managing tender proposals")
@RequestMapping("/proposals")
public interface ProposalControllerApi {

  @Operation(summary = "Get user's proposals", description = "Retrieve all proposals made by a specific user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of proposals", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @GetMapping("/my/{userId}")
  ResponseEntity<List<ProposalResponseDto>> getMyProposals(
      @Parameter(description = "ID of the user to retrieve proposals for") @PathVariable Long userId);

  @Operation(summary = "Accept a proposal", description = "Accept a specific proposal by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Proposal accepted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Proposal not found", content = @Content),
      @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
      @ApiResponse(responseCode = "409", description = "Proposal cannot be accepted due to conflict, this tender already has accepted proposal", content = @Content)
  })
  @PatchMapping("/{proposalId}/status")
  ResponseEntity<ProposalResponseDto> acceptProposal(
      @Parameter(description = "ID of the proposal to accept") @PathVariable Long proposalId);
}
