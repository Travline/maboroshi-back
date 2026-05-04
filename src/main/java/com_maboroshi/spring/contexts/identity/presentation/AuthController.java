package com_maboroshi.spring.contexts.identity.presentation;

import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserMapper;
import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserRequest;
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
  private final SessionCookie sessionCookie;

  public AuthController(RegisterUserUseCase registerUserUseCase, SessionCookie sessionCookie) {
    this.registerUserUseCase = registerUserUseCase;
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
}
