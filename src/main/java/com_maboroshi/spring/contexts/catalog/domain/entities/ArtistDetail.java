package com_maboroshi.spring.contexts.catalog.domain.entities;

public class ArtistDetail {

  private String artistId;
  private String name;
  private String image;
  private BaseProduct[] products;

  public ArtistDetail(
      String artistId,
      String name,
      String image,
      BaseProduct[] products
  ) {
    this.artistId = artistId;
    this.name = name;
    this.image = image;
    this.products = products;
  }

  public String getArtistId() {
    return artistId;
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public BaseProduct[] getProducts() {
    return products;
  }
}