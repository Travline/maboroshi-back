package com_maboroshi.spring.contexts.catalog.application.errors;

public class ProductsRetrievalFailed extends CatalogError {
  public ProductsRetrievalFailed(String message) {
    super(message);
  }
}