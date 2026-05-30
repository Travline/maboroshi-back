package com_maboroshi.spring.contexts.catalog.presentation;

import com_maboroshi.spring.contexts.catalog.application.use_cases.GetProductsUseCase;
import com_maboroshi.spring.contexts.catalog.application.use_cases.GetRecommendedProductsUseCase;
import com_maboroshi.spring.contexts.catalog.application.use_cases.SearchProductsUseCase;
import com_maboroshi.spring.contexts.catalog.domain.ports.ProductRepository;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com_maboroshi.spring.contexts.catalog.application.use_cases.GetArtistsUseCase;

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
  public GetRecommendedProductsUseCase getRecommendedProductsUseCase(ProductRepository productRepository,
      AppLogger appLogger) {
    return new GetRecommendedProductsUseCase(productRepository, appLogger);
  }

  @Bean
  public GetArtistsUseCase getArtistsUseCase(
      ProductRepository productRepository,
      AppLogger appLogger) {
    return new GetArtistsUseCase(productRepository, appLogger);
  }
}
