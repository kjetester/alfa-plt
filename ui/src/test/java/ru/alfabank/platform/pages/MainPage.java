package ru.alfabank.platform.pages;

import static ru.alfabank.platform.helpers.DriverSingleton.getDriver;
import static ru.alfabank.platform.helpers.DriverSingleton.waitForElementBecomesVisible;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage {

  @FindBy(className = "logo_1TN8l")
  private WebElement logo;

  /**
   * Opening acms.
   * @return boolean
   */
  public boolean openAndAuthorize() {
    String baseUrl = "http://admin:h3VtGrE696@develop.ci.k8s.alfa.link/acms/pages/";
    getDriver().get(baseUrl);
    waitForElementBecomesVisible(logo, 55);
    return logo.isDisplayed();
  }
}
