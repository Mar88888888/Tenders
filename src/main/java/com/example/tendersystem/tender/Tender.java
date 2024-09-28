package com.example.tendersystem.tender;

import java.util.Date;
import java.util.List;

import com.example.tendersystem.proposal.TenderProposal;
import com.example.tendersystem.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Tender {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;
  private String description;
  private boolean isActive;
  private Date createdDate;
  private Double expectedValue;

  @ElementCollection
  private List<String> keywords;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @OneToMany(mappedBy = "tender", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TenderProposal> proposals;

  public Tender() {
  }

  public Tender(Long id, String title, String description, boolean isActive, User owner, List<String> keywords) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.isActive = isActive;
    this.owner = owner;
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

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public Double getExpectedValue() {
    return expectedValue;
  }

  public void setExpectedValue(Double expectedValue) {
    this.expectedValue = expectedValue;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
}
