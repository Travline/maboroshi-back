package com_maboroshi.spring.contexts.identity.application.use_cases;

import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserRequest;
import com_maboroshi.spring.contexts.identity.application.errors.*;
import com_maboroshi.spring.contexts.identity.domain.entities.User;
import com_maboroshi.spring.contexts.identity.domain.entities.UserMail;
import com_maboroshi.spring.contexts.identity.domain.errors.InvalidMailException;
import com_maboroshi.spring.contexts.identity.domain.ports.PasswordHasher;
import com_maboroshi.spring.contexts.identity.domain.ports.UserRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;
import com_maboroshi.spring.shared.utils.UuidGenerator;

public class RegisterUserUseCase {
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final UuidGenerator uuidGenerator;
  private final AppLogger appLogger;

  public RegisterUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, UuidGenerator uuidGenerator, AppLogger appLogger) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.uuidGenerator = uuidGenerator;
    this.appLogger = appLogger;
  }

  public Result<User, RegisterError> execute(RegisterUserRequest userData) {
    UserMail mailValidated;
    try {
      mailValidated = new UserMail(userData.mail());
    } catch (InvalidMailException ime) {
      appLogger.warn("Register Error: Mail '" + userData.mail() + "' is invalid");
      return Result.fail(new InvalidMailFormat("Mail format is invalid"));
    }

    if (userData.pwd() == null || userData.pwd().trim().length() < 8) {
      appLogger.warn("Register Error: Password too short");
      return Result.fail(new PasswordTooWeak("Password too short"));
    }

    var hashResult = passwordHasher.hash(userData.pwd());
    if (!hashResult.isSucces) {
      appLogger.warn("Register Error: Password value is invalid");
      return Result.fail(new UnprocessablePassword("Password value is invalid"));
    }

    var userSearch = userRepository.findByMail(mailValidated);
    if (userSearch.isSucces) {
      appLogger.warn("Register Error: User with mail '" + mailValidated.toString() + "' already exists");
      return Result.fail(new UserAlreadyExists("User already exists"));
    }

    User newUser = new User(
            uuidGenerator.generateId(),
            userData.username(),
            mailValidated,
            hashResult.getValue(),
            userData.phone(),
            true
    );

    var saveResult = userRepository.save(newUser);
    if (!saveResult.isSucces) {
      // Queda a espera de obtener como valor de resultado erróneo una excepción
      appLogger.error("Repository Error: User cannot be created", (Throwable) saveResult.getErrorValue());
      return Result.fail(new CannotCreateUser("User cannot be created"));
    }

    appLogger.info("User " + newUser.getUsername() + " registered successfully");
    return Result.ok(newUser);
  }
}
