package com_maboroshi.spring.contexts.identity.application.errors;

public record PasswordTooWeak(String message) implements RegisterError {

}
