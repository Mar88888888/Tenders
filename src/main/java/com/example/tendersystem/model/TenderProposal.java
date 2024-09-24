package com.example.tendersystem.model;

import java.util.Date;

public class TenderProposal {
  private Long id;
  private String description;
  private Tender tender;
  private User proposer;
  private Date submittedDate;

  private Double price;

  public TenderProposal() {
  }

  public TenderProposal(Long id, String description, Tender tender, User proposer, Double price) {
    this.id = id;
    this.description = description;
    this.tender = tender;
    this.proposer = proposer;
    this.price = price;
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

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Date getSubmittedDate() {
    return submittedDate;
  }

  public void setSubmittedDate(Date submittedDate) {
    this.submittedDate = submittedDate;
  }
}
