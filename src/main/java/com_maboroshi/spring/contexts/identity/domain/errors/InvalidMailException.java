package com_maboroshi.spring.contexts.identity.domain.errors;

public class InvalidMailException extends Exception {
  public InvalidMailException(String errorMesage) {
    super(errorMesage);
  }
}
