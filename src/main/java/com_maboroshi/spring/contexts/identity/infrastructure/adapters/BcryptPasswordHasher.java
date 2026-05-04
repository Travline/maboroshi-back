package com_maboroshi.spring.contexts.identity.infrastructure.adapters;

import com_maboroshi.spring.contexts.identity.domain.ports.PasswordHasher;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.ServiceError;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptPasswordHasher implements PasswordHasher {
  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Override
  public Result<String, ServiceError> hash(String plainPassword) {
    String hashedPassword = encoder.encode(plainPassword);
    if (hashedPassword == null) {
      return Result.fail(new HashingError("Error trying to hash password"));
    }
    return Result.ok(hashedPassword);
  }

  @Override
  public Result<Boolean, ServiceError> compare(String plainPassword, String hashedPassword) {
    if (plainPassword == null) {
      return Result.fail(new HashingError("Error with password value to be compared"));
    }

    Boolean matches = encoder.matches(plainPassword, hashedPassword);
    return Result.ok(matches);
  }
}
