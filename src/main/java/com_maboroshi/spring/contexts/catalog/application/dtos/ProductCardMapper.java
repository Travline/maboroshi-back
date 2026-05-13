package com_maboroshi.spring.contexts.catalog.application.dtos;

import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;

import java.util.Arrays;

public class ProductCardMapper {

  public static ProductCardResponse toResponse(BaseProduct product) {
    double discount = 0;
    if (product.getSalePrice() > 0 && product.getRealPrice() > 0) {
      discount = Math.round(((product.getRealPrice() - product.getSalePrice()) / product.getRealPrice()) * 100.0);
    }

    return new ProductCardResponse(
        product.getId().toString(),
        product.getProductName(),
        product.getArtist(),
        product.getRealPrice(),
        product.getSalePrice(),
        discount,
        product.getSlug(),
        Arrays.asList(product.getImages()),
        product.isInWishlist(),
        product.isInCart()
    );
  }

  public static ProductCardResponse[] toResponseArray(BaseProduct[] products) {
    ProductCardResponse[] responses = new ProductCardResponse[products.length];
    for (int i = 0; i < products.length; i++) {
      responses[i] = toResponse(products[i]);
    }
    return responses;
  }
}