package com_maboroshi.spring.contexts.cart.services;

import com_maboroshi.spring.contexts.cart.errors.CannotUpdateQuantity;
import com_maboroshi.spring.contexts.cart.models.CartItem;
import com_maboroshi.spring.contexts.cart.persistence.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCartItemQuantity {
  private final CartRepository repository;

  public CartItem execute(UUID userId, UUID productId, int quantity) {
    return repository.updateCartItemQuantityByUserId(userId, productId, quantity)
        .orElseThrow(() -> new CannotUpdateQuantity("Error updating quantity in the cart item"));
  }
}
