package com_maboroshi.spring.contexts.cart.errors;

public class CannotRemoveItem extends RuntimeException {
  public CannotRemoveItem(String message) {
    super(message);
  }
}
