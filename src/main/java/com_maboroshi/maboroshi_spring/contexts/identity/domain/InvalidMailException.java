package com_maboroshi.maboroshi_spring.contexts.identity.domain;

public class InvalidMailException extends Exception {
  public InvalidMailException(String errorMesage) {
    super(errorMesage);
  }
}
