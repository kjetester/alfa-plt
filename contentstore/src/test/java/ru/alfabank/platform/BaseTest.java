package ru.alfabank.platform;

import static ru.alfabank.platform.helpers.KeycloakHelper.logoutAllUsers;

import org.testng.annotations.AfterSuite;

public class BaseTest {

  @AfterSuite
  public void afterSuite() {
    logoutAllUsers();
  }
}
