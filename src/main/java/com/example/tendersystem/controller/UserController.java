package com.example.tendersystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.tendersystem.model.User;
import com.example.tendersystem.service.UserService;;

@Controller
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("user", new User());
    return "signup";
  }

  @PostMapping("/login")
  public String loginSubmit(@ModelAttribute User user, Model model) {
    userService.createUser(user);

    model.addAttribute("success", "User registered successfully!");
    return "redirect:/login";
  }

  @PostMapping("/signup")
  public String signupSubmit(@ModelAttribute User user, Model model) {
    userService.createUser(user);

    model.addAttribute("success", "User registered successfully!");
    return "redirect:/login";
  }

  @GetMapping("/logout")
  public String logout() {
    return "redirect:/user/login";
  }
}
