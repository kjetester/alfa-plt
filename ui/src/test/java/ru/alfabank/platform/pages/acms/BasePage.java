package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BasePage {

  @FindBy (xpath = "//div[text() = 'Изменения успешно сохранены']")
  protected WebElement draftSavedNotificationBanner;
  @FindBy(xpath = "//div[text() = 'Изменения успешно опубликованы']")
  protected WebElement draftPublishedNotificationBanner;
  @FindBy(xpath = "//div[text() = 'Черновик успешно удалён']")
  protected WebElement draftDeletedNotificationBanner;
  @FindBy(className = "ant-modal-body")
  protected WebElement modalWindow;
  @FindBy(css = ".ant-modal-body .ant-btn-primary")
  protected WebElement modalWindowSubmitButton;
  @FindBy(xpath = "//i[@aria-label=\"icon: setting\"]/..")
  protected WebElement widgetSettings;
  private By deleteGeoButtonSelector = By.cssSelector(".ant-select-selection__choice__remove");

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
      textArea.sendKeys(Keys.chord(Keys.SHIFT, Keys.END), Keys.DELETE);
      textArea.sendKeys(textValue);
    } catch (ElementNotInteractableException e) {
      textArea.click();
      WebElement el = getDriver().switchTo().activeElement();
      el.sendKeys(Keys.HOME, Keys.chord(Keys.LEFT_SHIFT, Keys.END), Keys.DELETE);
      el.sendKeys(textValue);
    }
  }

  protected static boolean isPresent(WebElement w, By sharedMarkerSelector) {
    try {
      w.findElement(sharedMarkerSelector);
    } catch (org.openqa.selenium.NoSuchElementException e) {
      return false;
    }
    return true;
  }
}
