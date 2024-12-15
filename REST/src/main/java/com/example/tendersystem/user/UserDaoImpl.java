package com.example.tendersystem.user;

import com.example.tendersystem.interfaces.user.UserDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
  private final JdbcTemplate jdbcTemplate;

  public UserDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Long create(User user) {
    String sql = "INSERT INTO app_user (username, password, email) VALUES (?, ?, ?) RETURNING id";
    return jdbcTemplate.queryForObject(sql, Long.class, user.getUsername(), user.getPassword(), user.getEmail());
  }

  @Override
  public Optional<User> read(Long id) {
    String sql = "SELECT * FROM app_user WHERE id = ?";
    List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
    return users.stream().findFirst();
  }

  @Override
  public List<User> findAll() {
    String sql = "SELECT * FROM app_user";
    return jdbcTemplate.query(sql, new UserRowMapper());
  }

  @Override
  public void update(User user) {
    String sql = "UPDATE app_user SET username = ?, password = ?, email = ? WHERE id = ?";
    jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail(), user.getId());
  }

  @Override
  public void delete(Long id) {
    String sql = "DELETE FROM app_user WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public User findByUsername(String username) {
    String sql = "SELECT * FROM app_user WHERE username = ?";
    return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
  }

  @Override
  public User findByEmail(String email) {
    String sql = "SELECT * FROM app_user WHERE email = ?";
    return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
  }

  private static class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
      return new User(
          rs.getLong("id"),
          rs.getString("username"),
          rs.getString("password"),
          rs.getString("email"));
    }
  }
}
