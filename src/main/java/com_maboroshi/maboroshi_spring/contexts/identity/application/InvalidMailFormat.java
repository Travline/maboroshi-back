package com_maboroshi.maboroshi_spring.contexts.identity.application;

public record InvalidMailFormat(String message) implements RegisterError {
}
