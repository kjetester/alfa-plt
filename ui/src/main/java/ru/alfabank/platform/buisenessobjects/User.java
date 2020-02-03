package ru.alfabank.platform.buisenessobjects;

import org.apache.log4j.*;

public class User {

  private static final Logger LOGGER = LogManager.getLogger(User.class);

  private String login;
  private String password;

  /**
   * Class constructor.
   */
  public User() {
    this.login = "user1";
    this.password = "123";
    LOGGER.debug(
        String.format("Создан новый пользователь с логином '%s' и паролем '%s'", login, password));
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }
}
