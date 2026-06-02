package com_maboroshi.spring.contexts.wishlist.persistence;

import com_maboroshi.spring.contexts.wishlist.models.WishlistItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistRepository {
  public Optional<WishlistItem> saveWishlistItemByUserId(UUID userId, UUID productId);

  public Optional<List<WishlistItem>> findAllWishlistItemsByUserId(UUID userId);

  public Optional<WishlistItem> deleteWishlistItemByUserId(UUID userId, UUID productId);
}
