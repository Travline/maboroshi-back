package com_maboroshi.spring.contexts.identity.presentation;

import com_maboroshi.spring.contexts.identity.application.RegisterUserUseCase;
import com_maboroshi.spring.contexts.identity.domain.PasswordHasher;
import com_maboroshi.spring.contexts.identity.domain.UserIdGenerator;
import com_maboroshi.spring.contexts.identity.domain.UserRepository;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityConfiguration {

  @Bean
  public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, UserIdGenerator userIdGenerator, AppLogger appLogger) {
    return new RegisterUserUseCase(userRepository, passwordHasher, userIdGenerator, appLogger);
  }
}
