package com_maboroshi.spring.shared.utils;

import com_maboroshi.spring.contexts.identity.domain.entities.User;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.InvalidTokenException;
import com_maboroshi.spring.shared.errors.SessionCookieError;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionCookie {
  private static final String COOKIE_NAME = "maboroshi-token";
  private final JwtService jwtService;
  private AppLogger appLogger;

  public SessionCookie(JwtService jwtService, AppLogger appLogger) {
    this.jwtService = jwtService;
    this.appLogger = appLogger;
  }

  public ResponseCookie createCookie(User user) {
    String token = jwtService.generateToken(user);

    return ResponseCookie.from(COOKIE_NAME, token)
            .path("/")
            .maxAge(30 * 60)
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .build();
  }

  public Result<UUID, SessionCookieError> getUserIdFromSessionCookie(String token) {
    try {
      if (token == null) {
        return Result.fail(new SessionCookieError("Cookies not found"));
      }

      UUID userId = jwtService.extractUserId(token);
      return Result.ok(userId);
    } catch (InvalidTokenException ite) {
      appLogger.warn("Session cookie invalid");
      return Result.fail(new SessionCookieError("Session cookie expired"));
    }
  }

}
