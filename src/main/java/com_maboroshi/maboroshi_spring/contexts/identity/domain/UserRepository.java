package com_maboroshi.maboroshi_spring.contexts.identity.domain;

import java.util.Optional;

public interface UserRepository {
  public Optional<User> save(User user);

  public Optional<User> findById(String id);

  public Optional<User> findByMail(UserMail mail);
}
