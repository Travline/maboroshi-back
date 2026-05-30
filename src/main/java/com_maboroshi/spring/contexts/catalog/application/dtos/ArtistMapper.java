package com_maboroshi.spring.contexts.catalog.application.dtos;

import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;

public class ArtistMapper {

  public static ArtistResponse toResponse(Artist artist) {
    return new ArtistResponse(
        artist.getId().toString(),
        artist.getName(),
        artist.getImage()
    );
  }

  public static ArtistResponse[] toResponseArray(Artist[] artists) {
    ArtistResponse[] responses = new ArtistResponse[artists.length];

    for (int i = 0; i < artists.length; i++) {
      responses[i] = toResponse(artists[i]);
    }

    return responses;
  }
}