package com.example.tendersystem.controller;

import com.example.tendersystem.model.Tender;
import com.example.tendersystem.model.TenderProposal;
import com.example.tendersystem.service.TenderProposalService;
import com.example.tendersystem.service.TenderService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tenders")
public class TenderController {

  @Value("${app.url}")
  private String appUrl;

  private final TenderService tenderService;
  private final TenderProposalService tenderProposalService;

  public TenderController(TenderService tenderService, TenderProposalService tenderProposalService) {
    this.tenderService = tenderService;
    this.tenderProposalService = tenderProposalService;
  }

  @GetMapping
  public String getAllTenders(Model model) {
    List<Tender> tenders = tenderService.getAllTenders();
    model.addAttribute("tenders", tenders);
    return "tenders.html";
  }

  @GetMapping("/active")
  public String getActiveTenders(Model model) {
    List<Tender> tenders = tenderService.getActiveTenders();
    model.addAttribute("tenders", tenders);
    return "tenders.html";
  }

  @GetMapping("/proposals/my")
  public String getMyProposals(Model model) {
    List<TenderProposal> proposals = tenderProposalService.getMyProposals();
    model.addAttribute("proposals", proposals);
    return "proposals.html";
  }

  @GetMapping("/my")
  public String getMyTenders(Model model) {
    List<Tender> tenders = tenderService.getMyTenders();
    model.addAttribute("tenders", tenders);
    return "tenders.html";
  }

  @GetMapping("/add")
  public String showAddTenderForm(Model model) {
    model.addAttribute("tender", new Tender());
    return "addTender.html";
  }

  @GetMapping("/{id}")
  public String getTenderById(@PathVariable Long id, Model model, Principal principal) {
    tenderService.getTenderById(id).ifPresent(tender -> model.addAttribute("tender", tender));
    model.addAttribute("appUrl", appUrl);
    List<TenderProposal> proposals = tenderProposalService.getProposalsByTenderId(id);
    model.addAttribute("proposals", proposals);
    model.addAttribute("principal", principal);
    return "detail.html";
  }

  @PostMapping
  public String createTender(@ModelAttribute Tender tender) {
    tenderService.createTender(tender);
    return "redirect:/tenders";
  }

  @PostMapping("/toggleActive/{id}")
  public String toggleActive(@PathVariable Long id) {
    tenderService.toggleActive(id);
    return "redirect:/tenders/" + String.valueOf(id);
  }

  @DeleteMapping("/{id}")
  public String deleteTender(@PathVariable Long id) {
    tenderService.deleteTender(id);
    tenderProposalService.deleteAllProposalsByTenderId(id);
    return "redirect:/tenders";
  }

  @GetMapping("/search")
  public String searchTenders(Model model, @RequestParam String keyword) {
    List<Tender> tenders = tenderService.searchTenders(keyword);
    model.addAttribute("tenders", tenders);
    return "tenders.html";
  }

  @GetMapping("/{tenderId}/proposals/add")
  public String showAddProposalForm(@PathVariable Long tenderId, Model model) {
    tenderService.getTenderById(tenderId).ifPresent(tender -> model.addAttribute("tender", tender));
    model.addAttribute("tenderProposal", new TenderProposal());
    return "addProposal.html";
  }

  @PostMapping("/{tenderId}/addproposal")
  public String createProposal(@PathVariable Long tenderId, @ModelAttribute TenderProposal tenderProposal) {
    tenderService.getTenderById(tenderId).ifPresent(tender -> {
      tenderProposalService.createTenderProposal(tenderProposal, tender);
    });
    return "redirect:/tenders/" + tenderId;
  }

}
