package com_maboroshi.spring.contexts.catalog.domain.ports;

import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.entities.DetailedProduct;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;

public interface ProductRepository {
  Result<BaseProduct[], RepositoryError> getLastProducts(int length);

  Result<BaseProduct[], RepositoryError> getMostViewedProducts(int length);

  Result<BaseProduct[], RepositoryError> getDiscountedProducts(int length);

  Result<DetailedProduct, RepositoryError> getProductDetail(String slug);

  Result<BaseProduct[], RepositoryError> searchProducts(String term);

  Result<BaseProduct[], RepositoryError> getRecommendedProducts(String[] names);
}