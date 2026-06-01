package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.ArtistResponse;
import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsNotFound;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;
import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class GetArtistsUseCase {

  private final ProductRepository productRepository;
  private final AppLogger appLogger;

  public GetArtistsUseCase(ProductRepository productRepository, AppLogger appLogger) {
    this.productRepository = productRepository;
    this.appLogger = appLogger;
  }

  public Result<ArtistResponse[], CatalogError> execute() {
    var result = productRepository.getAllArtists();

    if (!result.isSucces) {
      appLogger.warn("GetArtists Error: could not retrieve artists");
      return Result.fail(new ProductsRetrievalFailed("Could not retrieve artists"));
    }

    Artist[] artists = result.getValue();

    if (artists == null || artists.length == 0) {
      appLogger.warn("GetArtists: no artists found");
      return Result.fail(new ProductsNotFound("No artists found"));
    }

    ArtistResponse[] responses = new ArtistResponse[artists.length];
    for (int i = 0; i < artists.length; i++) {
      responses[i] = new ArtistResponse(
          artists[i].getId().toString(),
          artists[i].getName(),
          artists[i].getImage()
      );
    }

    appLogger.info("GetArtists: retrieved " + artists.length + " artists");
    return Result.ok(responses);
  }
}
