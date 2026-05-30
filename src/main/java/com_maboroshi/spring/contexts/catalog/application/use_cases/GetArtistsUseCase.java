package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.ArtistMapper;
import com_maboroshi.spring.contexts.catalog.application.dtos.ArtistResponse;
import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class GetArtistsUseCase {

  private final ProductRepository productRepository;
  private final AppLogger appLogger;

  public GetArtistsUseCase(
      ProductRepository productRepository,
      AppLogger appLogger
  ) {
    this.productRepository = productRepository;
    this.appLogger = appLogger;
  }

  public Result<ArtistResponse[], String> execute() {

    Result<Artist[], ?> repositoryResult =
        productRepository.getArtists();

    if (!repositoryResult.isSucces) {
      appLogger.warn("GetArtists Error");
      return Result.fail("Could not retrieve artists");
    }

    return Result.ok(
        ArtistMapper.toResponseArray(
            repositoryResult.getValue()
        )
    );
  }
}