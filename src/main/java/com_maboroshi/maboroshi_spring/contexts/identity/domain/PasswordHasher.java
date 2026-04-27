package com_maboroshi.maboroshi_spring.contexts.identity.domain;

import com_maboroshi.maboroshi_spring.shared.core.Result;

public interface PasswordHasher {
  public Result<String, ServiceError> hash(String pwd);

  public Result<Boolean, ServiceError> compare(String plain, String hash);
}
