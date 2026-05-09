package com_maboroshi.spring.contexts.identity.application.errors;

public class UserNotActive extends LoginError {
  public UserNotActive(String message) {
    super(message);
  }
}
