package com_maboroshi.maboroshi_spring.contexts.identity.domain;

public class User {
  private String id;
  private String username;
  private UserMail mail;
  private String pwd;
  private int phone; // Queda pendiente una validaciónpara proteger la entidad
  private boolean isActive;

  public User(String id, String username, UserMail mail, String pwd, int phone, boolean isActive) {
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

  public String getId() {
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

  public int getPhone() {
    return phone;
  }

  public boolean isActive() {
    return isActive;
  }
}
