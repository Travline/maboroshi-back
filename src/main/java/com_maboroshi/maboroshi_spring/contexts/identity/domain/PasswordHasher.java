package com_maboroshi.maboroshi_spring.contexts.identity.domain;

public interface PasswordHasher {
  public String hash(String pwd);

  public Boolean compare(String plain, String hash);
}
