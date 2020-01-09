package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesVisible;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementsBecomeVisible;

import io.qameta.allure.Step;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WidgetSidebarPage extends BasePage {

  @FindBy(xpath = "//div[@class = 'ant-collapse-header']/..")
  private List<WebElement> propList;
  @FindBy(xpath = "//div[@class = 'ant-collapse-header']/span")
  private List<WebElement> propNameList;
  @FindBy(xpath = "//div[contains(text(), 'Выберите свойство')]")
  private WebElement newPropNameInput;
  @FindBy(css = "[role='listbox']")
  private WebElement newPropDropdownList;
  @FindBy(xpath = "//span[text() = 'Добавить']/..")
  private WebElement addNewPropButton;
  @FindBy(css = "button[class='ant-btn ant-btn-primary']")
  private WebElement submitButton;

  /**
   * New Property addition to widget.
   * @param newPropertyName name
   * @return this
   */
  @Step
  public WidgetSidebarPage createNewProperty(String newPropertyName) {
    newPropNameInput.click();
    waitForElementBecomesVisible(newPropDropdownList);
    getDriver().switchTo().activeElement().sendKeys(Keys.chord(newPropertyName), Keys.ENTER);
    waitForElementBecomesClickable(addNewPropButton).click();
    return this;
  }

  /**
   * Checking the result of addition a new Widget.
   * @param newName name
   */
  @Step
  public WidgetSidebarPage checkIfPropertyWasAdded(String newName) {
    waitForElementsBecomeVisible(propNameList);
    Assertions.assertThat(propNameList.stream().anyMatch(e -> newName.equals(e.getText())))
        .as("Checking if widget with name '%s' is present", newName)
        .isTrue();
    return this;
  }

  /**
   * Submitting changes.
   * @return Main Page reinitiated instance.
   */
  @Step
  public MainPage submitChanges() {
    System.out.println("Submitting changes");
    waitForElementBecomesClickable(submitButton).click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Deleting property.
   * @param propName property name
   * @return this
   */
  @Step
  public WidgetSidebarPage deleteProperty(String propName) {
    waitForElementBecomesClickable(getDriver().findElement(By.xpath(String.format(
                "//div[@class = 'ant-collapse-header']/span[text() = '%s']/..//button", propName))))
        .click();
    waitForElementBecomesVisible(modalWindow);
    modalWindowSubmitButton.click();
    return this;
  }

  /**
   * Checking the result of deletion the Widget.
   * @param propName property name
   * @return this
   */
  @Step
  public WidgetSidebarPage checkIfPropertyIsAbsent(String propName) {
    waitForElementsBecomeVisible(propNameList);
    Assertions.assertThat(propNameList.stream().anyMatch(e -> propName.equals(e.getText())))
        .as("Checking if widget with name '%s' is absent", propName)
        .isFalse();
    return this;
  }

  /**
   * Expanding a Widget Meta Info panel.
   * @return new instance of WidgetMetaInfoPage
   */
  @Step
  public WidgetMetaInfoPage expandWidgetMetaInfo() {
    System.out.println("Expanding Widget's MetaInfo pane");
    waitForElementBecomesClickable(widgetSettings);
    widgetSettings.click();
    return PageFactory.initElements(getDriver(), WidgetMetaInfoPage.class);
  }
}
