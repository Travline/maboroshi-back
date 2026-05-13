package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.ProductCardResponse;
import com_maboroshi.spring.contexts.catalog.application.dtos.ProductCardMapper;
import com_maboroshi.spring.contexts.catalog.application.dtos.SearchProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsNotFound;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;
import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class SearchProductsUseCase {

  private final ProductRepository productRepository;
  private final AppLogger appLogger;

  public SearchProductsUseCase(ProductRepository productRepository, AppLogger appLogger) {
    this.productRepository = productRepository;
    this.appLogger = appLogger;
  }

  public Result<ProductCardResponse[], CatalogError> execute(SearchProductsRequest request) {
    if (request.term() == null || request.term().trim().isEmpty()) {
      appLogger.warn("SearchProducts Error: empty search term");
      return Result.fail(new ProductsNotFound("Search term cannot be empty"));
    }

    var repositoryResult = productRepository.searchProducts(request.term().trim());

    if (!repositoryResult.isSucces) {
      appLogger.warn("SearchProducts Error: could not search products with term=" + request.term());
      return Result.fail(new ProductsRetrievalFailed("Could not search products"));
    }

    BaseProduct[] products = repositoryResult.getValue();

    if (products == null || products.length == 0) {
      appLogger.info("SearchProducts: no products found for term=" + request.term());
      return Result.fail(new ProductsNotFound("No products found for: " + request.term()));
    }

    appLogger.info("SearchProducts: found " + products.length + " products for term=" + request.term());
    return Result.ok(ProductCardMapper.toResponseArray(products));
  }
}