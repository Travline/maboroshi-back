package com_maboroshi.spring.contexts.catalog.presentation;

import com_maboroshi.spring.contexts.catalog.application.dtos.GetProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.dtos.GetRecommendedProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.dtos.SearchProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.use_cases.*;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/catalog")
public class CatalogController {

  private static final int DEFAULT_LENGTH = 50;

  private final GetProductsUseCase getProductsUseCase;
  private final SearchProductsUseCase searchProductsUseCase;
  private final GetRecommendedProductsUseCase getRecommendedProductsUseCase;
  private final GetArtistsUseCase getArtistsUseCase;
  private final GetArtistUseCase getArtistUseCase;
  private final GetProductsByArtistUseCase getProductsByArtistUseCase;
  private final ProductRepository productRepository;
  private final GetGenreFilteredDataUseCase getGenreFilteredDataUseCase;


  public CatalogController(
      GetProductsUseCase getProductsUseCase,
      SearchProductsUseCase searchProductsUseCase,
      GetRecommendedProductsUseCase getRecommendedProductsUseCase,
      GetArtistsUseCase getArtistsUseCase,
      GetArtistUseCase getArtistUseCase,
      GetProductsByArtistUseCase getProductsByArtistUseCase,
      ProductRepository productRepository,
      GetGenreFilteredDataUseCase getGenreFilteredDataUseCase) {
    this.getProductsUseCase = getProductsUseCase;
    this.searchProductsUseCase = searchProductsUseCase;
    this.getRecommendedProductsUseCase = getRecommendedProductsUseCase;
    this.getArtistsUseCase = getArtistsUseCase;
    this.getArtistUseCase = getArtistUseCase;
    this.getProductsByArtistUseCase = getProductsByArtistUseCase;
    this.productRepository = productRepository;
    this.getGenreFilteredDataUseCase = getGenreFilteredDataUseCase;
  }

  @GetMapping("/products")
  public ResponseEntity<?> getProducts(
      @RequestParam(name = "order_by", defaultValue = "date") String orderBy,
      @RequestParam(name = "type", required = false) String type,
      @RequestParam(name = "status", required = false) String status) {
    return getProductsUseCase.execute(new GetProductsRequest(orderBy, DEFAULT_LENGTH, type, status)).match(
        products -> ResponseEntity.ok(products),
        error -> ResponseEntity.status(CatalogStatusMapper.getStatus(error)).body(error.message()));
  }

  @GetMapping("/products/{slug}")
  public ResponseEntity<?> getProductDetail(@PathVariable String slug) {
    return productRepository.getProductDetail(slug).match(
        product -> ResponseEntity.ok(product),
        error -> ResponseEntity.status(404).body(error.message()));
  }

  @GetMapping("/products/search")
  public ResponseEntity<?> searchProducts(@RequestParam String term) {
    return searchProductsUseCase.execute(new SearchProductsRequest(term)).match(
        products -> ResponseEntity.ok(products),
        error -> ResponseEntity.status(CatalogStatusMapper.getStatus(error)).body(error.message()));
  }

  @PostMapping("/products/recommended")
  public ResponseEntity<?> getRecommendedProducts(@RequestBody List<String> names) {
    return getRecommendedProductsUseCase.execute(
        new GetRecommendedProductsRequest(names.toArray(new String[0]))).match(
            products -> ResponseEntity.ok(products),
            error -> ResponseEntity.status(CatalogStatusMapper.getStatus(error)).body(error.message()));
  }

  @GetMapping("/artists")
  public ResponseEntity<?> getArtists() {
    return getArtistsUseCase.execute().match(
        artists -> ResponseEntity.ok(artists),
        error -> ResponseEntity.status(CatalogStatusMapper.getStatus(error)).body(error.message()));
  }

  @GetMapping("/artists/{name}")
  public ResponseEntity<?> getArtist(@PathVariable String name) {
    return getArtistUseCase.execute(name).match(
        artist -> ResponseEntity.ok(artist),
        error -> ResponseEntity.status(CatalogStatusMapper.getStatus(error)).body(error.message()));
  }

  @GetMapping("/artists/{name}/products")
  public ResponseEntity<?> getProductsByArtist(@PathVariable String name) {
    return getProductsByArtistUseCase.execute(name).match(
        products -> ResponseEntity.ok(products),
        error -> ResponseEntity.status(CatalogStatusMapper.getStatus(error)).body(error.message()));
  }
  @GetMapping("/genres/{genreName}")
public ResponseEntity<?> getGenreFilteredData(@PathVariable String genreName) {
  return getGenreFilteredDataUseCase.execute(genreName).match(
      data -> ResponseEntity.ok(data),
      error -> ResponseEntity.status(CatalogStatusMapper.getStatus(error)).body(error.message())
  );
}
}