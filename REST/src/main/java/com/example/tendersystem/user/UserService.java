package com.example.tendersystem.user;

import com.example.tendersystem.interfaces.user.UserDao;
import com.example.tendersystem.user.dto.CreateUserDto;
import com.example.tendersystem.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public User createUser(CreateUserDto userDto) {
    User existingUser = userDao.findByUsername(userDto.getUsername());
    if (existingUser != null) {
      throw new IllegalArgumentException("Username is already taken.");
    }

    User existingEmail = userDao.findByEmail(userDto.getEmail());
    if (existingEmail != null) {
      throw new IllegalArgumentException("Email is already taken.");
    }

    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());
    user.setEmail(userDto.getEmail());

    user.setId(userDao.create(user));
    return user;
  }

  public List<User> getAllUsers() {
    return userDao.findAll();
  }

  public Optional<User> getUserById(Long id) {
    return userDao.read(id);
  }

  public User updateUser(Long id, User updatedUser) {
    Optional<User> existingUser = getUserById(id);
    if (existingUser.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }

    updatedUser.setId(id);
    userDao.update(updatedUser);
    return updatedUser;
  }

  public void deleteUser(Long id) {
    userDao.delete(id);
  }

  public Optional<User> findByUsername(String username) {
    User user = userDao.findByUsername(username);
    return Optional.ofNullable(user);
  }

  public Optional<User> findByEmail(String email) {
    User user = userDao.findByEmail(email);
    return Optional.ofNullable(user);
  }

  public UserResponseDto convertToDto(User user) {
    UserResponseDto dto = new UserResponseDto();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    return dto;
  }

  public List<UserResponseDto> convertToDtoList(List<User> users) {
    return users.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }
}
