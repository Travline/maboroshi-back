package com_maboroshi.spring.contexts.catalog.infrastructure.errors;

import com_maboroshi.spring.shared.errors.RepositoryError;

public record ProductsCannotBeRetrieved(String message) implements RepositoryError {
}