package com.example.tendersystem.model;

public class Tender {
  private Long id;
  private String title;
  private String description;
  private boolean isActive;
  private User owner;
  private String[] keywords;

  public Tender() {
  }

  public Tender(Long id, String title, String description, boolean isActive, User owner, String[] keywords) {
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

  public String[] getKeywords() {
    return keywords;
  }

  public void setKeywords(String[] keywords) {
    this.keywords = keywords;
  }
}
