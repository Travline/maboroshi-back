package com_maboroshi.spring.contexts.wishlist.errors;

public class CannotRemoveItem extends RuntimeException {
  public CannotRemoveItem(String message) {
    super(message);
  }
}
