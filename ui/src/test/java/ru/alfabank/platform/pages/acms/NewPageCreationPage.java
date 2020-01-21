package ru.alfabank.platform.pages.acms;

import io.qameta.allure.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

public class NewPageCreationPage extends BasePage {

  @FindBy(css = "#uri")
  private WebElement pathInput;
  @FindBy(css = "#title")
  private WebElement titleInput;
  @FindBy(css = "#description")
  private WebElement descriptionInput;
  @FindBy(css = "")
  private WebElement startDate;
  @FindBy(css = "")
  private WebElement endDate;
  @FindBy(css = "button[type = 'submit']")
  private WebElement submitButton;

  /**
   * Filling and submitting the creation new page form.
   * @param pageUri page URI
   */
  @Step
  public void fillCreationForm(String pageUri){
    waitForElementBecomesClickable(pathInput).sendKeys(pageUri);
    titleInput.sendKeys(pageUri);
    descriptionInput.sendKeys(pageUri);
    waitForElementBecomesClickable(submitButton).click();
  };
}
