package com_maboroshi.spring.contexts.identity.application.errors;

public class InvalidCredentials extends LoginError {
  public InvalidCredentials(String message) {
    super(message);
  }
}
