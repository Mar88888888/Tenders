package com.example.tendersystem.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

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
    return StreamSupport.stream(userRepository.findAll().spliterator(), false)
        .toList();
  }

  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  public Optional<User> findByUsername(String name) {
    return getAllUsers().stream().filter(user -> user.getUsername().equals(name)).findFirst();
  }

  public User createUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
