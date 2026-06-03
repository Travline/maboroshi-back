package com_maboroshi.spring.contexts.cart.services;

import com_maboroshi.spring.contexts.cart.errors.CannotRemoveItem;
import com_maboroshi.spring.contexts.cart.models.CartItem;
import com_maboroshi.spring.contexts.cart.persistence.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveCartItem {
  private final CartRepository repository;

  public CartItem execute(UUID userId, UUID productId) {
    return repository
        .deleteCartItemByUserId(userId, productId)
        .orElseThrow(() -> new CannotRemoveItem("Cannot remove item from wishlist or item does not exist"));
  }
}
