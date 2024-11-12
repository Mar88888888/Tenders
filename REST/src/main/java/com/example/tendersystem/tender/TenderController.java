package com.example.tendersystem.tender;

import com.example.tendersystem.proposal.TenderProposal;
import com.example.tendersystem.proposal.TenderProposalService;
import com.example.tendersystem.tender.dto.CreateProposalDto;
import com.example.tendersystem.tender.dto.CreateTenderDto;
import com.example.tendersystem.tender.dto.ProposalResponseDto;
import com.example.tendersystem.tender.dto.TenderResponseDto;
import com.example.tendersystem.tender.dto.TenderStatusUpdateDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tenders")
public class TenderController {

  private final TenderService tenderService;
  private final TenderProposalService tenderProposalService;

  public TenderController(
      TenderService tenderService,
      TenderProposalService tenderProposalService) {
    this.tenderService = tenderService;
    this.tenderProposalService = tenderProposalService;
  }

  // Pagination + filtering
  @GetMapping
  public ResponseEntity<List<TenderResponseDto>> getAllTenders(
      @RequestParam(required = false) LocalDate date,
      @RequestParam(required = false) Double minSum,
      @RequestParam(required = false) Double maxSum,
      @RequestParam(defaultValue = "0") int page) {

    List<Tender> tenders = tenderService.getFilteredTenders(date, minSum, maxSum, page, 5);
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @GetMapping("/active")
  public ResponseEntity<List<TenderResponseDto>> getActiveTenders() {
    List<Tender> tenders = tenderService.getActiveTenders();
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @GetMapping("/my/{userId}")
  public ResponseEntity<List<TenderResponseDto>> getMyTenders(@PathVariable(name = "userId") Long id) {
    List<Tender> tenders = tenderService.getMyTenders(id);
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getTenderById(@PathVariable Long id) {
    Optional<Tender> tender = tenderService.getTenderById(id);
    if (tender.isPresent()) {
      TenderResponseDto tenderDto = tenderService.convertToDto(tender.get());
      return ResponseEntity.ok(tenderDto);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tender not found");
  }

  @PostMapping
  public ResponseEntity<?> createTender(@RequestBody CreateTenderDto createTenderDto) {
    System.out.println(createTenderDto);
    try {
      Tender createdTender = tenderService.createTender(createTenderDto);
      TenderResponseDto tenderDto = tenderService.convertToDto(createdTender);
      return ResponseEntity.status(HttpStatus.CREATED).body(tenderDto);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<String> updateTenderStatus(
      @PathVariable Long id,
      @RequestBody TenderStatusUpdateDto statusUpdateDto) {
    try {
      boolean statusUpdated = tenderService.updateTenderStatus(id, statusUpdateDto.isActive());

      if (statusUpdated) {
        return ResponseEntity.ok("Tender status updated successfully");
      }

      return ResponseEntity.ok("Tender status is already " + (statusUpdateDto.isActive() ? "active" : "inactive"));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tender not found");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteTender(@PathVariable Long id) {
    tenderService.deleteTender(id);
    return ResponseEntity.ok("Tender deleted successfully");
  }

  @GetMapping("/search")
  public ResponseEntity<List<TenderResponseDto>> searchTenders(@RequestParam String keyword) {
    List<Tender> tenders = tenderService.searchTenders(keyword);
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @PostMapping("/{tenderId}/proposals")
  public ResponseEntity<?> createProposal(@PathVariable Long tenderId,
      @RequestBody CreateProposalDto tenderProposalDto) {

    try {
      TenderProposal proposal = tenderProposalService.createTenderProposal(tenderProposalDto, tenderId);
      ProposalResponseDto proposalDto = tenderProposalService.convertToDto(proposal);
      return ResponseEntity.status(HttpStatus.CREATED).body(proposalDto);

    } catch (ResponseStatusException ex) {
      return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
  }

}
