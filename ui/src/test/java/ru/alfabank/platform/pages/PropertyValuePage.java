package ru.alfabank.platform.pages;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PropertyValuePage extends BasePage {

  @FindBy(xpath = "//*[@class = 'ant-btn ant-btn-default']")
  private WebElement addPropertyValueButton;

  /**
   * Modifying property value.
   * @param propertyName property name
   * @param propertyValue property value
   * @param geoGroup geoGroup set
   * @return this
   */
  @Step
  public PropertyValuePage modifyPropertyValue(String propertyName,
                                               String propertyValue,
                                               String... geoGroup) {
    By propertySelector = By.xpath(String.format("//span[text() = '%s']/../..", propertyName));
    WebElement property = getDriver().findElement(propertySelector);
    List<WebElement> propertyValueList
        = property.findElements(By.cssSelector("[class ^= value-property-container]"));
    ////*[contains(@class, 'value-property-container')]
    WebElement existingPropertyValue = propertyValueList.get(propertyValueList.size() - 1);
    WebElement propertyValueInput =
        existingPropertyValue.findElement(By.cssSelector("[class = view-line]"));
    propertyValueInput.click();
    // clearInputWithRobot();
    getDriver().switchTo().activeElement().clear();
    getDriver().switchTo().activeElement().sendKeys(propertyValue);
    setGeoGroupsToWidget(existingPropertyValue, geoGroup);
    return this;
  }

  /**
   * Adding a new property value.
   * @param propertyName property name
   * @param propertyValue property value
   * @param geoGroups geo group
   * @return WidgetSidebarPage
   */
  @Step
  public WidgetSidebarPage createPropertyValue(String propertyName,
                                               String propertyValue,
                                               String... geoGroups) {
    By newPropertyValueSelector = By.xpath(
        String.format("//span[text() = '%s']/../..//span[contains(text(), '\"\"')]"
            + "/../../../../../../../../..", propertyName));
    addPropertyValueButton.click();
    WebElement newPropertyValue = getDriver().findElement(newPropertyValueSelector);
    WebElement newPropertyValueInput =
        newPropertyValue.findElement(By.cssSelector("div[class = 'view-line']"));
    newPropertyValueInput.click();
    // clearInputWithRobot();
    getDriver().switchTo().activeElement().sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE);
    getDriver().switchTo().activeElement().sendKeys(propertyValue);
    setGeoGroupsToWidget(newPropertyValue, geoGroups);
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
    WebElement geoInput = propertyValue.findElement(
            By.cssSelector("div[class = 'ant-select-selection__rendered']"));
    List<WebElement> selectedGeoList = propertyValue.findElements(By.cssSelector("li[title]"));
    setGeoGroupsTo(selectedGeoList, geoInput, geoGroups);
  }
}
