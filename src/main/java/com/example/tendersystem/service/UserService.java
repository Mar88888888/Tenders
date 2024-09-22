package com.example.tendersystem.service;

import com.example.tendersystem.model.User;
import com.example.tendersystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  public Optional<User> findByUsername(String name) {
    return userRepository.findByUsername(name);
  }

  public User createUser(User user) {
    Long newId = userRepository.findAll().stream()
        .mapToLong(User::getId)
        .max()
        .orElse(0L) + 1;

    user.setId(newId);
    return userRepository.save(user);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
