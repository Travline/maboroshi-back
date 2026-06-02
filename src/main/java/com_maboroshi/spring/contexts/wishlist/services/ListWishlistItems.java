package com_maboroshi.spring.contexts.wishlist.services;

import com_maboroshi.spring.contexts.wishlist.errors.WishlistNotFound;
import com_maboroshi.spring.contexts.wishlist.models.WishlistItem;
import com_maboroshi.spring.contexts.wishlist.persistence.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListWishlistItems {
  private final WishlistRepository repository;

  public List<WishlistItem> execute(UUID userId) {
    return repository
        .findAllWishlistItemsByUserId(userId)
        .orElseThrow(() -> new WishlistNotFound("Could not retrieve wishlist for user: " + userId));
  }
}
