package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.implicitlyWait;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesVisible;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
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
import ru.alfabank.platform.businessobjects.User;

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
  protected WebElement modalWindowBody;
  @FindBy(css = "[class ^= 'ant-modal'] .ant-btn-primary")
  private WebElement modalWindowSubmitButton;
  @FindBy(xpath = "//i[@aria-label=\"icon: setting\"]/..")
  protected WebElement widgetSettings;
  private By deleteValueButtonSelector = By.cssSelector("[aria-label = 'close']");
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
   * Setting values to combobox.
   * @param selectedValues values that are already chosen
   * @param input input
   * @param desirableValues array of values that should be selected
   */
  protected void setValuesToCombobox(List<WebElement> selectedValues,
                                     WebElement input,
                                     List<?> desirableValues) {
    LOGGER.info("Удаляю все значения из комбобокса");
    implicitlyWait(false);
    selectedValues.forEach(value ->
        value.findElement(deleteValueButtonSelector).click());
    input.click();
    desirableValues.stream().map(Object::toString)
        .collect(Collectors.toList()).forEach(value -> {
          LOGGER.info(String.format("Устанавливаю значение: '%s'", value));
          getDriver().switchTo().activeElement().sendKeys(value, Keys.ENTER);
        });
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
      TimeUnit.SECONDS.sleep(1);
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
      LOGGER.info(String.format("Устанавливаю вэлью '%s'", textValue));
      LOGGER.debug("Ставлю фокус в инпут");
      textArea.click();
      LOGGER.debug("Очищаю поле - SHIFT + END + PAGE_DOWN и DELETE");
      textArea.sendKeys(Keys.chord(Keys.SHIFT, Keys.END), Keys.PAGE_DOWN, Keys.DELETE);
      LOGGER.info(String.format("Ввожу в инпут значение '%s'", textValue));
      textArea.sendKeys(textValue);
    } catch (ElementNotInteractableException e) {
      LOGGER.warn("An ElementNotInteractableException has been caught");
      textArea.click();
      clearInput(getDriver().switchTo().activeElement()).sendKeys(textValue);
    }
  }

  /**
   * Clear input.
   * @param element element
   * @return element
   */
  protected WebElement clearInput(WebElement element) {
    element.sendKeys(Keys.HOME, Keys.chord(Keys.LEFT_SHIFT, Keys.END), Keys.DELETE);
    return element;
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
   * @param hiddenElement hidden element
   */
  protected void hoverAndClick(WebElement hiddenElement) {
    new Actions(getDriver())
        .moveToElement(hiddenElement)
        .pause(500L)
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
    //TODO: String label = checkbox.findElement(By.xpath("../../../..//label[@for]")).getText();
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

  /**
   * Submit in a modal window.
   */
  protected void submitDialog() {
    LOGGER.debug("Подтверждаю действие");
    waitForElementBecomesVisible(modalWindowBody);
    modalWindowSubmitButton.click();
  }
}
