package com_maboroshi.spring.contexts.wishlist.errors;

public class CannotAddItem extends RuntimeException {
  public CannotAddItem(String message) {
    super(message);
  }
}
