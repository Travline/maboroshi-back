package com_maboroshi.spring.shared.utils;

public interface AppLogger {
  void warn(String message);

  void error(String message, Throwable cause);

  void info(String message);
}
