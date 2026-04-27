package com_maboroshi.maboroshi_spring.contexts.identity.application;

public record UserAlreadyExists(String message) implements RegisterError {
}
