package com_maboroshi.spring.contexts.identity.application.errors;

public record UserAlreadyExists(String message) implements RegisterError {
}
