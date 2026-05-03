package com_maboroshi.spring.shared.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            // 1. Deshabilitar CSRF (indispensable para que funcionen los POST desde Postman)
            .csrf(csrf -> csrf.disable())

            // 2. Autorizar todas las peticiones sin autenticación
            .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()
            )

            // 3. Deshabilitar el formulario de login por defecto
            .formLogin(form -> form.disable())

            // 4. Deshabilitar la autenticación básica (la ventanita que pide user/pass)
            .httpBasic(basic -> basic.disable());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}