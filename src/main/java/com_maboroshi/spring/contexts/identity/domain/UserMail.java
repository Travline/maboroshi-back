package com_maboroshi.spring.contexts.identity.domain;

import com_maboroshi.spring.contexts.identity.domain.errors.InvalidMailException;

import java.util.regex.Pattern;

public class UserMail {
  private final String mail;
  private static final Pattern EMAIL_REGEX = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

  public UserMail(String mail) throws InvalidMailException {
    if (!isValid(mail)) {
      throw new InvalidMailException("Invalid mail value");
    }
    this.mail = mail;
  }

  private boolean isValid(String mail) {
    if (mail == null || !EMAIL_REGEX.matcher(mail).matches()) {
      return false;
    }
    return true;
  }

  public String toString() {
    return this.mail;
  }

  public boolean equals(UserMail newMail) {
    return this.mail.equals(newMail.toString());
  }
}
