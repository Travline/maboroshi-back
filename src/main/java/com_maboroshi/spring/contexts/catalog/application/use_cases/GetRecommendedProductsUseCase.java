package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.GetRecommendedProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.dtos.ProductCardResponse;
import com_maboroshi.spring.contexts.catalog.application.dtos.ProductCardMapper;
import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsNotFound;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;
import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class GetRecommendedProductsUseCase {

  private final ProductRepository productRepository;
  private final AppLogger appLogger;

  public GetRecommendedProductsUseCase(ProductRepository productRepository, AppLogger appLogger) {
    this.productRepository = productRepository;
    this.appLogger = appLogger;
  }

  public Result<ProductCardResponse[], CatalogError> execute(GetRecommendedProductsRequest request) {
    if (request.names() == null || request.names().length == 0) {
      appLogger.warn("GetRecommended Error: empty names list");
      return Result.fail(new ProductsNotFound("Names list cannot be empty"));
    }

    var repositoryResult = productRepository.getRecommendedProducts(request.names());

    if (!repositoryResult.isSucces) {
      appLogger.warn("GetRecommended Error: could not retrieve recommended products");
      return Result.fail(new ProductsRetrievalFailed("Could not retrieve recommended products"));
    }

    BaseProduct[] products = repositoryResult.getValue();

    if (products == null || products.length == 0) {
      appLogger.info("GetRecommended: no products found for given names");
      return Result.fail(new ProductsNotFound("No recommended products found"));
    }

    appLogger.info("GetRecommended: retrieved " + products.length + " recommended products");
    return Result.ok(ProductCardMapper.toResponseArray(products));
  }
}