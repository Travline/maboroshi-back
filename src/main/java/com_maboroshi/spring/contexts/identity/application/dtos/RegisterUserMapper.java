package com_maboroshi.spring.contexts.identity.application.dtos;

import com_maboroshi.spring.contexts.identity.domain.User;

public class RegisterUserMapper {
  public static RegisterUserResponse toResponse(User user) {
    return new RegisterUserResponse(
            user.getId().toString(),
            user.getUsername(),
            user.getMail().toString(),
            user.getPhone()
    );
  }
}
