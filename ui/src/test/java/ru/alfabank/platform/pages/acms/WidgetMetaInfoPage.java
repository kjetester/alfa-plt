package ru.alfabank.platform.pages.acms;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import java.util.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.reporting.BasicLogger.*;

public class WidgetMetaInfoPage extends BasePage {

  private static final String WIDGET_GEO_XPATH =
      "//*[text() = 'Тип устройства:']/../..//*[text() = 'Выберите нужные гео-группы']/..";
  //TODO: [class ^= city-groups-select-container]:first-of-type

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
   * @param isToBeSelected to be selected
   * @return this
   */
  public WidgetMetaInfoPage setVisibilityTo(boolean isToBeSelected) {
    waitForElementBecomesClickable(widgetGeoGroupsInput);
    boolean isAlreadySelected = visibilityCheckbox.isSelected();
    info(String.format("Setting the Visibility checkbox into '%b'", isToBeSelected));
    if (!isAlreadySelected && isToBeSelected) {
      visibilityCheckbox.click();
    } else if (isAlreadySelected && !isToBeSelected) {
      visibilityCheckbox.click();
    }
    return this;
  }

  /**
   * Opens the Start date picker.
   * @return new instance of DatePickerPage
   */
  public DatePickerPage openStartDatePicker() {
    info("Opening the Start date picker");
    waitForElementBecomesClickable(startDatePicker).click();
    return PageFactory.initElements(getDriver(), DatePickerPage.class);
  }

  /**
   * Opens the End date picker.
   * @return new instance of DatePickerPage
   */
  public DatePickerPage openEndDatePicker() {
    info("Opening the End date picker");
    waitForElementBecomesClickable(endDatePicker).click();
    return PageFactory.initElements(getDriver(), DatePickerPage.class);
  }

  /**
   * Setting geo-groups into a Widget.
   * @param geoGroups geo-groups
   * @return this
   */
  public WidgetMetaInfoPage setGeoGroupsToWidget(String... geoGroups) {
    info(String.format("Setting geo to '%s'", geoGroups));
    waitForElementBecomesClickable(widgetGeoGroupsInput);
    setGeoGroupsTo(widgetGeoGroupList, widgetGeoGroupsInput, geoGroups);
    return this;
  }

  /**
   * Collapsing a Widget Meta Info Pane.
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage collapseWidgetMetaInfo() {
    waitForElementBecomesClickable(widgetSettings).click();
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Collapsing a Widget Meta Info Pane.
   * @return WidgetSidebarPage
   */
  public PropertyAndPropertyValuePage collapseWidgetMetaInfoAnd() {
    info("Collapsing Widget's MetaInfo pane");
    waitForElementBecomesClickable(widgetSettings).click();
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }
}
