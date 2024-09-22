package com.example.tendersystem.model;

public class TenderProposal {
  private Long id;
  private String proposalText;
  private Tender tender;
  private User proposer;
  private Double porposalSum;

  public TenderProposal() {
  }

  public TenderProposal(Long id, String proposalText, Tender tender, User proposer) {
    this.id = id;
    this.proposalText = proposalText;
    this.tender = tender;
    this.proposer = proposer;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProposalText() {
    return proposalText;
  }

  public void setProposalText(String proposalText) {
    this.proposalText = proposalText;
  }

  public Tender getTender() {
    return tender;
  }

  public void setTender(Tender tender) {
    this.tender = tender;
  }

  public User getProposer() {
    return proposer;
  }

  public void setProposer(User proposer) {
    this.proposer = proposer;
  }

  public Double getPorposalSum() {
    return porposalSum;
  }

  public void setPorposalSum(Double porposalSum) {
    this.porposalSum = porposalSum;
  }
}
