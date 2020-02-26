package ru.alfabank.platform.pages.acms;

import org.apache.log4j.*;
import org.apache.maven.surefire.shade.booter.org.apache.commons.lang3.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.*;
import ru.alfabank.platform.buisenessobjects.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class NewPageCreationPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(NewPageCreationPage.class);

  @FindBy(css = "#uri")
  private WebElement pathInput;
  @FindBy(css = "#title")
  private WebElement titleInput;
  @FindBy(css = "#description")
  private WebElement descriptionInput;
  @FindBy(css = "#dateFrom")
  private WebElement startDate;
  @FindBy(css = "#dateTo")
  private WebElement endDate;
  @FindBy(css = "#enable")
  private WebElement visabilityCheckbox;
  @FindBy(css = "button[type = 'submit']")
  private WebElement submitButton;
  private By calClearBttn = By.cssSelector("[aria-label $= 'close-circle']");

  /**
   * Filling and submitting the creation new page form.
   * @param page page instance with id
   */
  public Page fillAndSubmitCreationForm(Page page) throws InterruptedException {
    LOGGER.info("Заполняю поля формы");
    //TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-18912)
    LOGGER.info(String.format("Путь: '%s'", page.getUri()));
    waitForElementBecomesClickable(pathInput).sendKeys(page.getUri());
    LOGGER.info(String.format("Название: '%s'", page.getTitle()));
    titleInput.sendKeys(page.getTitle());
    LOGGER.info(String.format("Описание: '%s'", page.getDescription()));
    descriptionInput.sendKeys(page.getDescription());
    if (page.getDateFrom() != null) {
      LOGGER.info("Дата начала отображения:");
      startDate.click();
      PageFactory.initElements(getDriver(), DatePickerPage.class)
          .setDateTo(page.getLocalDateTimeFrom());
    } else {
      LOGGER.info("Очищаю поле даты начала отображения");
      new Actions(getDriver())
          .moveToElement(startDate.findElement(calClearBttn))
          .pause(500L)
          .click()
          .build()
          .perform();
    }
    if (page.getDateTo() != null) {
      LOGGER.info("Дата окончания отображения:");
      endDate.click();
      PageFactory.initElements(getDriver(), DatePickerPage.class)
          .setDateTo(page.getLocalDateTimeTo());
    }
    setCheckboxTo(visabilityCheckbox, page.isEnable());
    LOGGER.info("Сохраняю");
    waitForElementBecomesClickable(submitButton).click();
    LOGGER.info("Закрываю баннер успешного создания");
    waitForElementBecomesClickable(bannerCloseBttn).click();
    new Actions(getDriver()).pause(1_000L).build().perform();
    return new Page.PageBuilder().using(page)
        .setId(Integer.parseInt(
            StringUtils.substringBetween(getDriver().getCurrentUrl(), "pages/", "/")))
        .build();
  }
}
