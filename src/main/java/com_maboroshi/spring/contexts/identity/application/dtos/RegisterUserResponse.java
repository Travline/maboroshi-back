package com_maboroshi.spring.contexts.identity.application.dtos;

public record RegisterUserResponse(String userId, String username, String mail, int phone) {
}
