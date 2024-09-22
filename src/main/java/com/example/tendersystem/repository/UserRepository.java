package com.example.tendersystem.repository;

import com.example.tendersystem.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

  private List<User> users = new ArrayList<>();

  public UserRepository() {
    users.add(new User(1L, "user1", "password1"));
    users.add(new User(1L, "user2", "password2"));
  }

  public List<User> findAll() {
    return users;
  }

  public Optional<User> findById(Long id) {
    return users.stream().filter(user -> user.getId().equals(id)).findFirst();
  }

  public Optional<User> findByUsername(String name) {
    return users.stream().filter(user -> user.getUsername().equals(name)).findFirst();
  }

  public User save(User user) {
    users.add(user);
    return user;
  }

  public void deleteById(Long id) {
    users.removeIf(user -> user.getId().equals(id));
  }
}
