package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.ProductCardMapper;
import com_maboroshi.spring.contexts.catalog.application.dtos.ProductCardResponse;
import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsNotFound;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;
import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class GetProductsByArtistUseCase {

  private final ProductRepository productRepository;
  private final AppLogger appLogger;

  public GetProductsByArtistUseCase(ProductRepository productRepository, AppLogger appLogger) {
    this.productRepository = productRepository;
    this.appLogger = appLogger;
  }

  public Result<ProductCardResponse[], CatalogError> execute(String name) {
    var result = productRepository.getProductsByArtistName(name);

    if (!result.isSucces) {
      appLogger.warn("GetProductsByArtist Error: could not retrieve products for artist: " + name);
      return Result.fail(new ProductsRetrievalFailed("Could not retrieve products for artist: " + name));
    }

    BaseProduct[] products = result.getValue();

    if (products == null || products.length == 0) {
      appLogger.warn("GetProductsByArtist: no products found for artist: " + name);
      return Result.fail(new ProductsNotFound("No products found for artist: " + name));
    }

    appLogger.info("GetProductsByArtist: retrieved " + products.length + " products for artist: " + name);
    return Result.ok(ProductCardMapper.toResponseArray(products));
  }
}
