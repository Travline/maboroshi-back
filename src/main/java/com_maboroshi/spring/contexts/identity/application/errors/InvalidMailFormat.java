package com_maboroshi.spring.contexts.identity.application.errors;

public record InvalidMailFormat(String message) implements RegisterError {
}
