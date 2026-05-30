package com_maboroshi.spring.contexts.catalog.application.dtos;

import com_maboroshi.spring.contexts.catalog.domain.entities.ArtistDetail;

public class ArtistDetailMapper {

  public static ArtistDetailResponse toResponse(
      ArtistDetail artist
  ) {

    return new ArtistDetailResponse(
        artist.getArtistId(),
        artist.getName(),
        artist.getImage(),
        ProductCardMapper.toResponseArray(
            artist.getProducts()
        )
    );
  }
}