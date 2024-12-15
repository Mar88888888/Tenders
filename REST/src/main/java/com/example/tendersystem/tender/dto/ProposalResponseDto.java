package com.example.tendersystem.tender.dto;

import com.example.tendersystem.proposal.ProposalStatus;

public class ProposalResponseDto {
  private Long id;
  private String description;
  private Double price;
  private Long proposerId;
  private Long tenderId;
  private ProposalStatus status = ProposalStatus.PENDING;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Long getProposerId() {
    return proposerId;
  }

  public void setProposerId(Long proposerId) {
    this.proposerId = proposerId;
  }

  public Long getTenderId() {
    return tenderId;
  }

  public void setTenderId(Long tenderId) {
    this.tenderId = tenderId;
  }

  public ProposalStatus getStatus() {
    return status;
  }

  public void setStatus(ProposalStatus status) {
    this.status = status;
  }

}
