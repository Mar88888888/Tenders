package com.example.tendersystem.tender;

import com.example.tendersystem.proposal.TenderProposal;
import com.example.tendersystem.proposal.TenderProposalService;
import com.example.tendersystem.tender.dto.CreateProposalDto;
import com.example.tendersystem.tender.dto.CreateTenderDto;
import com.example.tendersystem.tender.dto.ProposalResponseDto;
import com.example.tendersystem.tender.dto.TenderResponseDto;
import com.example.tendersystem.tender.dto.TenderStatusUpdateDto;
import com.example.tendersystem.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Tender Management", description = "Operations related to managing tenders and proposals")
@RestController
@RequestMapping("/tenders")
public class TenderController {

  private final TenderService tenderService;
  private final TenderProposalService tenderProposalService;
  private final UserService userService;

  public TenderController(
      TenderService tenderService,
      TenderProposalService tenderProposalService,
      UserService userService) {
    this.tenderService = tenderService;
    this.tenderProposalService = tenderProposalService;
    this.userService = userService;
  }

  @Operation(summary = "Get all tenders", description = "Retrieve a paginated list of tenders with optional filtering by date, minimum sum, and maximum sum.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tenders", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TenderResponseDto.class))))
  @GetMapping
  public ResponseEntity<List<TenderResponseDto>> getAllTenders(
      @Parameter(description = "Filter by date") @RequestParam(required = false) LocalDate date,
      @Parameter(description = "Minimum sum for tenders") @RequestParam(required = false) Double minSum,
      @Parameter(description = "Maximum sum for tenders") @RequestParam(required = false) Double maxSum,
      @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page) {

    List<Tender> tenders = tenderService.getFilteredTenders(date, minSum, maxSum, page, 5);
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @Operation(summary = "Get active tenders", description = "Retrieve a list of currently active tenders.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of active tenders", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TenderResponseDto.class))))
  @GetMapping("/active")
  public ResponseEntity<List<TenderResponseDto>> getActiveTenders() {
    List<Tender> tenders = tenderService.getActiveTenders();
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @Operation(summary = "Get user-specific tenders", description = "Retrieve tenders specific to a user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user's tenders", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TenderResponseDto.class)))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @GetMapping("/my/{userId}")
  public ResponseEntity<List<TenderResponseDto>> getMyTenders(
      @Parameter(description = "User ID") @PathVariable(name = "userId") Long userId) {

    if (userService.getUserById(userId).isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    List<Tender> tenders = tenderService.getMyTenders(userId);
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @Operation(summary = "Get tender by ID", description = "Retrieve a tender by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tender found", content = @Content(schema = @Schema(implementation = TenderResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Tender not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<TenderResponseDto> getTenderById(@Parameter(description = "Tender ID") @PathVariable Long id) {
    Optional<Tender> tender = tenderService.getTenderById(id);
    if (tender.isPresent()) {
      TenderResponseDto tenderDto = tenderService.convertToDto(tender.get());
      return ResponseEntity.ok(tenderDto);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @Operation(summary = "Create a new tender", description = "Create a new tender and return the created tender details.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Tender successfully created", content = @Content(schema = @Schema(implementation = TenderResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "404", description = "Related entity (owner) not found", content = @Content)
  })
  @PostMapping
  public ResponseEntity<TenderResponseDto> createTender(@RequestBody CreateTenderDto createTenderDto) {
    try {
      Tender createdTender = tenderService.createTender(createTenderDto);
      TenderResponseDto tenderDto = tenderService.convertToDto(createdTender);
      return ResponseEntity.status(HttpStatus.CREATED).body(tenderDto);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Operation(summary = "Update tender status", description = "Update the status (active/inactive) of an existing tender.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tender status successfully updated", content = @Content(schema = @Schema(implementation = TenderResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "404", description = "Tender not found", content = @Content)
  })
  @PutMapping("/{id}/status")
  public ResponseEntity<TenderResponseDto> updateTenderStatus(
      @Parameter(description = "Tender ID") @PathVariable Long id,
      @RequestBody TenderStatusUpdateDto statusUpdateDto) {
    try {
      if (statusUpdateDto.isActive() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
      Optional<Tender> tender = tenderService.getTenderById(id);
      if (tender.isPresent()) {
        tenderService.updateTenderStatus(id, statusUpdateDto.isActive());
        TenderResponseDto tenderDto = tenderService.convertToDto(tender.get());
        return ResponseEntity.ok(tenderDto);
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Operation(summary = "Delete a tender", description = "Delete an existing tender by ID.")
  @ApiResponse(responseCode = "200", description = "Tender successfully deleted", content = @Content)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTender(@Parameter(description = "Tender ID") @PathVariable Long id) {
    tenderService.deleteTender(id);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Search tenders", description = "Search tenders by keyword.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully found tenders matching the keyword", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TenderResponseDto.class)))),
      @ApiResponse(responseCode = "400", description = "Keyword must be provided", content = @Content)
  })
  @GetMapping("/search")
  public ResponseEntity<List<TenderResponseDto>> searchTenders(
      @Parameter(description = "Search keyword") @RequestParam String keyword) {
    List<Tender> tenders = tenderService.searchTenders(keyword);
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @Operation(summary = "Create a proposal for a tender", description = "Create a new proposal for a specified tender.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Proposal successfully created", content = @Content(schema = @Schema(implementation = ProposalResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid proposal properties", content = @Content),
      @ApiResponse(responseCode = "404", description = "Tender not found", content = @Content)
  })
  @PostMapping("/{tenderId}/proposals")
  public ResponseEntity<ProposalResponseDto> createProposal(
      @Parameter(description = "Tender ID") @PathVariable Long tenderId,
      @RequestBody CreateProposalDto tenderProposalDto) {

    try {
      TenderProposal proposal = tenderProposalService.createTenderProposal(tenderProposalDto, tenderId);
      ProposalResponseDto proposalDto = tenderProposalService.convertToDto(proposal);
      return ResponseEntity.status(HttpStatus.CREATED).body(proposalDto);
    } catch (ResponseStatusException ex) {
      return ResponseEntity.status(ex.getStatusCode()).build();
    }
  }
}
