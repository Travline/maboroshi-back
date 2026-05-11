package com_maboroshi.spring.contexts.catalog.domain.ports;

import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.entities.DetailedProduct;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;

public interface ProductRepository {
  public Result<BaseProduct[], RepositoryError> getLastProducts(int length);

  public Result<BaseProduct[], RepositoryError> getMostViewedProducts(int length);

  public Result<BaseProduct[], RepositoryError> getDiscountedProducts(int length);

  public Result<DetailedProduct, RepositoryError> getProductDetail(String slug);
}
