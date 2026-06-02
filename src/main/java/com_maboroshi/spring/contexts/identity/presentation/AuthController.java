package com_maboroshi.spring.contexts.identity.presentation;

import com_maboroshi.spring.contexts.identity.application.dtos.LoginUserMapper;
import com_maboroshi.spring.contexts.identity.application.dtos.LoginUserRequest;
import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserMapper;
import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserRequest;
import com_maboroshi.spring.contexts.identity.application.use_cases.LoginUserUseCase;
import com_maboroshi.spring.contexts.identity.application.use_cases.RegisterUserUseCase;
import com_maboroshi.spring.shared.utils.SessionCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

  private final RegisterUserUseCase registerUserUseCase;
  private final LoginUserUseCase loginUserUseCase;
  private final SessionCookie sessionCookie;

  public AuthController(RegisterUserUseCase registerUserUseCase, LoginUserUseCase loginUserUseCase, SessionCookie sessionCookie) {
    this.registerUserUseCase = registerUserUseCase;
    this.loginUserUseCase = loginUserUseCase;
    this.sessionCookie = sessionCookie;
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
}
