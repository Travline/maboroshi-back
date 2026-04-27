package com_maboroshi.maboroshi_spring.contexts.identity.application;

public record UnprocessablePassword(String message) implements RegisterError {
}
