package com.example.tendersystem.user.utils;

import com.example.tendersystem.user.User;
import com.example.tendersystem.user.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserUtils {

  private UserService userService;

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      String username = ((UserDetails) authentication.getPrincipal()).getUsername();
      return userService.findByUsername(username).orElse(null);
    }
    return null;
  }
}
