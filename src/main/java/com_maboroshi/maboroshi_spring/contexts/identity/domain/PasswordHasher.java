package com_maboroshi.maboroshi_spring.contexts.identity.domain;

import java.util.Optional;

public interface PasswordHasher {
  public Optional<String> hash(String pwd);

  public Boolean compare(String plain, String hash);
}
