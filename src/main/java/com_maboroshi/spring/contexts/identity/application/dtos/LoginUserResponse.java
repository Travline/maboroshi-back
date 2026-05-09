package com_maboroshi.spring.contexts.identity.application.dtos;

public record LoginUserResponse(String userId, String username, String mail, String phone) {
}
