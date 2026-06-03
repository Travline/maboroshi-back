package com_maboroshi.spring.contexts.order.errors;

public class UserUnauthenticated extends RuntimeException {
  public UserUnauthenticated(String message) {
    super(message);
  }
}
