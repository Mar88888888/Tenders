package com.example.tendersystem.proposal.dto;

public class ProposalStatusUpdateDto {
  private boolean accepted;

  public boolean isActive() {
    return accepted;
  }

  public void setActive(boolean accepted) {
    this.accepted = accepted;
  }
}
