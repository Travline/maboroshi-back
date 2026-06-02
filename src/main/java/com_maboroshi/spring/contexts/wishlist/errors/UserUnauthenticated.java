package com_maboroshi.spring.contexts.wishlist.errors;

public class UserUnauthenticated extends RuntimeException {
  public UserUnauthenticated(String message) {
    super(message);
  }
}
