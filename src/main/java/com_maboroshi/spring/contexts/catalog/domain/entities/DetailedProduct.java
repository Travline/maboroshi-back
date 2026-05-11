package com_maboroshi.spring.contexts.catalog.domain.entities;

import java.util.UUID;

public class DetailedProduct extends BaseProduct {
  private String[] tracklist;
  private String[] genres;

  public DetailedProduct(UUID id, String productName, String artist, double realPrice, double salePrice, int stock, String slug, String[] images, boolean inWishlist, boolean inCart, String[] tracklist, String[] genres) {
    super(id, productName, artist, realPrice, salePrice, stock, slug, images, inWishlist, inCart);
    this.tracklist = tracklist;
    this.genres = genres;
  }

  public String[] getTracklist() {
    return tracklist;
  }

  public String[] getGenres() {
    return genres;
  }
}
