package ru.alfabank.platform.buisenessobjects;

public class User {

  private String login;
  private String password;

  public User() {
    this.login = "user1";
    this.password = "123";
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }
}
