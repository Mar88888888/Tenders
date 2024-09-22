package com.example.tendersystem.service;

import com.example.tendersystem.model.User;
import com.example.tendersystem.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean login(String username, String rawPassword) {
    User user = findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (passwordEncoder.matches(rawPassword, user.getPassword())) {
      return true;
    } else {
      throw new IllegalArgumentException("Invalid username or password");
    }
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
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
