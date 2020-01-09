package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import io.qameta.allure.Step;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DatePickerPage extends BasePage {

  private static final String DATES_CSS_SELECTOR =
      ".ant-calendar-tbody td:not([class *= last-month]):not([class *= next-month]) div";

  // Year picker selectors
  @FindBy(css = ".ant-calendar-year-select")
  private WebElement yearPicker;
  @FindBy(css = ".ant-calendar-prev-year-btn")
  private WebElement minusYearButton;
  @FindBy(css = ".ant-calendar-next-year-btn")
  private WebElement plusYearButton;
  @FindBy(css = ".ant-calendar-month-select")
  // Month picker selectors
  private WebElement monthPicker;
  @FindBy(css = ".ant-calendar-prev-month-btn")
  private WebElement minusMonthButton;
  @FindBy(css = ".ant-calendar-next-month-btn")
  private WebElement plusMonthButton;
  // Time picker selectors
  @FindBy(css = ".ant-calendar-time-picker-btn[role = button]")
  private WebElement dataPickerChooseTimeButton;
  @FindBy(css = ".ant-calendar-time-picker-select:nth-child(1) > ul > li[role = button]")
  private List<WebElement> hourList;
  @FindBy(css = ".ant-calendar-time-picker-select:nth-child(2) > ul > li[role = button]")
  private List<WebElement> minuteList;
  @FindBy(css = ".ant-calendar-time-picker-select:nth-child(3) > ul > li[role = button]")
  private List<WebElement> secondList;
  @FindBy(css = ".ant-calendar-ok-btn")
  private WebElement dataPickerOkButton;

  /**
   * Setting the date and time.
   * @param calendar date and time to be set
   * @return new WidgetMetaInfoPage instance
   */
  @Step
  public WidgetMetaInfoPage setDateTo(Calendar calendar) throws InterruptedException {
    System.out.println(String.format("Setting the date to: '%s'", calendar.toString()));
    Thread.sleep(1000);
    while (calendar.get(Calendar.YEAR) != Integer.parseInt(yearPicker.getText())) {
      if (calendar.get(Calendar.YEAR) < Integer.parseInt(yearPicker.getText())) {
        minusYearButton.click();
      } else {
        plusYearButton.click();
      }
    }
    int month;
    switch (monthPicker.getText()) {
      case "янв." : month = 0;
      break;
      case "февр.": month = 1;
      break;
      case "март" : month = 2;
      break;
      case "апр." : month = 3;
      break;
      case "май"  : month = 4;
      break;
      case "июнь" : month = 5;
      break;
      case "июль" : month = 6;
      break;
      case "авг." : month = 7;
      break;
      case "сент.": month = 8;
      break;
      case "окт." : month = 9;
      break;
      case "нояб.": month = 10;
      break;
      default     : month = 11;
    }
    int diff = calendar.get(Calendar.MONTH) - month;
    if (diff > 0) {
      IntStream.range(0,diff).forEach(i -> plusMonthButton.click());
    } else if (diff < 0) {
      IntStream.range(0,diff).forEach(i -> minusMonthButton.click());
    }
    List<WebElement> dateList = getDriver().findElements(By.cssSelector(DATES_CSS_SELECTOR));
    dateList.get(calendar.get(Calendar.DATE) - 1).click();
    dataPickerChooseTimeButton.click();
    scrollToElement(hourList.get(calendar.get(Calendar.HOUR_OF_DAY))).click();
    scrollToElement(minuteList.get(calendar.get(Calendar.MINUTE))).click();
    scrollToElement(secondList.get(calendar.get(Calendar.SECOND))).click();
    dataPickerOkButton.click();
    System.out.println(String.format("Date and time have been set to: %s",
        new Timestamp(calendar.getTimeInMillis())));
    return PageFactory.initElements(getDriver(), WidgetMetaInfoPage.class);
  }
}
