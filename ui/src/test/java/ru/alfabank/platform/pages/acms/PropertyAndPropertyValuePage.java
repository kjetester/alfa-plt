package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesVisible;

import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PropertyAndPropertyValuePage extends BasePage {

  @FindBy(xpath = "//*[@class = 'ant-btn ant-btn-default']")
  private WebElement addPropertyValueButton;

  private By propertyValuesSelector = By.cssSelector("[class ^= value-property-container]");
  private By propertyValueDeleteButtonSelector = By.cssSelector("button[mode = 'button']");
  private By propertyValueGeoInputSelector =
      By.cssSelector("div[class = 'ant-select-selection__rendered']");
  private By propertyValueSelectedGeoGroups = By.cssSelector("li[title]");
  private By propertyValueTextAreaSelector = By.cssSelector("div[class = 'view-line']");
  private String propertySelectorXpath = "//span[text() = '%s']/../..";
  private String newPropertyValueSelectorXpath =
      "//span[text() = '%s']/../..//span[contains(text(), '\"\"')]/../../../../../../../../..";

  /**
   * Modifying property value.
   * @param propertyName property name
   * @param value property value
   * @param geoGroup geoGroup set
   * @return this
   */
  @Step
  public PropertyAndPropertyValuePage modifyPropertyValue(String propertyName,
                                                          String value,
                                                          String... geoGroup) {
    System.out.println(
        String.format("Setting to the '%s' property '%s' value", propertyName, value));
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    WebElement property = getDriver().findElement(propertySelector);
    List<WebElement> propertyValueList = property.findElements(propertyValuesSelector);
    WebElement existingPropertyValue = propertyValueList.get(propertyValueList.size() - 1);
    WebElement textArea = existingPropertyValue.findElement(propertyValueTextAreaSelector);
    setValueToMonacoTextArea(value, textArea);
    setGeoGroupsToWidget(existingPropertyValue, geoGroup);
    return this;
  }

  /**
   * Adding a new property value.
   * @param propertyName property name
   * @param value property value
   * @param geoGroups geo group
   * @return WidgetSidebarPage
   */
  @Step
  public WidgetSidebarPage createValue(String propertyName,
                                       String value,
                                       String... geoGroups) {
    System.out.println("Creating a new property value");
    addPropertyValueButton.click();
    By newPropertyValueSelector = By.xpath(
        String.format(newPropertyValueSelectorXpath, propertyName));
    System.out.println(
        String.format("Setting to the '%s' property '%s' value", propertyName, value));
    WebElement propertyValue = getDriver().findElement(newPropertyValueSelector);
    WebElement textArea =
        propertyValue.findElement(propertyValueTextAreaSelector);
    setValueToMonacoTextArea(value, textArea);
    setGeoGroupsToWidget(propertyValue, geoGroups);
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Setting geo-groups into a Widget.
   * @param propertyValue propertyValue value
   * @param geoGroups geoGroups
   */
  @Step
  public void setGeoGroupsToWidget(WebElement propertyValue,
                                   String... geoGroups) {
    WebElement geoInput = propertyValue.findElement(propertyValueGeoInputSelector);
    List<WebElement> selectedGeoList = propertyValue.findElements(propertyValueSelectedGeoGroups);
    setGeoGroupsTo(selectedGeoList, geoInput, geoGroups);
  }

  /**
   * Deleting bottom property value.
   * @param propertyName property name
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage deletePropertyValue(String propertyName) {
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    WebElement property = getDriver().findElement(propertySelector);
    List<WebElement> propertyValueList = property.findElements(propertyValuesSelector);
    WebElement bottomPropertyValue = propertyValueList.get(propertyValueList.size() - 1);
    bottomPropertyValue.findElement(propertyValueDeleteButtonSelector).click();
    waitForElementBecomesVisible(modalWindow);
    waitForElementBecomesClickable(modalWindowSubmitButton).click();
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }
}
