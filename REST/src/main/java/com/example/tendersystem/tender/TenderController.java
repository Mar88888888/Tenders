package com.example.tendersystem.tender;

import com.example.tendersystem.proposal.Proposal;
import com.example.tendersystem.proposal.ProposalService;
import com.example.tendersystem.proposal.ProposalStatus;
import com.example.tendersystem.tender.dto.CreateProposalDto;
import com.example.tendersystem.tender.dto.CreateTenderDto;
import com.example.tendersystem.tender.dto.ProposalResponseDto;
import com.example.tendersystem.tender.dto.TenderResponseDto;
import com.example.tendersystem.tender.dto.TenderStatusUpdateDto;
import com.example.tendersystem.user.UserService;

import org.springframework.format.annotation.DateTimeFormat;
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
  private final ProposalService tenderProposalService;
  private final UserService userService;

  public TenderController(
      TenderService tenderService,
      ProposalService tenderProposalService,
      UserService userService) {
    this.tenderService = tenderService;
    this.tenderProposalService = tenderProposalService;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<TenderResponseDto>> getAllTenders(
      @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date,
      @RequestParam(required = false) Double minSum,
      @RequestParam(required = false) Double maxSum,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size) {

    List<Tender> tenders = tenderService.getFilteredTenders(date, minSum, maxSum, page, size);
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
  public ResponseEntity<List<TenderResponseDto>> getMyTenders(@PathVariable Long userId) {

    if (userService.getUserById(userId).isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    List<Tender> tenders = tenderService.getUserTenders(userId);
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TenderResponseDto> getTenderById(@PathVariable Long id) {
    Optional<Tender> tender = tenderService.getTenderById(id);
    if (tender.isPresent()) {
      TenderResponseDto tenderDto = tenderService.convertToDto(tender.get());
      return ResponseEntity.ok(tenderDto);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

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

  @PatchMapping("/{id}/status")
  public ResponseEntity<TenderResponseDto> updateTenderStatus(
      @PathVariable Long id,
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

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTender(@PathVariable Long id) {
    tenderService.deleteTender(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/search")
  public ResponseEntity<List<TenderResponseDto>> searchTenders(@RequestParam String keyword) {
    List<Tender> tenders = tenderService.searchTenders(keyword);
    List<TenderResponseDto> tenderDtos = tenderService.convertToDtoList(tenders);
    return ResponseEntity.ok(tenderDtos);
  }

  @PostMapping("/{tenderId}/proposals")
  public ResponseEntity<ProposalResponseDto> createProposal(
      @PathVariable Long tenderId,
      @RequestBody CreateProposalDto tenderProposalDto) {

    try {
      Proposal proposal = tenderProposalService.createTenderProposal(tenderProposalDto, tenderId);
      proposal.setStatus(ProposalStatus.PENDING);
      ProposalResponseDto proposalDto = tenderProposalService.convertToDto(proposal);
      return ResponseEntity.status(HttpStatus.CREATED).body(proposalDto);
    } catch (ResponseStatusException ex) {
      return ResponseEntity.status(ex.getStatusCode()).build();
    }
  }
}
