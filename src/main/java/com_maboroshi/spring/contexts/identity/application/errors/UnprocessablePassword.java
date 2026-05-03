package com_maboroshi.spring.contexts.identity.application.errors;

public record UnprocessablePassword(String message) implements RegisterError {
}
