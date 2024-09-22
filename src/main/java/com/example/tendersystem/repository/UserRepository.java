package com.example.tendersystem.repository;

import com.example.tendersystem.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

  private final List<User> users = new ArrayList<>();

  public List<User> findAll() {
    return new ArrayList<>(users);
  }

  public Optional<User> findById(Long id) {
    return users.stream().filter(user -> user.getId().equals(id)).findFirst();
  }

  public Optional<User> findByUsername(String username) {
    return users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
  }

  public void save(User user) {
    users.add(user);
  }

  public void deleteById(Long id) {
    users.removeIf(user -> user.getId().equals(id));
  }
}
