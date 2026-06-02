package com_maboroshi.spring.contexts.wishlist.models;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class WishlistItem {
  private UUID productId;
  private String productName;
  private Double realPrice;
  private Double salePrice;
  private String slug;
}
