package com_maboroshi.spring.contexts.identity.presentation;

import com_maboroshi.spring.contexts.identity.application.errors.*;

public class AuthStatusMapper {
  public static int getStatus(RegisterError error) {
    return switch (error) {
      case PasswordTooWeak ptw -> 400;
      case UnprocessablePassword up -> 422;
      case InvalidMailFormat imf -> 400;
      case UserAlreadyExists uae -> 409;
      case CannotCreateUser ccu -> 500;
      default -> 500;
    };
  }
}
