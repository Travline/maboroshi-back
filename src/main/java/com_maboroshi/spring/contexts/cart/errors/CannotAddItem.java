package com_maboroshi.spring.contexts.cart.errors;

public class CannotAddItem extends RuntimeException {
  public CannotAddItem(String message) {
    super(message);
  }
}
