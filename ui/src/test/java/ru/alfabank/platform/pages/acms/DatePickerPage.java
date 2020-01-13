package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import io.qameta.allure.Step;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.TestException;

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
  @FindBy(css = ".ant-calendar-input")
  private WebElement dateInput;

  /**
   * Setting the date and time.
   * @param dateTime date and time to be set
   * @return new WidgetMetaInfoPage instance
   */
  @Step
  public WidgetMetaInfoPage setDateTo(LocalDateTime dateTime) throws InterruptedException {
    System.out.println(String.format("Setting the date to: '%s'",
        dateTime.toString()));
    Thread.sleep(1000);
    while (dateTime.getYear() != Integer.parseInt(yearPicker.getText())) {
      if (dateTime.getYear() < Integer.parseInt(yearPicker.getText())) {
        minusYearButton.click();
      } else {
        plusYearButton.click();
      }
    }
    int month;
    switch (monthPicker.getText()) {
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
      default: throw new TestException("Unknown month");
    }
    int diff = dateTime.getMonthValue() - month;
    if (diff > 0) {
      IntStream.range(0,diff).forEach(i -> plusMonthButton.click());
    } else if (diff < 0) {
      IntStream.range(0,-diff).forEach(i -> minusMonthButton.click());
    }
    List<WebElement> dateList = getDriver().findElements(By.cssSelector(DATES_CSS_SELECTOR));
    dateList.get(dateTime.getDayOfMonth() - 1).click();
    dataPickerChooseTimeButton.click();
    scrollToElement(hourList.get(dateTime.getHour())).click();
    scrollToElement(minuteList.get(dateTime.getMinute())).click();
    scrollToElement(secondList.get(dateTime.getSecond())).click();
    System.out.println(String.format("Date and time have been set to: %s",
        dateInput.getAttribute("value")));
    dataPickerOkButton.click();
    return PageFactory.initElements(getDriver(), WidgetMetaInfoPage.class);
  }
}
