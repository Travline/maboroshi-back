package com_maboroshi.spring.contexts.catalog.application.errors;

public class ProductsNotFound extends CatalogError {
  public ProductsNotFound(String message) {
    super(message);
  }
}