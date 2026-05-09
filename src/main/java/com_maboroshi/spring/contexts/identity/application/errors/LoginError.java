package com_maboroshi.spring.contexts.identity.application.errors;

public abstract class LoginError {
  private final String message;

  public LoginError(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }
}
