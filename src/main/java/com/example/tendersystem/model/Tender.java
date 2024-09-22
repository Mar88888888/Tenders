package com.example.tendersystem.model;

public class Tender {
  private Long id;
  private String title;
  private String description;
  private boolean isActive;
  private User owner;

  public Tender() {
  }

  public Tender(Long id, String title, String description, boolean isActive, User owner) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.isActive = isActive;
    this.owner = owner;
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
}
