package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.GetProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.dtos.ProductCardResponse;
import com_maboroshi.spring.contexts.catalog.application.dtos.ProductCardMapper;
import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsNotFound;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;
import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class GetProductsUseCase {

  private final ProductRepository productRepository;
  private final AppLogger appLogger;

  public GetProductsUseCase(ProductRepository productRepository, AppLogger appLogger) {
    this.productRepository = productRepository;
    this.appLogger = appLogger;
  }

  public Result<ProductCardResponse[], CatalogError> execute(GetProductsRequest request) {
    String orderBy = request.orderBy() != null ? request.orderBy() : "date";
    int length = request.length() > 0 ? request.length() : 10;

    Result<BaseProduct[], ?> repositoryResult = switch (orderBy) {
      case "views" -> productRepository.getMostViewedProducts(length);
      case "discount" -> productRepository.getDiscountedProducts(length);
      default -> productRepository.getLastProducts(length);
    };

    if (!repositoryResult.isSucces) {
      appLogger.warn("GetProducts Error: could not retrieve products with order_by=" + orderBy);
      return Result.fail(new ProductsRetrievalFailed("Could not retrieve products"));
    }

    BaseProduct[] products = repositoryResult.getValue();

    if (products == null || products.length == 0) {
      appLogger.warn("GetProducts: no products found with order_by=" + orderBy);
      return Result.fail(new ProductsNotFound("No products found"));
    }

    appLogger.info("GetProducts: retrieved " + products.length + " products with order_by=" + orderBy);
    return Result.ok(ProductCardMapper.toResponseArray(products));
  }
}