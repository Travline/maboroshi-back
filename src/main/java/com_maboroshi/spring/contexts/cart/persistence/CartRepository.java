package com_maboroshi.spring.contexts.cart.persistence;

import com_maboroshi.spring.contexts.cart.models.CartItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
  public Optional<CartItem> saveCartItemByUserId(UUID userId, UUID productId);

  public Optional<List<CartItem>> findAllCartItemsByUserId(UUID userId);

  public Optional<CartItem> deleteCartItemByUserId(UUID userId, UUID productId);

  public Optional<CartItem> updateCartItemQuantityByUserId(UUID userId, UUID productId, int quantity);
}
