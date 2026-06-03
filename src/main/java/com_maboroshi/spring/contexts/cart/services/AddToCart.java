package com_maboroshi.spring.contexts.cart.services;

import com_maboroshi.spring.contexts.cart.errors.CannotAddItem;
import com_maboroshi.spring.contexts.cart.models.CartItem;
import com_maboroshi.spring.contexts.cart.persistence.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddToCart {
  private final CartRepository repository;

  public CartItem execute(UUID userId, UUID productId) {
    return repository
        .saveCartItemByUserId(userId, productId)
        .orElseThrow(() -> new CannotAddItem("Cannot add item to cart"));
  }
}
