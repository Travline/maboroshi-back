package com_maboroshi.maboroshi_spring.contexts.identity.application;

import com_maboroshi.maboroshi_spring.contexts.identity.domain.*;
import com_maboroshi.maboroshi_spring.shared.core.Result;

public class RegisterUserUseCase {
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final UserIdGenerator userIdGenerator;

  public RegisterUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, UserIdGenerator userIdGenerator) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.userIdGenerator = userIdGenerator;
  }

  public Result<User, RegisterError> execute(RegisterUserRequest userData) {
    if (userData.pwd().trim().length() < 8) {
      return Result.fail(new PasswordTooWeak("Password too short"));
    }

    UserMail mailValidated;
    try {
      mailValidated = new UserMail(userData.mail());
    } catch (InvalidMailException ime) {
      return Result.fail(new InvalidMailFormat("Mail format is invalid"));
    }

    var hashResult = passwordHasher.hash(userData.pwd());
    if (!hashResult.isSucces) {
      return Result.fail(new UnprocessablePassword("Password Value is invalid"));
    }

    var userSearch = userRepository.findByMail(mailValidated);
    if (userSearch.isSucces) {
      return Result.fail(new UserAlreadyExists("User already exists"));
    }

    User newUser = new User(
            userIdGenerator.generateId(),
            userData.username(),
            mailValidated,
            hashResult.getValue(),
            userData.phone(),
            true
    );

    var saveResult = userRepository.save(newUser);
    if (!saveResult.isSucces) {
      return Result.fail(new CannotCreateUser("User cannot be created"));
    }

    return Result.ok(newUser);
  }
}
