package com_maboroshi.spring.contexts.catalog.domain.entities;

import java.util.UUID;

public class BaseProduct {
  private UUID id;
  private String productName;
  private String artist;
  private String artistImage;
  private double realPrice;
  private Double salePrice;
  private int stock;
  private String slug;
  private String[] images;
  private boolean inWishlist;
  private boolean inCart;
  private String type;
  private String status;

  public BaseProduct(UUID id, String productName, String artist, String artistImage, double realPrice, Double salePrice, int stock, String slug, String[] images, boolean inWishlist, boolean inCart, String type, String status) {
    this.id = id;
    this.productName = productName;
    this.artist = artist;
    this.artistImage = artistImage;
    this.realPrice = realPrice;
    this.salePrice = salePrice;
    this.stock = stock;
    this.slug = slug;
    this.images = images;
    this.inWishlist = inWishlist;
    this.inCart = inCart;
    this.type = type;
    this.status = status;
  }

  public UUID getId() { return id; }
  public String getProductName() { return productName; }
  public String getArtist() { return artist; }
  public String getArtistImage() { return artistImage; }
  public double getRealPrice() { return realPrice; }
  public Double getSalePrice() { return salePrice; }
  public int getStock() { return stock; }
  public String getSlug() { return slug; }
  public String[] getImages() { return images; }
  public boolean isInWishlist() { return inWishlist; }
  public boolean isInCart() { return inCart; }
  public String getType() { return type; }
  public String getStatus() { return status; }
}
