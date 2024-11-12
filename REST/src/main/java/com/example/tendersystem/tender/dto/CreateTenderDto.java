package com.example.tendersystem.tender.dto;

import java.util.List;

public class CreateTenderDto {
  private String title;
  private String description;
  private Double expectedPrice;
  private List<String> keywords;
  private Long ownerId;

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

  public Double getExpectedPrice() {
    return expectedPrice;
  }

  public void setExpectedPrice(Double expectedPrice) {
    this.expectedPrice = expectedPrice;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }
}
