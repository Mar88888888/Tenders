package com.example.tendersystem.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;;

@Controller
@RequestMapping("/user")
public class UserController {

  private UserService userService;

  @Autowired
  public void setUserService(UserService userService) {
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
    userService.login(user.getUsername(), user.getPassword());
    model.addAttribute("success", "User logged in successfully!");
    return "redirect:/login";
  }

  @PostMapping("/signup")
  public String signupSubmit(@ModelAttribute User user, Model model) {
    userService.createUser(user);

    model.addAttribute("success", "User registered successfully!");
    return "redirect:/login";
  }

}
