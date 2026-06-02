package com_maboroshi.spring.contexts.wishlist.errors;

public class WishlistNotFound extends RuntimeException {
  public WishlistNotFound(String message) {
    super(message);
  }
}
