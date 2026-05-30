package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.entities.DetailedProduct;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductNotFound;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductsCannotBeRetrieved;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import org.springframework.stereotype.Repository;
import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import com_maboroshi.spring.shared.errors.RepositoryError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Repository
public class MockedProductRepository implements ProductRepository {

  private final List<DetailedProduct> products = new ArrayList<>();

  public void add(DetailedProduct product) {
    products.add(product);
  }

  public void clear() {
    products.clear();
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getLastProducts(int length) {
    BaseProduct[] result = products.stream()
        .limit(length)
        .map(this::toBase)
        .toArray(BaseProduct[]::new);
    return Result.ok(result);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getMostViewedProducts(int length) {
    return getLastProducts(length);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getDiscountedProducts(int length) {
    BaseProduct[] result = products.stream()
        .filter(p -> p.getSalePrice() != null && p.getSalePrice() < p.getRealPrice())
        .limit(length)
        .map(this::toBase)
        .toArray(BaseProduct[]::new);
    return Result.ok(result);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> searchProducts(String term) {
    BaseProduct[] result = products.stream()
        .filter(p -> p.getProductName().toLowerCase().contains(term.toLowerCase()))
        .map(this::toBase)
        .toArray(BaseProduct[]::new);
    return Result.ok(result);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getRecommendedProducts(String[] names) {
    if (names == null || names.length == 0) {
      return Result.fail(new ProductNotFound("Names list is empty"));
    }
    List<String> nameList = Arrays.asList(names);
    BaseProduct[] result = products.stream()
        .filter(p -> nameList.contains(p.getProductName()))
        .map(this::toBase)
        .toArray(BaseProduct[]::new);
    return Result.ok(result);
  }

  @Override
  public Result<DetailedProduct, RepositoryError> getProductDetail(String slug) {
    return products.stream()
        .filter(p -> p.getSlug().equals(slug))
        .findFirst()
        .<Result<DetailedProduct, RepositoryError>>map(Result::ok)
        .orElse(Result.fail(new ProductNotFound("Product not found: " + slug)));
  }

  @Override
  public Result<Artist[], RepositoryError> getArtists() {
    return Result.ok(new Artist[0]);
  }

  private BaseProduct toBase(DetailedProduct p) {
    return new BaseProduct(
        p.getId(),
        p.getProductName(),
        p.getArtist(),
        p.getRealPrice(),
        p.getSalePrice(),
        p.getStock(),
        p.getSlug(),
        p.getImages(),
        false,
        false);
  }
}