package ru.alfabank.platform.pages.acms;

import org.assertj.core.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.testng.log4testng.*;

import java.util.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.reporting.BasicLogger.info;

public class WidgetSidebarPage extends BasePage {

  private static final Logger LOGGER = Logger.getLogger(WidgetSidebarPage.class);

  @FindBy(xpath = "//div[@class = 'ant-collapse-header']/..")
  private List<WebElement> propList;
  @FindBy(xpath = "//div[@class = 'ant-collapse-header']/span")
  private List<WebElement> propDropdownList;
  @FindBy(css = "[class $= 'ant-select-allow-clear'] .ant-select-selection__rendered")
  private WebElement newPropNameInput;
  @FindBy(css = "[role='listbox']")
  private WebElement newPropDropdownList;
  @FindBy(xpath = "//span[text() = 'Добавить']/..")
  private WebElement addNewPropButton;
  @FindBy(css = ".ant-drawer-body button[class='ant-btn ant-btn-primary']")
  private WebElement submitButton;
  @FindBy(css = "button[aria-label='Close']")
  private WebElement widgetSidebarCloseButton;

  /**
   * New Property addition to widget.
   * @param newPropertyName name
   * @return this
   */
  public WidgetSidebarPage createNewProperty(String newPropertyName) {
    newPropNameInput.click();
    waitForElementBecomesVisible(newPropDropdownList);
    getDriver().switchTo().activeElement().sendKeys(Keys.chord(newPropertyName), Keys.ENTER);
    waitForElementBecomesClickable(addNewPropButton).click();
    return this;
  }

  /**
   * New Property addition to widget.
   * @param newPropertyName name
   * @return PropertyAndPropertyValuePage
   */
  public PropertyAndPropertyValuePage createNewPropertyToWorkWith(String newPropertyName) {
    newPropNameInput.click();
    waitForElementBecomesVisible(newPropDropdownList);
    getDriver().switchTo().activeElement().sendKeys(Keys.chord(newPropertyName), Keys.ENTER);
    waitForElementBecomesClickable(addNewPropButton).click();
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }

  /**
   * Checking the result of the new Property addition.
   * @param newName name
   */
  public WidgetSidebarPage checkIfPropertyWasAdded(String newName) {
    info(String.format("Checking if the new Property named '%s' has been added", newName));
    waitForElementsBecomeVisible(propDropdownList);
    Assertions.assertThat(propDropdownList.stream().anyMatch(e -> newName.equals(e.getText())))
        .as("The Property named '{}' not found", newName)
        .isTrue();
    return this;
  }

  /**
   * Submitting changes.
   * @return Main Page reinitiated instance.
   */
  public MainPage submitChanges() {
    LOGGER.info("Submitting changes");
    waitForElementBecomesClickable(submitButton).click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Deleting property.
   * @param propName property name
   * @return this
   */
  public WidgetSidebarPage deleteProperty(String propName) {
    WebElement deleteBttn = getDriver().findElement(By.xpath(String.format("//div[@class = "
        + "'ant-collapse-header']/span[text() = '%s']/..//button", propName)));
    waitForElementBecomesClickable(deleteBttn);
    deleteBttn.click();
    waitForElementBecomesVisible(modalWindow);
    modalWindowSubmitButton.click();
    return this;
  }

  /**
   * Checking the result of deletion the Widget.
   * @param propName property name
   * @return this
   */
  public WidgetSidebarPage checkIfPropertyIsAbsent(String propName) {
    waitForElementsBecomeVisible(propDropdownList);
    Assertions.assertThat(propDropdownList.stream().anyMatch(e -> propName.equals(e.getText())))
        .as("Checking if widget with name '%s' is absent", propName)
        .isFalse();
    return this;
  }

  /**
   * Expanding a Widget Meta Info panel.
   * @return new instance of WidgetMetaInfoPage
   */
  public WidgetMetaInfoPage expandWidgetMetaInfo() {
    LOGGER.info("Expanding Widget's MetaInfo pane");
    waitForElementBecomesClickable(widgetSettings).click();
    return PageFactory.initElements(getDriver(), WidgetMetaInfoPage.class);
  }

  /**
   * Closing Widget's sidebar.
   * @return new instance of MainPage
   */
  public MainPage closeWidgetSidebar() {
    LOGGER.info("Closing Widget's sidebar");
    waitForElementBecomesClickable(widgetSidebarCloseButton).click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }
}
