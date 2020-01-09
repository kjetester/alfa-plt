package ru.alfabank.platform.pages;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.alfabank.platform.pages.acms.BasePage;
import ru.alfabank.platform.pages.acms.MainPage;

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
    loginInput.sendKeys(username);
    passwordInput.sendKeys(password);
    submitBttn.click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

}
