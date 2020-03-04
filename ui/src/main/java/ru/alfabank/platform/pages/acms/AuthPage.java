package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
