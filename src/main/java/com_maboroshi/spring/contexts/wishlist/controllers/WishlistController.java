package com_maboroshi.spring.contexts.wishlist.controllers;

import com_maboroshi.spring.contexts.wishlist.errors.UserUnauthenticated;
import com_maboroshi.spring.contexts.wishlist.models.WishlistItem;
import com_maboroshi.spring.contexts.wishlist.services.AddToWishlist;
import com_maboroshi.spring.contexts.wishlist.services.ListWishlistItems;
import com_maboroshi.spring.contexts.wishlist.services.RemoveWishlistItem;
import com_maboroshi.spring.shared.utils.SessionCookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

  private final SessionCookie cookie;
  private final AddToWishlist addToWishlist;
  private final RemoveWishlistItem removeWishlistItem;
  private final ListWishlistItems listWishlistItems;

  @PostMapping("/{id}")
  public ResponseEntity<WishlistItem> newWishlistProduct(
      @CookieValue(name = "maboroshi-token") String token,
      @PathVariable UUID id) {

    UUID userId = extractUserIdOrThrow(token);

    WishlistItem response = addToWishlist.execute(userId, id);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<WishlistItem> deleteWishlistProduct(
      @CookieValue(name = "maboroshi-token") String token,
      @PathVariable UUID id) {

    UUID userId = extractUserIdOrThrow(token);

    WishlistItem response = removeWishlistItem.execute(userId, id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<WishlistItem>> getWishlistProducts(
      @CookieValue(name = "maboroshi-token") String token) {

    UUID userId = extractUserIdOrThrow(token);

    List<WishlistItem> response = listWishlistItems.execute(userId);
    return ResponseEntity.ok(response);
  }

  private UUID extractUserIdOrThrow(String token) {
    var cookieRes = cookie.getUserIdFromSessionCookie(token);
    if (!cookieRes.isSucces) {
      throw new UserUnauthenticated("Session cookie expired or invalid");
    }
    return cookieRes.getValue();
  }
}
