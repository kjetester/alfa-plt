package ru.alfabank.platform.pages.acms;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class AuthPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(AuthPage.class);

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
   * @return new instance of the MainSliderPage class
   */
  public MainSliderPage login(String username, String password) {
    LOGGER.info(
        String.format("Авторизуюсь как пользователь '%s' с паролем '%s'", username, password));
    loginInput.sendKeys(username);
    passwordInput.sendKeys(password);
    submitBttn.click();
    return PageFactory.initElements(getDriver(), MainSliderPage.class);
  }

}
