package com_maboroshi.spring.contexts.wishlist.services;

import com_maboroshi.spring.contexts.wishlist.errors.CannotRemoveItem;
import com_maboroshi.spring.contexts.wishlist.models.WishlistItem;
import com_maboroshi.spring.contexts.wishlist.persistence.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveWishlistItem {
  private final WishlistRepository repository;

  public WishlistItem execute(UUID userId, UUID productId) {
    return repository
        .deleteWishlistItemByUserId(userId, productId)
        .orElseThrow(() -> new CannotRemoveItem("Cannot remove item from wishlist or item does not exist"));
  }
}
