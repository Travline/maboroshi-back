package com_maboroshi.spring.contexts.cart.controllers;

import com_maboroshi.spring.contexts.cart.models.CartItem;
import com_maboroshi.spring.contexts.cart.services.AddToCart;
import com_maboroshi.spring.contexts.cart.services.ListCartItems;
import com_maboroshi.spring.contexts.cart.services.RemoveCartItem;
import com_maboroshi.spring.contexts.cart.services.UpdateCartItemQuantity;
import com_maboroshi.spring.contexts.wishlist.errors.UserUnauthenticated;
import com_maboroshi.spring.shared.utils.SessionCookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

  private final SessionCookie cookie;
  private final AddToCart addToCart;
  private final RemoveCartItem removeCartItem;
  private final ListCartItems listCartItems;
  private final UpdateCartItemQuantity updateCartItemQuantity;

  @PostMapping("/{id}")
  public ResponseEntity<CartItem> newCartProduct(
      @CookieValue(name = "maboroshi-token", required = false) String token,
      @PathVariable UUID id) {

    UUID userId = extractUserIdOrThrow(token);

    CartItem response = addToCart.execute(userId, id);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CartItem> updateQuantityCartProduct(
      @CookieValue(name = "maboroshi-token", required = false) String token,
      @PathVariable UUID id,
      @RequestParam int quantity) {

    UUID userId = extractUserIdOrThrow(token);

    CartItem response = updateCartItemQuantity.execute(userId, id, quantity);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CartItem> deleteWishlistProduct(
      @CookieValue(name = "maboroshi-token", required = false) String token,
      @PathVariable UUID id) {

    UUID userId = extractUserIdOrThrow(token);

    CartItem response = removeCartItem.execute(userId, id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<CartItem>> getWishlistProducts(
      @CookieValue(name = "maboroshi-token", required = false) String token) {

    UUID userId = extractUserIdOrThrow(token);

    List<CartItem> response = listCartItems.execute(userId);
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
