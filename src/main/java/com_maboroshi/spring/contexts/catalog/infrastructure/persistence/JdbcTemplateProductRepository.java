package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.entities.DetailedProduct;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JdbcTemplateProductRepository implements ProductRepository {

  private final ProductListQueries listQueries;
  private final ProductSearchQueries searchQueries;
  private final ProductDetailQueries detailQueries;

  public JdbcTemplateProductRepository(
      ProductListQueries listQueries,
      ProductSearchQueries searchQueries,
      ProductDetailQueries detailQueries
  ) {
    this.listQueries = listQueries;
    this.searchQueries = searchQueries;
    this.detailQueries = detailQueries;
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getLastProducts(int length) {
    return listQueries.getLastProducts(length);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getMostViewedProducts(int length) {
    return listQueries.getMostViewedProducts(length);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getDiscountedProducts(int length) {
    return listQueries.getDiscountedProducts(length);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> searchProducts(String term) {
    return searchQueries.searchProducts(term);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getRecommendedProducts(String[] names) {
    return searchQueries.getRecommendedProducts(names);
  }

  @Override
  public Result<DetailedProduct, RepositoryError> getProductDetail(String slug) {
    return detailQueries.getProductDetail(slug);
  }
}