package com_maboroshi.spring.contexts.catalog.presentation;

import com_maboroshi.spring.contexts.catalog.application.dtos.GetProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.dtos.GetRecommendedProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.dtos.SearchProductsRequest;
import com_maboroshi.spring.contexts.catalog.application.use_cases.GetProductsUseCase;
import com_maboroshi.spring.contexts.catalog.application.use_cases.GetRecommendedProductsUseCase;
import com_maboroshi.spring.contexts.catalog.application.use_cases.SearchProductsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com_maboroshi.spring.contexts.catalog.application.use_cases.GetArtistsUseCase;
import com_maboroshi.spring.contexts.catalog.application.use_cases.GetArtistDetailUseCase;

import java.util.List;

@RestController
@RequestMapping("v1/catalog")
public class CatalogController {

    private static final int DEFAULT_LENGTH = 10;

    private final GetProductsUseCase getProductsUseCase;
    private final SearchProductsUseCase searchProductsUseCase;
    private final GetRecommendedProductsUseCase getRecommendedProductsUseCase;
    private final GetArtistsUseCase getArtistsUseCase;
    private final GetArtistDetailUseCase getArtistDetailUseCase;

    public CatalogController(
            GetProductsUseCase getProductsUseCase,
            SearchProductsUseCase searchProductsUseCase,
            GetRecommendedProductsUseCase getRecommendedProductsUseCase,
            GetArtistsUseCase getArtistsUseCase,
            GetArtistDetailUseCase getArtistDetailUseCase) {
        this.getProductsUseCase = getProductsUseCase;
        this.searchProductsUseCase = searchProductsUseCase;
        this.getRecommendedProductsUseCase = getRecommendedProductsUseCase;
        this.getArtistsUseCase = getArtistsUseCase;
        this.getArtistDetailUseCase = getArtistDetailUseCase;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(
            @RequestParam(name = "order_by", defaultValue = "date") String orderBy) {
        return getProductsUseCase.execute(new GetProductsRequest(orderBy, DEFAULT_LENGTH)).match(
                products -> ResponseEntity.ok(products),
                error -> ResponseEntity
                        .status(CatalogStatusMapper.getStatus(error))
                        .body(error.message()));
    }

    @GetMapping("/products/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam String term) {
        return searchProductsUseCase.execute(new SearchProductsRequest(term)).match(
                products -> ResponseEntity.ok(products),
                error -> ResponseEntity
                        .status(CatalogStatusMapper.getStatus(error))
                        .body(error.message()));
    }

    @PostMapping("/products/recommended")
    public ResponseEntity<?> getRecommendedProducts(
            @RequestBody List<String> names) {
        return getRecommendedProductsUseCase.execute(
                new GetRecommendedProductsRequest(names.toArray(new String[0]))).match(
                        products -> ResponseEntity.ok(products),
                        error -> ResponseEntity
                                .status(CatalogStatusMapper.getStatus(error))
                                .body(error.message()));
    }

    @GetMapping("/artists")
    public ResponseEntity<?> getArtists() {
        return getArtistsUseCase.execute().match(
                artists -> ResponseEntity.ok(artists),
                error -> ResponseEntity.badRequest().body(error));

    }

    @GetMapping("/artists/{artistId}")
    public ResponseEntity<?> getArtistDetail(
            @PathVariable String artistId) {
        return getArtistDetailUseCase.execute(artistId).match(
                artist -> ResponseEntity.ok(artist),
                error -> ResponseEntity
                        .status(CatalogStatusMapper.getStatus(error))
                        .body(error.message()));
    }

}
