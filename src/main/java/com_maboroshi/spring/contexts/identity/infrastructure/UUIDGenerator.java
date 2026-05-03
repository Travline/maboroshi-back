package com_maboroshi.spring.contexts.identity.infrastructure;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import com_maboroshi.spring.contexts.identity.domain.UserIdGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDGenerator implements UserIdGenerator {
  NoArgGenerator generator = Generators.timeBasedEpochGenerator();

  @Override
  public UUID generateId() {
    return generator.generate();
  }
}
