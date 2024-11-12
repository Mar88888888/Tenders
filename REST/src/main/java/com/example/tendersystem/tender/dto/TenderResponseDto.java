package com.example.tendersystem.tender.dto;

import java.util.List;

public class TenderResponseDto {
  private Long id;
  private String title;
  private String description;
  private Double expectedPrice;
  private boolean isActive;
  private Long ownerId;
  private Long acceptedProposalId;
  private List<String> keywords;

  public Long getId() {
    return id;
  }

  public TenderResponseDto setId(Long id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public TenderResponseDto setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public TenderResponseDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public Double getExpectedPrice() {
    return expectedPrice;
  }

  public TenderResponseDto setExpectedPrice(Double price) {
    this.expectedPrice = price;
    return this;
  }

  public boolean isActive() {
    return isActive;
  }

  public TenderResponseDto setActive(boolean active) {
    isActive = active;
    return this;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public TenderResponseDto setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  public Long getAcceptedProposalId() {
    return acceptedProposalId;
  }

  public TenderResponseDto setAcceptedProposalId(Long proposalId) {
    this.acceptedProposalId = proposalId;
    return this;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public TenderResponseDto setKeywords(List<String> keywords) {
    this.keywords = keywords;
    return this;
  }
}
