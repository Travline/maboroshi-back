package com_maboroshi.spring.contexts.identity.presentation;

import com_maboroshi.spring.contexts.identity.application.RegisterUserUseCase;
import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserMapper;
import com_maboroshi.spring.contexts.identity.application.dtos.RegisterUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

  private final RegisterUserUseCase registerUserUseCase;

  public AuthController(RegisterUserUseCase registerUserUseCase) {
    this.registerUserUseCase = registerUserUseCase;
  }

  @PostMapping("register")
  public ResponseEntity<?> createUser(@RequestBody RegisterUserRequest userReq) {
    return registerUserUseCase.execute(userReq).match(
            user -> ResponseEntity.status(201).body(RegisterUserMapper.toResponse(user)),
            error -> ResponseEntity.status(AuthStatusMapper.getStatus(error)).body(error.message())
    );
  }
}
