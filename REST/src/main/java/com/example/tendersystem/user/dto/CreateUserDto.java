package com.example.tendersystem.user.dto;

public class CreateUserDto {
  private String username;
  private String password;

  public String getPassword() {
    return password;
  }

  public void setpassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}