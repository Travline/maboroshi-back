package com_maboroshi.spring.contexts.wishlist.services;

import com_maboroshi.spring.contexts.wishlist.errors.CannotAddItem;
import com_maboroshi.spring.contexts.wishlist.models.WishlistItem;
import com_maboroshi.spring.contexts.wishlist.persistence.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddToWishlist {
  private final WishlistRepository repository;

  public WishlistItem execute(UUID userId, UUID productId) {
    return repository
        .saveWishlistItemByUserId(userId, productId)
        .orElseThrow(() -> new CannotAddItem("Cannot add item to wishlist"));
  }
}
