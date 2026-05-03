package com_maboroshi.spring.contexts.identity.domain;

import java.util.UUID;

public interface UserIdGenerator {
  public UUID generateId();
}
