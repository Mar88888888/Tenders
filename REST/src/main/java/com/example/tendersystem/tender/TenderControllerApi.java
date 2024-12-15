package com.example.tendersystem.tender;

import com.example.tendersystem.tender.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Tender Management", description = "Operations related to managing tenders and proposals")
@RequestMapping("/tenders")
public interface TenderControllerApi {

  @Operation(summary = "Get all tenders", description = "Retrieve a paginated list of tenders with optional filtering by date, minimum sum, and maximum sum.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tenders", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TenderResponseDto.class))))
  @GetMapping
  ResponseEntity<List<TenderResponseDto>> getAllTenders(
      @Parameter(description = "Filter by date") @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date,
      @Parameter(description = "Minimum sum for tenders") @RequestParam(required = false) Double minSum,
      @Parameter(description = "Maximum sum for tenders") @RequestParam(required = false) Double maxSum,
      @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size for pagination") @RequestParam(defaultValue = "5") int size);

  @Operation(summary = "Get active tenders", description = "Retrieve a list of currently active tenders.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of active tenders", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TenderResponseDto.class))))
  @GetMapping("/active")
  ResponseEntity<List<TenderResponseDto>> getActiveTenders();

  @Operation(summary = "Get user-specific tenders", description = "Retrieve tenders specific to a user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user's tenders", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TenderResponseDto.class)))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @GetMapping("/my/{userId}")
  ResponseEntity<List<TenderResponseDto>> getMyTenders(@Parameter(description = "User ID") @PathVariable Long userId);

  @Operation(summary = "Get tender by ID", description = "Retrieve a tender by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tender found", content = @Content(schema = @Schema(implementation = TenderResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Tender not found", content = @Content)
  })
  @GetMapping("/{id}")
  ResponseEntity<TenderResponseDto> getTenderById(@Parameter(description = "Tender ID") @PathVariable Long id);

  @Operation(summary = "Create a new tender", description = "Create a new tender and return the created tender details.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Tender successfully created", content = @Content(schema = @Schema(implementation = TenderResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "404", description = "Related entity (owner) not found", content = @Content)
  })
  @PostMapping
  ResponseEntity<TenderResponseDto> createTender(@RequestBody CreateTenderDto createTenderDto);

  @Operation(summary = "Update tender status", description = "Update the status (active/inactive) of an existing tender.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tender status successfully updated", content = @Content(schema = @Schema(implementation = TenderResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "404", description = "Tender not found", content = @Content)
  })
  @PatchMapping("/{id}/status")
  ResponseEntity<TenderResponseDto> updateTenderStatus(
      @Parameter(description = "Tender ID") @PathVariable Long id,
      @RequestBody TenderStatusUpdateDto statusUpdateDto);

  @Operation(summary = "Delete a tender", description = "Delete an existing tender by ID.")
  @ApiResponse(responseCode = "204", description = "Tender successfully deleted", content = @Content)
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteTender(@Parameter(description = "Tender ID") @PathVariable Long id);

  @Operation(summary = "Search tenders", description = "Search tenders by keyword.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully found tenders matching the keyword", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TenderResponseDto.class)))),
      @ApiResponse(responseCode = "400", description = "Keyword must be provided", content = @Content)
  })
  @GetMapping("/search")
  ResponseEntity<List<TenderResponseDto>> searchTenders(
      @Parameter(description = "Search keyword") @RequestParam String keyword);

  @Operation(summary = "Create a proposal for a tender", description = "Create a new proposal for a specified tender.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Proposal successfully created", content = @Content(schema = @Schema(implementation = ProposalResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid proposal properties", content = @Content),
      @ApiResponse(responseCode = "404", description = "Tender not found", content = @Content)
  })
  @PostMapping("/{tenderId}/proposals")
  ResponseEntity<ProposalResponseDto> createProposal(
      @Parameter(description = "Tender ID") @PathVariable Long tenderId,
      @RequestBody CreateProposalDto tenderProposalDto);
}
