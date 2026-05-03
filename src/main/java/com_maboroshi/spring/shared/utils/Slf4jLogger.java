package com_maboroshi.spring.shared.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Slf4jLogger implements AppLogger {
  private static final Logger logger = LoggerFactory.getLogger(Slf4jLogger.class);

  @Override
  public void info(String message) {
    logger.info(message);
  }

  @Override
  public void warn(String message) {
    logger.warn(message);
  }

  @Override
  public void error(String message, Throwable cause) {
    logger.error(message, cause);
  }
}
