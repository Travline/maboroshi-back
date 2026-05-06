package com_maboroshi.spring.contexts.identity.infrastructure.persistance;

import com_maboroshi.spring.contexts.identity.domain.entities.User;
import com_maboroshi.spring.contexts.identity.domain.entities.UserMail;
import com_maboroshi.spring.contexts.identity.domain.errors.InvalidMailException;
import com_maboroshi.spring.contexts.identity.domain.ports.UserRepository;
import com_maboroshi.spring.contexts.identity.infrastructure.errors.UserCannotBeSaved;
import com_maboroshi.spring.contexts.identity.infrastructure.errors.UserNotFound;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@Primary
public class JdbcTemplateUserRepository implements UserRepository {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final AppLogger appLogger;

  public JdbcTemplateUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, AppLogger appLogger) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.appLogger = appLogger;
  }

  @Override
  public Result<User, RepositoryError> save(User newUser) {
    String sql = """
            INSERT INTO users (id, username, mail, pwd, phone) 
            VALUES (:id, :name, :mail, :pwd, :phone)
            """;

    Map<String, Object> params = new HashMap<>();
    params.put("id", newUser.getId());
    params.put("name", newUser.getUsername());
    params.put("mail", newUser.getMail().toString());
    params.put("pwd", newUser.getPwd());
    params.put("phone", newUser.getPhone());

    try {
      int rowsCreated = namedParameterJdbcTemplate.update(sql, params);

      if (rowsCreated != 1) {
        return Result.fail(new UserCannotBeSaved("User not been saved"));
      }

      return Result.ok(newUser);

    } catch (DataAccessException dataAccessException) {
      appLogger.error("Database error saving user", dataAccessException);
      return Result.fail(new UserCannotBeSaved(dataAccessException.getMessage()));
    }
  }

  @Override
  public Result<User, RepositoryError> findByMail(UserMail userMail) {
    String sql = """
            SELECT * FROM users WHERE mail = ?
            """;

    try {
      User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                try {
                  return new User(
                          UUID.fromString(rs.getString("id")),
                          rs.getString("username"),
                          new UserMail(rs.getString("mail")),
                          rs.getString("pwd"),
                          rs.getString("phone"),
                          rs.getBoolean("is_active")
                  );
                } catch (InvalidMailException invalidMailException) {
                  return null;
                }
              },
              userMail.toString());

      if (user == null) {
        return Result.fail(new UserNotFound("User not found by mail"));
      }
      return Result.ok(user);
    } catch (DataAccessException dataAccessException) {
      appLogger.error("Database error searching user by mail", dataAccessException);
      return Result.fail(new UserNotFound(dataAccessException.getMessage()));
    }
  }
}
