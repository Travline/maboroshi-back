package com_maboroshi.spring.contexts.identity.infrastructure.persistance;

import com_maboroshi.spring.contexts.identity.domain.entities.User;
import com_maboroshi.spring.contexts.identity.domain.entities.UserMail;
import com_maboroshi.spring.contexts.identity.domain.ports.UserRepository;
import com_maboroshi.spring.contexts.identity.infrastructure.errors.UserNotFound;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MockedUserRepository implements UserRepository {
  private List<User> users = new ArrayList<User>();

  @Override
  public Result<User, RepositoryError> save(User newUser) {
    users.add(newUser);
    return Result.ok(newUser);
  }

  @Override
  public Result<User, RepositoryError> findByMail(UserMail userMail) {
    User userFound = users.stream().filter(user -> user.getMail().equals(userMail)).findFirst().orElse(null);
    if (userFound == null) {
      return Result.fail(new UserNotFound("User not found by mail"));
    }
    return Result.ok(userFound);
  }
}
