package com_maboroshi.spring.contexts.catalog.application.dtos;

import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;

public record GenreFilteredResponse(
    BaseProduct[] products,
    Artist[] artists
) {}