package com_maboroshi.spring.contexts.identity.domain.ports;

import com_maboroshi.spring.contexts.identity.domain.entities.User;
import com_maboroshi.spring.contexts.identity.domain.entities.UserMail;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;

import java.util.UUID;

public interface UserRepository {
  public Result<User, RepositoryError> save(User newUser);

  public Result<User, RepositoryError> findByMail(UserMail userMail);
  
  public Result<User, RepositoryError> findById(UUID userId);
}
