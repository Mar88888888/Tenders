package com.example.tendersystem.tender;

import java.util.Date;
import java.util.List;

public class Tender {
  private Long id;
  private String title;
  private String description;
  private Date createdDate;
  private Double expectedPrice;
  private boolean isActive;
  private Long ownerId;
  private List<String> keywords;
  private Long acceptedProposalId;

  public Tender() {
  }

  public Tender(Long id, String title, String description, boolean isActive, Long ownerId, List<String> keywords) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.isActive = isActive;
    this.ownerId = ownerId;
    this.keywords = keywords;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public Double getExpectedPrice() {
    return expectedPrice;
  }

  public void setExpectedPrice(Double expectedPrice) {
    this.expectedPrice = expectedPrice;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Long getAcceptedProposalId() {
    return acceptedProposalId;
  }

  public void setAcceptedProposalId(Long acceptedProposalId) {
    this.acceptedProposalId = acceptedProposalId;
  }
}
