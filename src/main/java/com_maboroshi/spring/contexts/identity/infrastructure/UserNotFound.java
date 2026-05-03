package com_maboroshi.spring.contexts.identity.infrastructure;

import com_maboroshi.spring.shared.errors.RepositoryError;

public record UserNotFound(String message) implements RepositoryError {
}
