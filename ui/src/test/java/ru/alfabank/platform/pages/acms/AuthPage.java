package ru.alfabank.platform.pages.acms;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.reporting.BasicLogger.*;

public class AuthPage extends BasePage {

  @FindBy(id = "username")
  private WebElement loginInput;
  @FindBy(id = "password")
  private WebElement passwordInput;
  @FindBy(name = "login")
  private WebElement submitBttn;

  /**
   * Authorization.
   * @param username username
   * @param password password
   * @return new instance of the MainPage class
   */
  public MainPage login(String username, String password) {
    info(String.format("Logging in as %s with password %s", username, password));
    loginInput.sendKeys(username);
    passwordInput.sendKeys(password);
    submitBttn.click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

}
