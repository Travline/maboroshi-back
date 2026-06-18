package com_maboroshi.spring.contexts.catalog.application.dtos;

import java.util.List;

public record ProductCardResponse(
        String productId,
        String productName,
        String artist,
        String artistImage,
        double realPrice,
        double salePrice,
        double discount,
        String slug,
        List<String> images,
        boolean inWishlist,
        boolean inCart,
        String type,
        String status,
        String spotifyId) {
}
