package com_maboroshi.maboroshi_spring.contexts.identity.application;

public record PasswordTooWeak(String message) implements RegisterError {

}
