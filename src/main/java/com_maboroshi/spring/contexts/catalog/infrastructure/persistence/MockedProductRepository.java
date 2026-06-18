package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.domain.entities.DetailedProduct;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductNotFound;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class MockedProductRepository implements ProductRepository {

  private final List<DetailedProduct> products = new ArrayList<>();
  private final List<Artist> artists = new ArrayList<>();

  public void add(DetailedProduct product) {
    products.add(product);
  }

  public void addArtist(Artist artist) {
    artists.add(artist);
  }

  public void clear() {
    products.clear();
    artists.clear();
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getLastProducts(int length) {
    return Result.ok(products.stream().limit(length).map(this::toBase).toArray(BaseProduct[]::new));
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getMostViewedProducts(int length) {
    return getLastProducts(length);
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getDiscountedProducts(int length) {
    return Result.ok(products.stream()
        .filter(p -> p.getSalePrice() != null && p.getSalePrice() > 0 && p.getSalePrice() < p.getRealPrice())
        .limit(length).map(this::toBase).toArray(BaseProduct[]::new));
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getProductsByType(String type, int length) {
    return Result.ok(products.stream()
        .filter(p -> type.equals(p.getType()))
        .limit(length).map(this::toBase).toArray(BaseProduct[]::new));
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getProductsByStatus(String status, int length) {
    return Result.ok(products.stream()
        .filter(p -> status.equals(p.getStatus()))
        .limit(length).map(this::toBase).toArray(BaseProduct[]::new));
  }

  @Override
  public Result<BaseProduct[], RepositoryError> searchProducts(String term) {
    return Result.ok(products.stream()
        .filter(p -> p.getProductName().toLowerCase().contains(term.toLowerCase()))
        .map(this::toBase).toArray(BaseProduct[]::new));
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getRecommendedProducts(String[] names) {
    if (names == null || names.length == 0) {
      return Result.fail(new ProductNotFound("Names list is empty"));
    }
    List<String> nameList = Arrays.asList(names);
    return Result.ok(products.stream()
        .filter(p -> nameList.contains(p.getProductName()))
        .map(this::toBase).toArray(BaseProduct[]::new));
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
  public Result<Artist[], RepositoryError> getAllArtists() {
    return Result.ok(artists.toArray(new Artist[0]));
  }

  @Override
  public Result<Artist, RepositoryError> getArtistByName(String name) {
    return artists.stream()
        .filter(a -> a.getName().equals(name))
        .findFirst()
        .<Result<Artist, RepositoryError>>map(Result::ok)
        .orElse(Result.fail(new ProductNotFound("Artist not found: " + name)));
  }

  @Override
  public Result<BaseProduct[], RepositoryError> getProductsByArtistName(String name) {
    return Result.ok(products.stream()
        .filter(p -> p.getArtist().equalsIgnoreCase(name))
        .map(this::toBase).toArray(BaseProduct[]::new));
  }

  private BaseProduct toBase(DetailedProduct p) {
    return new BaseProduct(
        p.getId(), p.getProductName(), p.getArtist(), p.getArtistImage(),
        p.getRealPrice(), p.getSalePrice(), p.getStock(), p.getSlug(),
        p.getImages(), false, false, p.getType(), p.getStatus(), p.getSpotifyId());
  }
}
