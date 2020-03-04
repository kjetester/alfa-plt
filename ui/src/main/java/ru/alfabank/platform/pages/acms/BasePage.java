package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.implicitlyWait;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesVisible;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.TestNGException;
import ru.alfabank.platform.buisenessobjects.User;

public class BasePage {

  private static final Logger LOGGER = LogManager.getLogger(BasePage.class);

  protected SoftAssertions softly = new SoftAssertions();

  @FindBy(css = ".ant-notification-notice")
  private WebElement notificationBanner;
  @FindBy(css = ".ant-notification-notice-message")
  private WebElement notificationMessage;
  @FindBy(css = ".ant-notification-notice-description")
  private WebElement notificationDescription;
  @FindBy (css = ".ant-notification-notice > a")
  protected WebElement bannerCloseBttn;
  @FindBy(className = "ant-modal-body")
  protected WebElement modalWindow;
  @FindBy(css = "[class ^= 'ant-modal'] .ant-btn-primary")
  protected WebElement modalWindowSubmitButton;
  @FindBy(xpath = "//i[@aria-label=\"icon: setting\"]/..")
  protected WebElement widgetSettings;
  private By deleteGeoButtonSelector = By.cssSelector(".ant-select-selection__choice__remove");
  private By errorIconSelector = By.cssSelector("[class $= 'icon-error']");

  /**
   * Open acms page.
   * @param url url
   * @param user user
   * @return this
   */
  public MainSliderPage openAndAuthorize(final String url, final User user) {
    String fullUrl =  url + "acms/";
    LOGGER.info(String.format("Перехожу по адресу '%s'", fullUrl));
    getDriver().get(fullUrl);
    return PageFactory.initElements(getDriver(), AuthPage.class)
        .login(user.getLogin(), user.getPassword());
  }

  /**
   * Setting geo-groups without using dropdown list.
   * @param geoGroupList geo-groups that are already chosen
   * @param geoGroupsInput input for geo-groups
   * @param geoGroups array of geo-groups that should be selected
   */
  protected void setGeoGroupsTo(List<WebElement> geoGroupList,
                                WebElement geoGroupsInput,
                                String[] geoGroups) {

    geoGroupList.forEach(geo ->
        geo.findElement(deleteGeoButtonSelector).click());
    geoGroupsInput.click();
    Arrays.asList(geoGroups).forEach(geo ->
        getDriver().switchTo().activeElement().sendKeys(geo, Keys.ENTER));
    getDriver().switchTo().activeElement().sendKeys(Keys.ESCAPE);
  }

  /**
   * Scrolling to given element.
   * @param element WebElement
   * @return WebElement
   */
  protected WebElement scrollToElement(WebElement element) {
    try {
      ((JavascriptExecutor) getDriver())
          .executeScript("arguments[0].scrollIntoView(true);", element);
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return element;
  }

  /**
   * Clearing MonacoTextArea and setting given value.
   * @param textValue value
   * @param textArea MonacoTextArea
   */
  protected void setValueToMonacoTextArea(String textValue, WebElement textArea) {
    try {
      textArea.click();
      textArea.sendKeys(Keys.chord(Keys.SHIFT, Keys.END), Keys.PAGE_DOWN, Keys.DELETE);
      textArea.sendKeys(textValue);
    } catch (ElementNotInteractableException e) {
      textArea.click();
      WebElement el = getDriver().switchTo().activeElement();
      el.sendKeys(Keys.HOME, Keys.chord(Keys.LEFT_SHIFT, Keys.END), Keys.DELETE);
      el.sendKeys(textValue);
    }
  }

  /**
   * Check if element is present within an element.
   * @param element element
   * @param selector selector
   * @return boolean
   */
  protected static boolean isPresent(WebElement element, By selector) {
    try {
      element.findElement(selector);
    } catch (org.openqa.selenium.NoSuchElementException e) {
      return false;
    }
    return true;
  }

  /**
   * Check if element is present on a page.
   * @param sharedMarkerSelector selector
   * @return boolean
   */
  protected static boolean isPresent(By sharedMarkerSelector) {
    try {
      getDriver().findElement(sharedMarkerSelector);
    } catch (org.openqa.selenium.NoSuchElementException e) {
      return false;
    }
    return true;
  }

  /**
   * Click by a button that showing by mouse hovering only.
   * @param element element
   */
  protected void clickOnHiddenButton(WebElement element) {
    new Actions(getDriver())
        .moveToElement(element)
        .pause(Duration.ofMillis(500L))
        .click()
        .build()
        .perform();
  }

  /**
   * JS click.
   * @param element element
   */
  protected void clickWithJavaScriptExecutor(WebElement element) {
    LOGGER.debug("Кликаю по труднодоступной кнопке JSом");
    ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
  }

  /**
   * Checkbox worker.
   * @param checkbox checkbox
   * @param isToBeSelected is to be selected
   */
  protected void setCheckboxTo(WebElement checkbox, boolean isToBeSelected) {
    boolean isAlreadySelected = checkbox.isSelected();
    // String label = checkbox.findElement(By.xpath("../../../..//label[@for]")).getText();
    // LOGGER.info(String.format("Устанавливаю чекбокс '%s' в '%b'", label, isToBeSelected));
    if (!isAlreadySelected && isToBeSelected) {
      checkbox.click();
    } else if (isAlreadySelected && !isToBeSelected) {
      checkbox.click();
    }
  }

  /**
   * Closing success banner if expected
   * otherwise fails a test.
   */
  protected void closeSuccessBanner() {
    waitForElementBecomesVisible(notificationBanner);
    implicitlyWait(false);
    try {
      if (isPresent(errorIconSelector)) {
        throw new TestNGException(String.format("'%s': '%s'",
            notificationMessage.getText(),
            notificationDescription.getText()));
      }
    } finally {
      implicitlyWait(true);
    }
    waitForElementBecomesClickable(bannerCloseBttn).click();
  }
}
