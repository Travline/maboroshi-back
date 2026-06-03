package com_maboroshi.spring.contexts.order.models;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Getter
public class OrderItem {
  private UUID productId;
  private String productName;
  private Double realPrice;
  private Double salePrice;
  private String slug;
  private Integer quantity;
  private OffsetDateTime date;
}
