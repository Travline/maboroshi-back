package com_maboroshi.spring.contexts.catalog.application.use_cases;

import com_maboroshi.spring.contexts.catalog.application.dtos.ArtistDetailMapper;
import com_maboroshi.spring.contexts.catalog.application.dtos.ArtistDetailResponse;
import com_maboroshi.spring.contexts.catalog.application.errors.CatalogError;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsNotFound;
import com_maboroshi.spring.contexts.catalog.application.errors.ProductsRetrievalFailed;
import com_maboroshi.spring.contexts.catalog.domain.entities.ArtistDetail;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.utils.AppLogger;

public class GetArtistDetailUseCase {

  private final ProductRepository productRepository;
  private final AppLogger appLogger;

  public GetArtistDetailUseCase(
      ProductRepository productRepository,
      AppLogger appLogger
  ) {
    this.productRepository = productRepository;
    this.appLogger = appLogger;
  }

  public Result<ArtistDetailResponse, CatalogError> execute(
      String artistId
  ) {

    var repositoryResult =
        productRepository.getArtistDetail(artistId);

    if (!repositoryResult.isSucces) {
      appLogger.warn(
          "GetArtistDetail Error: could not retrieve artist detail"
      );

      return Result.fail(
          new ProductsRetrievalFailed(
              "Could not retrieve artist detail"
          )
      );
    }

    ArtistDetail artist = repositoryResult.getValue();

    if (artist == null) {
      appLogger.warn(
          "GetArtistDetail: artist not found"
      );

      return Result.fail(
          new ProductsNotFound(
              "Artist not found"
          )
      );
    }

    appLogger.info(
        "GetArtistDetail: retrieved artist " +
            artist.getName()
    );

    return Result.ok(
        ArtistDetailMapper.toResponse(artist)
    );
  }
}