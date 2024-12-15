package com.example.tendersystem.user;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tendersystem.user.dto.CreateUserDto;
import com.example.tendersystem.user.dto.UserResponseDto;

@RestController
@RequestMapping("/user")
public class UserController implements UserControllerApi {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<UserResponseDto> signupSubmit(
      @RequestBody CreateUserDto user) {
    try {
      User createdUser = userService.createUser(user);
      UserResponseDto userDto = userService.convertToDto(createdUser);
      return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> userDtos = this.userService.convertToDtoList(this.userService.getAllUsers());
    return ResponseEntity.ok(userDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUserById(
      @PathVariable Long id) {
    Optional<User> user = userService.getUserById(id);
    if (user.isPresent()) {
      UserResponseDto userDto = userService.convertToDto(user.get());
      return ResponseEntity.ok(userDto);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }
}
