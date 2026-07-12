package com_maboroshi.spring.contexts.catalog.presentation;

import com_maboroshi.spring.contexts.catalog.application.use_cases.*;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfiguration {

  @Bean
  public GetProductsUseCase getProductsUseCase(ProductRepository productRepository, AppLogger appLogger) {
    return new GetProductsUseCase(productRepository, appLogger);
  }

  @Bean
  public SearchProductsUseCase searchProductsUseCase(ProductRepository productRepository, AppLogger appLogger) {
    return new SearchProductsUseCase(productRepository, appLogger);
  }

  @Bean
  public GetRecommendedProductsUseCase getRecommendedProductsUseCase(ProductRepository productRepository, AppLogger appLogger) {
    return new GetRecommendedProductsUseCase(productRepository, appLogger);
  }

  @Bean
  public GetArtistsUseCase getArtistsUseCase(ProductRepository productRepository, AppLogger appLogger) {
    return new GetArtistsUseCase(productRepository, appLogger);
  }

  @Bean
  public GetArtistUseCase getArtistUseCase(ProductRepository productRepository, AppLogger appLogger) {
    return new GetArtistUseCase(productRepository, appLogger);
  }

  @Bean
  public GetProductsByArtistUseCase getProductsByArtistUseCase(ProductRepository productRepository, AppLogger appLogger) {
    return new GetProductsByArtistUseCase(productRepository, appLogger);
  }

  @Bean
  public GetGenreFilteredDataUseCase getGenreFilteredDataUseCase(ProductRepository productRepository) {
    return new GetGenreFilteredDataUseCase(productRepository);
  }
}
