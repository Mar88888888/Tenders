package com.example.tendersystem.proposal;

import com.example.tendersystem.tender.TenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proposals")
public class TenderProposalController {

  @Value("${app.url}")
  private String appUrl;

  private TenderService tenderService;
  private TenderProposalService tenderProposalService;

  public TenderProposalController(TenderService tenderService, TenderProposalService tenderProposalService) {
    this.tenderService = tenderService;
    this.tenderProposalService = tenderProposalService;
  }

  @GetMapping("/my")
  public String getMyProposals(Model model) {
    List<TenderProposal> proposals = tenderProposalService.getMyProposals();
    model.addAttribute("proposals", proposals);
    return "proposals.html";
  }

  @PostMapping("/{proposalId}/status")
  public String acceptProposal(@PathVariable Long proposalId, Principal principal) {
    Optional<TenderProposal> optionalProposal = tenderProposalService.getTenderProposalById(proposalId);

    if (!optionalProposal.isPresent()) {
      return "redirect:/tenders/active?error=ProposalNotFound";
    }

    TenderProposal proposal = optionalProposal.get();
    return tenderService.acceptProposal(proposal, principal.getName());
  }
}
