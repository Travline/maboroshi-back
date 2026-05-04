package com_maboroshi.spring.shared.utils;

import com_maboroshi.spring.contexts.identity.domain.entities.User;
import com_maboroshi.spring.shared.errors.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

  private final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");
  private final long EXPIRATION_TIME = 30 * 60 * 1000;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  /**
   * Genera un token JWT con el ID del usuario como Subject.
   */
  public String generateToken(User user) {
    return Jwts.builder()
            .subject(user.getId().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSigningKey())
            .compact();
  }

  /**
   * Valida si el token es estructuralmente correcto y no ha expirado.
   */
  public boolean isTokenValid(String token) {
    try {
      Jwts.parser()
              .verifyWith(getSigningKey())
              .build()
              .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Valida el token y extrae el ID del usuario (UUID).
   * Lanza una excepción o devuelve null si el token no es válido.
   */
  public UUID extractUserId(String token) throws InvalidTokenException {
    if (!isTokenValid(token)) {
      throw new InvalidTokenException("Invalid o expired token");
    }

    String subject = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();

    return UUID.fromString(subject);
  }
}
