package com_maboroshi.spring.contexts.identity.application.errors;

public record CannotCreateUser(String message) implements RegisterError {
}
