package com.example.tendersystem.interfaces.user;

import com.example.tendersystem.interfaces.GenericDao;
import com.example.tendersystem.user.User;

public interface UserDao extends GenericDao<User, Long> {
  User findByUsername(String username);

  User findByEmail(String email);

}
