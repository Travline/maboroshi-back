package com_maboroshi.maboroshi_spring.contexts.identity.application;

public record RegisterUserRequest(String username, String mail, String pwd, int number, int phone) {

}
