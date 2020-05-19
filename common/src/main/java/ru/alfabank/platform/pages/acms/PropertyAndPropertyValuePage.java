package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.alfabank.platform.businessobjects.enums.Geo;

public class PropertyAndPropertyValuePage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(PropertyAndPropertyValuePage.class);

  @FindBy(css = "div:nth-child(17) div.ant-drawer-content-wrapper div.ant-drawer-header > button")
  private WebElement closeButton;
  @FindBy(css = "[role='combobox'] ")
  private WebElement newPropDropdownList;
  @FindBy(xpath = "//span[contains(text(), 'Добавить')]/..")
  private WebElement newPropAddSubmitButton;
  @FindBy(xpath = "//*[@class = 'ant-btn ant-btn-default']")
  private WebElement addPropertyValueButton;
  private By propertyValuesSelector = By.cssSelector("[data-test-type = 'value']");
  private By propertyValueDeleteButtonSelector = By.cssSelector("button[mode = 'button']");
  private By propertyValueGeoInputSelector = By.cssSelector(".ant-select-selection__rendered");
  private By propertyValueSelectedGeoGroups = By.cssSelector("li[title]");
  private By propertyValueInputSelector = By.cssSelector("div[class = 'view-line']");
  private By propertyValueSpansSelector = By.cssSelector("span > span");
  private String propertySelectorXpath = "//span[text() = '%s']/../..";

  /**
   * Modifying property value.
   *
   * @param propertyName property name
   * @param value        property value
   * @param geos         geos
   * @return this
   */
  public PropertyAndPropertyValuePage modifyValueContinueWithPropertyPage(
      String propertyName,
      String value,
      Geo... geos) {
    modifyVal(propertyName, value, Arrays.asList(geos));
    return this;
  }

  /**
   * Modifying property value.
   *
   * @param propertyName property name
   * @param value        property value
   * @param geos         geos
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage modifyValueAndContinueWithWidgetSidebarPage(String propertyName,
                                                                       String value,
                                                                       Geo... geos) {
    modifyVal(propertyName, value, Arrays.asList(geos));
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Modifying existing property value.
   *
   * @param propertyName property name
   * @param value        property value
   * @param geoList      geos
   */
  private void modifyVal(String propertyName,
                         String value,
                         List<Geo> geoList) {
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    WebElement property = getDriver().findElement(propertySelector);
    List<WebElement> propertyValuesList = property.findElements(propertyValuesSelector);
    WebElement propertyValueArea = propertyValuesList.get(propertyValuesList.size() - 1);
    WebElement valueInput = propertyValueArea.findElement(propertyValueInputSelector);
    setValueToMonacoTextArea(value, valueInput);
    WebElement geoInput = propertyValueArea.findElement(propertyValueGeoInputSelector);
    // TODO: setGeoGroups(geoInput, geoGroup);
  }

  /**
   * Adding a new property value.
   *
   * @param propertyName property name
   * @param value        property value
   * @param geos         geos
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage createValue(String propertyName,
                                       String value,
                                       Geo... geos) {
    LOGGER.info(String.format("Creating a new value '%s' for property '%s'", value, propertyName));
    addPropertyValueButton.click();
    String newPropertyValueSelectorXpath =
        "//span[text() = '%s']/../..//span[contains(text(), '\"\"')]/../../../../../../../../../..";
    By newPropertyValueSelector = By.xpath(
        String.format(newPropertyValueSelectorXpath, propertyName));
    LOGGER.info(String.format("Setting to the '%s' property '%s' value", propertyName, value));
    WebElement newPropertyValue = getDriver().findElement(newPropertyValueSelector);
    WebElement textArea =
        newPropertyValue.findElement(propertyValueInputSelector);
    setValueToMonacoTextArea(value, textArea);
    List<Geo> geoList = Arrays.asList(geos);
    LOGGER.info(String.format("Устанавливаю гео-групп(ы) '%s'", geoList));
    setGeoGroups(newPropertyValue, geoList);
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Setting up geo-groups.
   *
   * @param input   input
   * @param geoList geos
   */
  public void setGeoGroups(WebElement input, List<Geo> geoList) {
    List<WebElement> selectedGeoList = input.findElements(propertyValueSelectedGeoGroups);
    setValuesToCombobox(selectedGeoList, input, geoList);
  }

  /**
   * Restoring the property and its value.
   *
   * @param propertyName property name
   * @param value        value
   * @param geos         geos
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage restorePropertyAndValues(String propertyName,
                                                    String value,
                                                    Geo... geos) {
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    if (isPresent(propertySelector)) {
      WebElement property = getDriver().findElement(propertySelector);
      List<WebElement> propertyValueList = property.findElements(propertyValuesSelector);
      if (propertyValueList.size() > 1) {
        IntStream.range(1, propertyValueList.size()).forEach(i -> {
          propertyValueList.get(i).findElement(propertyValueDeleteButtonSelector).click();
          submitDialog();
        });
      }
      if (propertyValueList.size() > 0) {
        modifyValueContinueWithPropertyPage(
            propertyName,
            value,
            geos);
      } else {
        PageFactory.initElements(getDriver(), WidgetSidebarPage.class)
            .createNewPropertyToWorkWith(propertyName)
            .createValue(propertyName, value, geos);
      }
    }
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Checking for a found entity marking.
   *
   * @param propertyName property name
   */
  public void checkPropertyMarking(String propertyName) {
    LOGGER.info(String.format("Проверяю, что проперти '%s' отмечена, как найденное", propertyName));
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    String actualColor = waitForElementBecomesClickable(
        getDriver().findElement(propertySelector).findElement(By.cssSelector("span")))
        .getCssValue("color");
    Assertions.assertThat(actualColor)
        .as("Проперти '{}' не отмечено, как найденное", propertyName)
        .contains("24, 144, 255");
  }

  /**
   * Read property value at {@code propertyName} property.
   *
   * @param propertyName propertyName
   * @return property value
   */
  public String readSingleValueFromProperty(String propertyName) {
    LOGGER.debug(String.format("Ищу проперти '%s'", propertyName));
    By propertySelector = By.xpath(String.format(propertySelectorXpath, propertyName));
    StringBuilder stringBuilder = new StringBuilder();
    LOGGER.debug("Собираю строку из вэлью");
    waitForElementBecomesClickable(getDriver().findElement(propertySelector))
        .findElements(propertyValueSpansSelector).forEach(e -> {
          String className = e.getAttribute("class");
          if (className.contains("whitespace")) {
            LOGGER.debug("Добавляю к строке: 'пробел'");
            stringBuilder.append(" ");
          } else if (className.contains("·")) {
            LOGGER.debug("Игнорирую разметку");
          } else {
            LOGGER.debug(String.format("Добавляю к строке: '%s'", e.getText()));
            stringBuilder.append(e.getText());
          }
        });
    String res = stringBuilder.toString().replaceAll("[\"]", "");
    LOGGER.debug(String.format("Собрана строка: '%s'", res));
    return res;
  }

  /**
   * Create property.
   *
   * @param title property title
   * @return this
   */
  public PropertyAndPropertyValuePage createNewProperty(String title) {
    LOGGER.info("Открываю дропдаун справочника пропсов");
    waitForElementBecomesClickable(newPropDropdownList).click();
    setValueToMonacoTextArea(title, getDriver().switchTo().activeElement());
    getDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
    LOGGER.info("Нажимаю кнопку 'Добавить'");
    waitForElementBecomesClickable(newPropAddSubmitButton).click();
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }
}
