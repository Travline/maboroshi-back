package com_maboroshi.spring.contexts.catalog.application.errors;

public abstract class CatalogError {
  private final String message;

  public CatalogError(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }
}