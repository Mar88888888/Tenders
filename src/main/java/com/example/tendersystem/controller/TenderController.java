package com.example.tendersystem.controller;

import com.example.tendersystem.model.Tender;
import com.example.tendersystem.service.TenderService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tenders")
public class TenderController {

  @Value("${app.url}")
  private String appUrl;

  private final TenderService tenderService;

  public TenderController(TenderService tenderService) {
    this.tenderService = tenderService;
  }

  @GetMapping
  public String getAllTenders(Model model) {
    List<Tender> tenders = tenderService.getAllTenders();
    model.addAttribute("tenders", tenders);
    return "tenders.html";
  }

  @GetMapping("/active")
  public String getActiveTenders(Model model) {
    List<Tender> tenders = tenderService.getAllTenders().stream().filter(tender -> tender.isActive()).toList();
    model.addAttribute("tenders", tenders);
    return "tenders.html";
  }

  @GetMapping("/add")
  public String showAddTenderForm(Model model) {
    model.addAttribute("tender", new Tender());
    return "addTender.html";
  }

  @GetMapping("/{id}")
  public String getTenderById(@PathVariable Long id, Model model) {
    tenderService.getTenderById(id).ifPresent(tender -> model.addAttribute("tender", tender));
    model.addAttribute("appUrl", appUrl);
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
    return "redirect:/tenders";
  }

  @GetMapping("/search")
  public String searchTenders(Model model, @RequestParam String keyword) {
    List<Tender> tenders = tenderService.searchTenders(keyword);
    model.addAttribute("tenders", tenders);
    return "tenders.html";
  }
}
