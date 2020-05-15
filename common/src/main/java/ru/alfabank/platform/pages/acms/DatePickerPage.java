package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.TestException;

public class DatePickerPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(DatePickerPage.class);
  private static final String VISIBLE_DATE_PICKER =
      "[class ^= ant-picker-dropdown]:not([class $= hidden]) ";
  private static final String DATES_CSS_SELECTOR = VISIBLE_DATE_PICKER
      + ".ant-picker-body td:not([class = ant-picker-cell]) div";

  // Year picker selectors
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-year-btn")
  private WebElement yearPickerButton;
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-header-super-prev-btn")
  private WebElement minusYearButton;
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-header-super-next-btn")
  private WebElement plusYearButton;
  // Month picker selectors
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-month-btn")
  private WebElement monthPickerButton;
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-header-prev-btn")
  private WebElement minusMonthButton;
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-header-next-btn")
  private WebElement plusMonthButton;
  // Time picker selectors
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-time-panel-column:nth-child(1) li")
  private List<WebElement> hourChooseButtonsList;
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-time-panel-column:nth-child(2) li")
  private List<WebElement> minuteChooseButtonsList;
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-time-panel-column:nth-child(3) li")
  private List<WebElement> secondChooseButtonsList;
  @FindBy(css = VISIBLE_DATE_PICKER
      + ".ant-picker-ok button")
  private WebElement dataPickerSubmitButton;
  @FindBy(css = ".ant-picker-time-panel .ant-picker-header-view")
  private WebElement dateInput;

  /**
   * Setting the date and time.
   * @param dateTime date and time to be set
   * @return new WidgetMetaInfoPage instance
   */
  public WidgetMetaInfoPage setDateTo(LocalDateTime dateTime) {
    LOGGER.info(dateTime.toString());
    waitForElementBecomesClickable(yearPickerButton);
    while (dateTime.getYear() != Integer.parseInt(yearPickerButton.getText())) {
      if (dateTime.getYear() < Integer.parseInt(yearPickerButton.getText())) {
        minusYearButton.click();
      } else {
        plusYearButton.click();
      }
    }
    int month;
    waitForElementBecomesClickable(monthPickerButton);
    switch (monthPickerButton.getText()) {
      case "янв." : month = 1;
        break;
      case "февр.": month = 2;
        break;
      case "март" : month = 3;
        break;
      case "апр." : month = 4;
        break;
      case "май"  : month = 5;
        break;
      case "июнь" : month = 6;
        break;
      case "июль" : month = 7;
        break;
      case "авг." : month = 8;
        break;
      case "сент.": month = 9;
        break;
      case "окт." : month = 10;
        break;
      case "нояб.": month = 11;
        break;
      case "дек." : month = 12;
        break;
      default: throw new TestException("Был передеан неизвестный месяц");
    }
    int diff = dateTime.getMonthValue() - month;
    if (diff > 0) {
      IntStream.range(0, diff).forEach(i -> plusMonthButton.click());
    } else if (diff < 0) {
      IntStream.range(0, -diff).forEach(i -> minusMonthButton.click());
    }
    List<WebElement> dateList = getDriver().findElements(By.cssSelector(DATES_CSS_SELECTOR));
    dateList.get(dateTime.getDayOfMonth() - 1).click();
    scrollToElement(hourChooseButtonsList.get(dateTime.getHour())).click();
    scrollToElement(minuteChooseButtonsList.get(dateTime.getMinute())).click();
    scrollToElement(secondChooseButtonsList.get(dateTime.getSecond())).click();
    LOGGER.debug(String.format("Дата и время установлено как: '%s'", dateInput.getText()));
    dataPickerSubmitButton.click();
    return PageFactory.initElements(getDriver(), WidgetMetaInfoPage.class);
  }
}
