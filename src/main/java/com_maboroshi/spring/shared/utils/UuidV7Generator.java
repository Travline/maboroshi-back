package com_maboroshi.spring.shared.utils;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidV7Generator implements UuidGenerator {
  NoArgGenerator generator = Generators.timeBasedEpochGenerator();

  @Override
  public UUID generateId() {
    return generator.generate();
  }
}
