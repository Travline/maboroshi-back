package com_maboroshi.maboroshi_spring.contexts.identity.application;

public record CannotCreateUser(String message) implements RegisterError {
}
