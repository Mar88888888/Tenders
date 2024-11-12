package com.example.tendersystem.tender.dto;

public class CreateProposalDto {
  private String description;
  private Double price;
  private Long proposerId;

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
}
