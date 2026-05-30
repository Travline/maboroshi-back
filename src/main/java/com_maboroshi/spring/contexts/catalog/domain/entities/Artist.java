package com_maboroshi.spring.contexts.catalog.domain.entities;

import java.util.UUID;

public class Artist {

  private UUID id;
  private String name;
  private String image;

  public Artist(UUID id, String name, String image) {
    this.id = id;
    this.name = name;
    this.image = image;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }
}