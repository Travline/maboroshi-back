package com_maboroshi.spring.contexts.identity.application.use_cases;

import com_maboroshi.spring.contexts.identity.application.dtos.LoginUserRequest;
import com_maboroshi.spring.contexts.identity.application.errors.*;
import com_maboroshi.spring.contexts.identity.domain.entities.User;
import com_maboroshi.spring.contexts.identity.domain.entities.UserMail;
import com_maboroshi.spring.contexts.identity.domain.errors.InvalidMailException;
import com_maboroshi.spring.contexts.identity.domain.ports.PasswordHasher;
import com_maboroshi.spring.contexts.identity.domain.ports.UserRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class LoginUserUseCase {
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final AppLogger appLogger;

  public LoginUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, AppLogger appLogger) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.appLogger = appLogger;
  }

  public Result<User, LoginError> execute(LoginUserRequest credentials) {
    UserMail mailValidated;
    try {
      mailValidated = new UserMail(credentials.mail());
    } catch (InvalidMailException ime) {
      appLogger.warn("Login Error: Mail '" + credentials.mail() + "' is invalid");
      return Result.fail(new InvalidCredentials("Invalid credentials"));
    }

    if (credentials.pwd() == null || credentials.pwd().trim().isEmpty()) {
      appLogger.warn("Login Error: Password is empty");
      return Result.fail(new InvalidCredentials("Invalid credentials"));
    }

    var userSearch = userRepository.findByMail(mailValidated);
    if (!userSearch.isSucces) {
      appLogger.warn("Login Error: User with mail '" + mailValidated.toString() + "' not found");
      return Result.fail(new InvalidCredentials("Invalid credentials"));
    }

    User user = userSearch.getValue();
    if (!user.isActive()) {
      appLogger.warn("Login Error: User with mail '" + mailValidated.toString() + "' is not active");
      return Result.fail(new UserNotActive("User account is not active"));
    }

    var compareResult = passwordHasher.compare(credentials.pwd(), user.getPwd());
    if (!compareResult.isSucces || !compareResult.getValue()) {
      appLogger.warn("Login Error: Invalid password for user '" + mailValidated.toString() + "'");
      return Result.fail(new InvalidCredentials("Invalid credentials"));
    }

    appLogger.info("User " + user.getUsername() + " logged in successfully");
    return Result.ok(user);
  }
}
