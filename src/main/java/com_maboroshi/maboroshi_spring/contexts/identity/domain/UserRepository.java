package com_maboroshi.maboroshi_spring.contexts.identity.domain;

import com_maboroshi.maboroshi_spring.shared.core.Result;

public interface UserRepository {
  public Result<User, UserDataError> save(User user);

  public Result<User, UserDataError> findById(String id);

  public Result<User, UserDataError> findByMail(UserMail mail);
}
