package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.maven.surefire.shade.booter.org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.alfabank.platform.buisenessobjects.Page;

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
   * Filling and submitting the creation new Page form.
   * @param page new Page instance with id
   */
  public Page fillAndSubmitCreationForm(Page page) throws InterruptedException {
    LOGGER.info("Заполняю поля формы");
    LOGGER.info(String.format("Путь: '%s'", page.getUri()));
    waitForElementBecomesClickable(pathInput).sendKeys(page.getUri().replaceAll("^/", ""));
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
    closeSuccessBanner();
    new Actions(getDriver()).pause(1_000L).build().perform();
    return new Page.PageBuilder().using(page)
        .setId(Integer.parseInt(
            StringUtils.substringBetween(getDriver().getCurrentUrl(), "pages/", "/")))
        .build();
  }
}
