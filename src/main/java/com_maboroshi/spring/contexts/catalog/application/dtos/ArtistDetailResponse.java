package com_maboroshi.spring.contexts.catalog.application.dtos;

public record ArtistDetailResponse(
    String artistId,
    String name,
    String image,
    ProductCardResponse[] products
) {
}