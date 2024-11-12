package com.example.tendersystem.proposal;

import java.util.Date;

import com.example.tendersystem.tender.Tender;
import com.example.tendersystem.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TenderProposal {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String description;
  private Date submittedDate;
  private Double price;

  @ManyToOne
  @JoinColumn(name = "tender_id", nullable = false)
  private Tender tender;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User proposer;

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
