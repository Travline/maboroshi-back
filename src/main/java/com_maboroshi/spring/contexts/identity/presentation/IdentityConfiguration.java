package com_maboroshi.spring.contexts.identity.presentation;

import com_maboroshi.spring.contexts.identity.application.use_cases.RegisterUserUseCase;
import com_maboroshi.spring.contexts.identity.domain.ports.PasswordHasher;
import com_maboroshi.spring.contexts.identity.domain.ports.UserRepository;
import com_maboroshi.spring.shared.utils.AppLogger;
import com_maboroshi.spring.shared.utils.UuidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityConfiguration {

  @Bean
  public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, UuidGenerator uuidGenerator, AppLogger appLogger) {
    return new RegisterUserUseCase(userRepository, passwordHasher, uuidGenerator, appLogger);
  }
}
