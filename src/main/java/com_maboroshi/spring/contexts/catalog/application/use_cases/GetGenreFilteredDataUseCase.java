package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.GenreFilteredResponse;
import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import org.springframework.stereotype.Service;

public class GetGenreFilteredDataUseCase {
  private final ProductRepository productRepository;

  public GetGenreFilteredDataUseCase(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Result<GenreFilteredResponse, CatalogError> execute(String genreName) {
    var productsResult = productRepository.getProductsByGenre(genreName);
    if (!productsResult.isSucces) {
      return Result.fail(new ProductsRetrievalFailed("Error al obtener los productos del género"));
    }

    var artistsResult = productRepository.getArtistsByGenre(genreName);
    if (!artistsResult.isSucces) {
      return Result.fail(new ProductsRetrievalFailed("Error al obtener los artistas del género"));
    }

    return Result.ok(new GenreFilteredResponse(
        productsResult.getValue(),
        artistsResult.getValue()
    ));
  }
}
