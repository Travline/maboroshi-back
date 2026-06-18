package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.ArtistResponse;
import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsNotFound;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;
import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class GetArtistUseCase {

  private final ProductRepository productRepository;
  private final AppLogger appLogger;

  public GetArtistUseCase(ProductRepository productRepository, AppLogger appLogger) {
    this.productRepository = productRepository;
    this.appLogger = appLogger;
  }

  public Result<ArtistResponse, CatalogError> execute(String name) {
    var result = productRepository.getArtistByName(name);

    if (!result.isSucces) {
      appLogger.warn("GetArtist Error: artist not found: " + name);
      return Result.fail(new ProductsNotFound("Artist not found: " + name));
    }

    Artist artist = result.getValue();
    return Result.ok(new ArtistResponse(
        artist.getId().toString(),
        artist.getName(),
        artist.getImage()));
  }
}
