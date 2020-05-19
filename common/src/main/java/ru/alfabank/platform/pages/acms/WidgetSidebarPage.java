package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesVisible;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementsBecomeVisible;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WidgetSidebarPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(WidgetSidebarPage.class);

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
   *
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
   *
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
   *
   * @param newName name
   */
  public WidgetSidebarPage checkIfPropertyWasAdded(String newName) {
    LOGGER.info(
        String.format("Проверяю, добавлено ли новое проперти с названием '%s'", newName));
    waitForElementsBecomeVisible(propDropdownList);
    Assertions.assertThat(propDropdownList.stream().anyMatch(e -> newName.equals(e.getText())))
        .as("Проперти с названием '{}' не найдено", newName)
        .isTrue();
    return this;
  }

  /**
   * Submitting changes.
   *
   * @return Main Page reinitiated instance.
   */
  public MainPage submitChanges() {
    LOGGER.info("Подтверждаю изменения");
    waitForElementBecomesClickable(submitButton).click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Deleting property.
   *
   * @param propName property name
   * @return this
   */
  public WidgetSidebarPage deleteProperty(String propName) {
    WebElement deleteBttn = getDriver().findElement(By.xpath(String.format("//div[@class = "
        + "'ant-collapse-header']/span[text() = '%s']/..//button", propName)));
    waitForElementBecomesClickable(deleteBttn);
    deleteBttn.click();
    submitDialog();
    return this;
  }

  /**
   * Checking the result of deletion the Widget.
   *
   * @param propName property name
   * @return this
   */
  public WidgetSidebarPage checkIfPropertyIsAbsent(String propName) {
    waitForElementsBecomeVisible(propDropdownList);
    Assertions.assertThat(propDropdownList.stream().anyMatch(e -> propName.equals(e.getText())))
        .as("Виджет с названием '{}' все еще присутствует", propName)
        .isFalse();
    return this;
  }

  /**
   * Expanding a Widget Meta Info panel.
   *
   * @return new instance of WidgetMetaInfoPage
   */
  public WidgetMetaInfoPage expandWidgetMetaInfo() {
    LOGGER.info("Раскрываю панель метаинфо виджета");
    waitForElementBecomesClickable(widgetSettings).click();
    return PageFactory.initElements(getDriver(), WidgetMetaInfoPage.class);
  }

  /**
   * Closing Widget's sidebar.
   *
   * @return new instance of the MainSliderPage class
   */
  public MainSliderPage closeWidgetSidebarAndGoToMainSlider() {
    closeWidgetSidebar();
    return PageFactory.initElements(getDriver(), MainSliderPage.class);
  }

  /**
   * Closing Widget's sidebar.
   *
   * @return new instance of the MainPage class
   */
  public MainPage closeWidgetSidebarAndGoToMainPage() {
    closeWidgetSidebar();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Closing Widget's sidebar.
   */
  private void closeWidgetSidebar() {
    LOGGER.info("Закрываю сайдбар виджета");
    waitForElementBecomesClickable(widgetSidebarCloseButton).click();
  }
}
