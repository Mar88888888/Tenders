package com.example.tendersystem.user;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tendersystem.user.dto.UserResponseDto;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<String> signupSubmit(@RequestBody User user) {
    try {
      User createdUser = userService.createUser(user);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body("User registered successfully with ID: " + createdUser.getId());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Registration failed: " + e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(this.userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getUserById(@PathVariable Long id) {
    Optional<User> user = userService.getUserById(id);
    System.out.println(user);
    if (user.isPresent()) {
      UserResponseDto userDto = userService.convertToDto(user.get());
      return ResponseEntity.ok(userDto);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
  }
}
