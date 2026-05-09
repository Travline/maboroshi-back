package com_maboroshi.spring.contexts.identity.application.dtos;

import com_maboroshi.spring.contexts.identity.domain.entities.User;

public class LoginUserMapper {
  public static LoginUserResponse toResponse(User user) {
    return new LoginUserResponse(
            user.getId().toString(),
            user.getUsername(),
            user.getMail().toString(),
            user.getPhone()
    );
  }
}
