package com_maboroshi.spring.contexts.identity.domain.ports;

import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.ServiceError;

public interface PasswordHasher {
  public Result<String, ServiceError> hash(String pwd);

  public Result<Boolean, ServiceError> compare(String plain, String hash);
}
