package com.example.tendersystem.user.utils;

import com.example.tendersystem.user.User;
import com.example.tendersystem.user.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

  private final UserService userService;

  public UserUtils(UserService userService) {
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
