package com_maboroshi.spring.contexts.identity.domain;

import java.util.UUID;

public class User {
  private UUID id;
  private String username;
  private UserMail mail;
  private String pwd;
  private String phone; // Queda pendiente una validaciónpara proteger la entidad
  private boolean isActive;

  public User(UUID id, String username, UserMail mail, String pwd, String phone, boolean isActive) {
    this.id = id;
    this.username = username;
    this.mail = mail;
    this.pwd = pwd;
    this.phone = phone;
    this.isActive = isActive;
  }

  public void deactive() {
    this.isActive = false;
  }

  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public UserMail getMail() {
    return mail;
  }

  public String getPwd() {
    return pwd;
  }

  public String getPhone() {
    return phone;
  }

  public boolean isActive() {
    return isActive;
  }
}
