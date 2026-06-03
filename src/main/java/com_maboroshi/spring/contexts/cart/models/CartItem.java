package com_maboroshi.spring.contexts.cart.models;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CartItem {
  private UUID productId;
  private String productName;
  private Double realPrice;
  private Double salePrice;
  private String slug;
  private Integer quantity;
}
