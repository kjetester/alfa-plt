package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WidgetMetaInfoPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(WidgetMetaInfoPage.class);
  private static final String WIDGET_GEO_XPATH =
      "//*[text() = 'Тип устройства:']/../..//*[text() = 'Выберите нужные гео-группы']/..";

  @FindBy(css = "[type *= 'checkbox']")
  private WebElement visibilityCheckbox;
  @FindBy(xpath = "//label[contains(., 'Дата начала')]//input")
  private WebElement startDatePicker;
  @FindBy(xpath = "//label[contains(., 'Дата окончания')]//input")
  private WebElement endDatePicker;
  @FindBy(xpath = WIDGET_GEO_XPATH)
  private WebElement widgetGeoGroupsInput;
  @FindBy(xpath = WIDGET_GEO_XPATH + "//li[@title]")
  private List<WebElement> widgetGeoGroupList;

  /**
   * Setting visibility of a Widget.
   *
   * @param isToBeSelected to be selected
   * @return this
   */
  public WidgetMetaInfoPage setVisibilityTo(boolean isToBeSelected) {
    waitForElementBecomesClickable(widgetGeoGroupsInput);
    setCheckboxTo(visibilityCheckbox, isToBeSelected);
    return this;
  }

  /**
   * Opens the Start date picker.
   *
   * @return new instance of DatePickerPage
   */
  public DatePickerPage openStartDatePicker() {
    LOGGER.info("Открываю датапикер 'Дата начала'");
    waitForElementBecomesClickable(startDatePicker).click();
    return PageFactory.initElements(getDriver(), DatePickerPage.class);
  }

  /**
   * Opens the End date picker.
   *
   * @return new instance of DatePickerPage
   */
  public DatePickerPage openEndDatePicker() {
    LOGGER.info("Открываю датапикер 'Дата окончания'");
    waitForElementBecomesClickable(endDatePicker).click();
    return PageFactory.initElements(getDriver(), DatePickerPage.class);
  }

  /**
   * Setting geo-groups into a Widget.
   *
   * @param geoList geo-groups
   * @return this
   */
  public WidgetMetaInfoPage setGeoGroupsToWidget(List<Object> geoList) {
    LOGGER.info("Устанавливаю гео-группы");
    geoList.forEach(geo -> {
      LOGGER.info("\n" + geo.toString());
    });
    waitForElementBecomesClickable(widgetGeoGroupsInput);
    setValuesToCombobox(widgetGeoGroupList, widgetGeoGroupsInput, geoList);
    return this;
  }

  /**
   * Collapsing a Widget Meta Info Pane.
   *
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage collapseWidgetMetaInfo() {
    waitForElementBecomesClickable(widgetSettings).click();
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Collapsing a Widget Meta Info Pane.
   *
   * @return WidgetSidebarPage
   */
  public PropertyAndPropertyValuePage collapseWidgetMetaInfoAnd() {
    LOGGER.info("Сворачиваю панель метаинфо виджета");
    waitForElementBecomesClickable(widgetSettings).click();
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }
}
