package com.example.tendersystem.user;

import org.springframework.stereotype.Service;

import com.example.tendersystem.user.dto.UserResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    userRepository.findAll().forEach(users::add);
    return users;
  }

  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  public User updateUser(Long id, User updatedUser) {
    if (!userRepository.existsById(id)) {
      throw new IllegalArgumentException("User not found");
    }
    updatedUser.setId(id);
    return userRepository.save(updatedUser);
  }

  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new IllegalArgumentException("User not found");
    }
    userRepository.deleteById(id);
  }

  public Optional<User> findByUsername(String username) {
    List<User> users = getAllUsers();
    return users.stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  public UserResponseDto convertToDto(User user) {
    UserResponseDto dto = new UserResponseDto();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    return dto;
  }

}
