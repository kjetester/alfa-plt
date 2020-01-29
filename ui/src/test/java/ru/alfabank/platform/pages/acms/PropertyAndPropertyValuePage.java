package ru.alfabank.platform.pages.acms;

import org.assertj.core.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import java.util.*;
import java.util.stream.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.reporting.BasicLogger.*;

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

  /**
   * Modifying property value.
   * @param propertyName property name
   * @param value property value
   * @param geoGroup geoGroup set
   * @return this
   */
  public PropertyAndPropertyValuePage modifyPropertyValue(String propertyName,
                                                          String value,
                                                          String... geoGroup) {
    info(String.format("Setting to the '%s' property '%s' value", propertyName, value));
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
   * Modifying property value.
   * @param propertyName property name
   * @param value property value
   * @param geoGroup geoGroup set
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage modifyValue(String propertyName,
                                                          String value,
                                                          String... geoGroup) {
    info(String.format("Setting to the '%s' property '%s' value", propertyName, value));
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    WebElement property = getDriver().findElement(propertySelector);
    List<WebElement> propertyValueList = property.findElements(propertyValuesSelector);
    WebElement existingPropertyValue = propertyValueList.get(propertyValueList.size() - 1);
    WebElement textArea = existingPropertyValue.findElement(propertyValueTextAreaSelector);
    setValueToMonacoTextArea(value, textArea);
    setGeoGroupsToWidget(existingPropertyValue, geoGroup);
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Adding a new property value.
   * @param propertyName property name
   * @param value property value
   * @param geoGroups geo group
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage createValue(String propertyName,
                                       String value,
                                       String... geoGroups) {
    info(String.format("Creating a new value '%s' for property '%s'", value, propertyName));
    addPropertyValueButton.click();
    String newPropertyValueSelectorXpath =
        "//span[text() = '%s']/../..//span[contains(text(), '\"\"')]/../../../../../../../../../..";
    By newPropertyValueSelector = By.xpath(
        String.format(newPropertyValueSelectorXpath, propertyName));
    info(String.format("Setting to the '%s' property '%s' value", propertyName, value));
    WebElement newPropertyValue = getDriver().findElement(newPropertyValueSelector);
    WebElement textArea =
        newPropertyValue.findElement(propertyValueTextAreaSelector);
    setValueToMonacoTextArea(value, textArea);
    info(String.format("Setting '%s' geo", geoGroups));
    setGeoGroupsToWidget(newPropertyValue, geoGroups);
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Setting geo-groups into a Widget.
   * @param propertyValue propertyValue value
   * @param geoGroups geoGroups
   */
  public void setGeoGroupsToWidget(WebElement propertyValue,
                                   String... geoGroups) {
    WebElement geoInput = propertyValue.findElement(propertyValueGeoInputSelector);
    List<WebElement> selectedGeoList = propertyValue.findElements(propertyValueSelectedGeoGroups);
    setGeoGroupsTo(selectedGeoList, geoInput, geoGroups);
  }

  /**
   * Restoring the property and its value.
   * @param propertyName property name
   * @param value value
   * @param geo geo
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage restorePropertyAndValues(String propertyName,
                                                    String value,
                                                    String... geo) {
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    if (isPresent(propertySelector)) {
      WebElement property = getDriver().findElement(propertySelector);
      List<WebElement> propertyValueList = property.findElements(propertyValuesSelector);
      if (propertyValueList.size() > 1) {
        IntStream.range(1, propertyValueList.size()).forEach(i -> {
          propertyValueList.get(i).findElement(propertyValueDeleteButtonSelector).click();
          waitForElementBecomesVisible(modalWindow);
          waitForElementBecomesClickable(modalWindowSubmitButton).click();
        });
      }
      if (propertyValueList.size() > 0) {
        modifyPropertyValue(
            propertyName,
            value,
            geo);
      } else {
        PageFactory.initElements(getDriver(), WidgetSidebarPage.class)
            .createNewPropertyToWorkWith(propertyName)
            .createValue(propertyName, value, geo);
      }
    }
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Checking for a found entity marking.
   * @param propertyName property name
   */
  public void checkPropertyMarking(String propertyName) {
    info(String.format("Checking if the propertyName '%s' has been marked", propertyName));
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    String actualColor = waitForElementBecomesClickable(
        getDriver().findElement(propertySelector).findElement(By.cssSelector("span")))
        .getCssValue("color");
    Assertions.assertThat(actualColor)
        .as("The propertyName hasn't been marked")
        .contains("24, 144, 255");
  }
}
