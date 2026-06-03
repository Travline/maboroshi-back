package com_maboroshi.spring.contexts.cart.errors;

public class UserUnauthenticated extends RuntimeException {
  public UserUnauthenticated(String message) {
    super(message);
  }
}
