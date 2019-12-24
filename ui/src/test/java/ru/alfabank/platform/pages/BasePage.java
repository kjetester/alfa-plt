package ru.alfabank.platform.pages;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BasePage {

  private JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
  private String operatingSystem = System.getProperties().getProperty("os.name");

  @FindBy (xpath = "//div[text() = 'Изменения успешно сохранены']")
  protected WebElement draftSavedNotificationBanner;
  @FindBy(xpath = "//div[text() = 'Изменения успешно опубликованы']")
  protected WebElement draftPublishedNotificationBanner;
  @FindBy(xpath = "//div[text() = 'Черновик успешно удалён']")
  protected WebElement draftDeletedNotificationBanner;
  @FindBy(className = "ant-modal-body")
  protected WebElement modalWindow;
  @FindBy(xpath = "//*[@class = 'ant-modal-body']//span[text() = 'Да']/..")
  protected WebElement modalWindowSubmitButton;
  @FindBy(xpath = "//i[@aria-label=\"icon: setting\"]/..")
  protected WebElement widgetSettings;

  /**
   * Setting geo-groups without using dropdown list.
   * @param geoGroupList geo-groups that are already chosen
   * @param geoGroupsInput input for geo-groups
   * @param geoGroups array of geo-groups that should be selected
   */
  public void setGeoGroupsTo(List<WebElement> geoGroupList,
                             WebElement geoGroupsInput,
                             String[] geoGroups) {

    geoGroupList.forEach(geo ->
        geo.findElement(By.cssSelector(".ant-select-selection__choice__remove")).click());
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
  public WebElement scrollToElement(WebElement element) {
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
   * Clearing input with Robot.
   */
  protected void clearInputWithRobot() {
    try {
      Robot robot = new Robot();
      if (operatingSystem.contains("Mac")) {
        robot.keyPress(KeyEvent.VK_META);
      } else {
        robot.keyPress(KeyEvent.VK_CONTROL);
      }
      robot.keyPress(KeyEvent.VK_A);
      robot.keyRelease(KeyEvent.VK_A);
      robot.keyRelease(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_DELETE);
      robot.keyRelease(KeyEvent.VK_DELETE);
    } catch (AWTException e) {
      e.printStackTrace();
    }
  }
}
