package com_maboroshi.spring.contexts.catalog.application.dtos;

public record GetProductsRequest(String orderBy, int length) {
}