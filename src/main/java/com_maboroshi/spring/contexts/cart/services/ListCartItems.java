package com_maboroshi.spring.contexts.cart.services;

import com_maboroshi.spring.contexts.cart.errors.CartNotFound;
import com_maboroshi.spring.contexts.cart.models.CartItem;
import com_maboroshi.spring.contexts.cart.persistence.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListCartItems {
  private final CartRepository repository;

  public List<CartItem> execute(UUID userId) {
    return repository
        .findAllCartItemsByUserId(userId)
        .orElseThrow(() -> new CartNotFound("Could not retrieve wishlist for user: " + userId));
  }
}
