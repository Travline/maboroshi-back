package com_maboroshi.spring.shared.errors;

public class InvalidTokenException extends Exception {
  public InvalidTokenException(String message) {
    super(message);
  }
}
