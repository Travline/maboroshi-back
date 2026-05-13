package com_maboroshi.spring.contexts.catalog.presentation;

import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsNotFound;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;

public class CatalogStatusMapper {
  public static int getStatus(CatalogError error) {
    return switch (error) {
      case ProductsNotFound pnf -> 404;
      case ProductsRetrievalFailed prf -> 500;
      default -> 500;
    };
  }
}
