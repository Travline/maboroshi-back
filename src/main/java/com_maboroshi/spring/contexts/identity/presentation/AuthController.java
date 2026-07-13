package com_maboroshi.spring.contexts.identity.presentation;

import com_maboroshi.spring.contexts.identity.application.dtos.LoginUserMapper;
import com_maboroshi.spring.contexts.identity.application.dtos.LoginUserRequest;
import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserMapper;
import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserRequest;
import com_maboroshi.spring.contexts.identity.application.use_cases.LoginUserUseCase;
import com_maboroshi.spring.contexts.identity.application.use_cases.RegisterUserUseCase;
import com_maboroshi.spring.contexts.identity.domain.ports.UserRepository;
import com_maboroshi.spring.contexts.wishlist.errors.UserUnauthenticated;
import com_maboroshi.spring.shared.utils.SessionCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

  private final RegisterUserUseCase registerUserUseCase;
  private final LoginUserUseCase loginUserUseCase;
  private final SessionCookie sessionCookie;
  private final UserRepository userRepository;

  public AuthController(RegisterUserUseCase registerUserUseCase, LoginUserUseCase loginUserUseCase, SessionCookie sessionCookie, UserRepository userRepository) {
    this.registerUserUseCase = registerUserUseCase;
    this.loginUserUseCase = loginUserUseCase;
    this.sessionCookie = sessionCookie;
    this.userRepository = userRepository;
  }

  @PostMapping("register")
  public ResponseEntity<?> createUser(@RequestBody RegisterUserRequest userReq) {
    return registerUserUseCase.execute(userReq).match(
        user -> ResponseEntity
            .status(201)
            .header(HttpHeaders.SET_COOKIE, sessionCookie.createCookie(user).toString())
            .body(RegisterUserMapper.toResponse(user)),
        error -> ResponseEntity
            .status(AuthStatusMapper.getStatus(error))
            .body(error.message())
    );
  }

  @PostMapping("login")
  public ResponseEntity<?> login(@RequestBody LoginUserRequest credentials) {
    return loginUserUseCase.execute(credentials).match(
        user -> ResponseEntity
            .status(200)
            .header(HttpHeaders.SET_COOKIE, sessionCookie.createCookie(user).toString())
            .body(LoginUserMapper.toResponse(user)),
        error -> ResponseEntity
            .status(AuthStatusMapper.getStatus(error))
            .body(error.message())
    );
  }

  @GetMapping("me")
  public ResponseEntity<HashMap<String, String>> iAm(
      @CookieValue(name = "maboroshi-token", required = false) String token
  ) {
    UUID userId = extractUserIdOrThrow(token);

    var userResult = userRepository.findById(userId);
    if (!userResult.isSucces) {
      var error = new HashMap<String, String>();
      error.put("error", "User not found");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    var response = new HashMap<String, String>();
    response.put("userId", userResult.getValue().getId().toString());
    response.put("username", userResult.getValue().getUsername());
    response.put("phone", userResult.getValue().getPhone());
    response.put("mail", userResult.getValue().getMail().toString());

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  private UUID extractUserIdOrThrow(String token) {
    var cookieRes = sessionCookie.getUserIdFromSessionCookie(token);
    if (!cookieRes.isSucces) {
      throw new UserUnauthenticated("Session cookie expired or invalid");
    }
    return cookieRes.getValue();
  }
}
