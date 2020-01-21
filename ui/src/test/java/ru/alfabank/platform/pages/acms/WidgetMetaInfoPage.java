package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
  @Step
  public WidgetMetaInfoPage setVisibilityTo(boolean isToBeSelected) {
    waitForElementBecomesClickable(widgetGeoGroupsInput);
    boolean isAlreadySelected = visibilityCheckbox.isSelected();
    System.out.println(String.format("Setting the Visibility checkbox into '%b'", isToBeSelected));
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
  @Step
  public DatePickerPage openStartDatePicker() {
    System.out.println("Opening the Start date picker");
    waitForElementBecomesClickable(startDatePicker).click();
    return PageFactory.initElements(getDriver(), DatePickerPage.class);
  }

  /**
   * Opens the End date picker.
   * @return new instance of DatePickerPage
   */
  @Step
  public DatePickerPage openEndDatePicker() {
    System.out.println("Opening the End date picker");
    waitForElementBecomesClickable(endDatePicker).click();
    return PageFactory.initElements(getDriver(), DatePickerPage.class);
  }

  /**
   * Setting geo-groups into a Widget.
   * @param geoGroups geo-groups
   * @return this
   */
  @Step
  public WidgetMetaInfoPage setGeoGroupsToWidget(String... geoGroups) {
    System.out.println(String.format("Setting geo to '%s'", geoGroups));
    waitForElementBecomesClickable(widgetGeoGroupsInput);
    setGeoGroupsTo(widgetGeoGroupList, widgetGeoGroupsInput, geoGroups);
    return this;
  }

  /**
   * Collapsing a Widget Meta Info Pane.
   * @return WidgetSidebarPage
   */
  @Step
  public WidgetSidebarPage collapseWidgetMetaInfo() {
    waitForElementBecomesClickable(widgetSettings).click();
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Collapsing a Widget Meta Info Pane.
   * @return WidgetSidebarPage
   */
  @Step
  public PropertyAndPropertyValuePage collapseWidgetMetaInfoAnd() {
    System.out.println("Collapsing Widget's MetaInfo pane");
    waitForElementBecomesClickable(widgetSettings).click();
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }
}