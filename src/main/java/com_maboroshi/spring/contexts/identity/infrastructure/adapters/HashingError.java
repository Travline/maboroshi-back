package com_maboroshi.spring.contexts.identity.infrastructure.adapters;

import com_maboroshi.spring.shared.errors.ServiceError;

public record HashingError(String message) implements ServiceError {
}
