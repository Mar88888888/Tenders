package com.example.tendersystem.proposal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Proposal {
  private Long id;
  private String description;
  private LocalDateTime submittedDate;
  private BigDecimal price;
  private Long tenderId;
  private Long proposerId;
  private ProposalStatus status;

  public Proposal() {
  }

  public Proposal(Long id, String description, Long tenderId, Long proposerId, BigDecimal price,
      LocalDateTime submittedDate) {
    this.id = id;
    this.description = description;
    this.tenderId = tenderId;
    this.proposerId = proposerId;
    this.price = price;
    this.submittedDate = submittedDate;
  }

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

  public Long getTenderId() {
    return tenderId;
  }

  public void setTenderId(Long tenderId) {
    this.tenderId = tenderId;
  }

  public Long getProposerId() {
    return proposerId;
  }

  public void setProposerId(Long proposerId) {
    this.proposerId = proposerId;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public LocalDateTime getSubmittedDate() {
    return submittedDate;
  }

  public void setSubmittedDate(LocalDateTime submittedDate) {
    this.submittedDate = submittedDate;
  }

  public ProposalStatus getStatus() {
    return status;
  }

  public void setStatus(ProposalStatus status) {
    this.status = status;
  }
}
