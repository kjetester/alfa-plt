package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.maven.surefire.shade.booter.org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.TestNGException;
import ru.alfabank.platform.businessobjects.Page;

public class NewPageCreationPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(NewPageCreationPage.class);

  @FindBy(css = "#uri")
  private WebElement uriInput;
  @FindBy(css = "#title")
  private WebElement titleInput;
  @FindBy(css = "#description")
  private WebElement descriptionInput;
  @FindBy(name = "dateFrom")
  private WebElement startDate;
  @FindBy(name = "dateTo")
  private WebElement endDate;
  @FindBy(css = "#enable")
  private WebElement visibilityCheckbox;
  @FindBy(css = "button[type = 'submit']")
  private WebElement submitButton;
  @FindBy(css = "#teams")
  private WebElement teamsInput;
  @FindBy(css = ".ant-select-selection-item")
  private List<WebElement> selectedTeams;
  private By calClearButton = By.xpath("..//*[@aria-label = 'close-circle']");

  /**
   * Filling and submitting the creation new Page form.
   * @param page new Page instance with id
   */
  public Page fillAndSubmitCreationForm(Page page) {
    if (page.getUri() == null
        || page.getTitle() == null
        || page.isEnable() == null) {
      throw new TestNGException("Не указаны обязательные параметры страницы");
    }
    LOGGER.info(String.format("Заполняю поле 'URL страницы' значением '%s'", page.getUri()));
    clearInput(waitForElementBecomesClickable(uriInput))
        .sendKeys(page.getUri().replaceAll("^/", ""));
    LOGGER.info(String.format("Заполняю поле 'Название страницы' значением '%s'", page.getTitle()));
    clearInput(waitForElementBecomesClickable(titleInput)).sendKeys(page.getTitle());
    if (page.getDescription() != null) {
      LOGGER.info(String.format("Заполняю поле 'Описание' значением '%s'", page.getDescription()));
      clearInput(waitForElementBecomesClickable(descriptionInput)).sendKeys(page.getDescription());
    }
    if (page.getDateFrom() != null) {
      LOGGER.info("Заполняю поле 'Дата начала публикации':");
      startDate.click();
      PageFactory.initElements(getDriver(), DatePickerPage.class)
          .setDateTo(page.getLocalDateTimeFrom());
    } else {
      if (isPresent(startDate, calClearButton)) {
        LOGGER.info("Очищаю поле 'Дата начала публикации'");
        hoverAndClick(startDate.findElement(calClearButton));
      }
    }
    if (page.getDateTo() != null) {
      LOGGER.info("Заполняю поле 'Дата окончания публикации:");
      endDate.click();
      PageFactory.initElements(getDriver(), DatePickerPage.class)
          .setDateTo(page.getLocalDateTimeTo());
    } else {
      if (isPresent(endDate, calClearButton)) {
        LOGGER.info("Очищаю поле 'Дата окончания публикации'");
        hoverAndClick(endDate.findElement(calClearButton));
      }
    }
    if (page.getTeams() != null) {
      LOGGER.info("Заполняю поле 'Команды':");
      setValuesToCombobox(selectedTeams, teamsInput, page.getTeams());
    }
    LOGGER.info(String.format(
        "Устанавливаю чекбокс 'Видимость' в значение '%s'",
        page.isEnable().toString()));
    setCheckboxTo(visibilityCheckbox, page.isEnable());
    return submitCreationForm(page);
  }

  /**
   * Check pre filled fields on the Page Creation form.
   * @param page page
   * @return this
   */
  public NewPageCreationPage checkAutoFilledCreationForm(Page page) {
    LOGGER.info("Проверяю предзаполненные поля формы копирования страницы");
    final String uri = page.getUri().replaceAll("/$", "") + "-copy/";
    softly.assertThat(uriInput.getAttribute("value")).as("Поле 'Путь': '%s'", uri)
        .isEqualTo(uri);
    softly.assertThat(titleInput.getAttribute("value")).as("Поле 'Имя': '%s'", page.getTitle())
        .isEqualTo(page.getTitle());
    softly.assertThat(descriptionInput.getText()).as("Поле 'Описание': '%s'", page.getDescription())
        .isNullOrEmpty();
    softly.assertThat(startDate.getText()).as("Поле 'Дата начала': '%s'", page.getDateFrom())
        .isNullOrEmpty();
    softly.assertThat(startDate.getText()).as("Поле 'Дата окончания': '%s'", page.getDateTo())
        .isNullOrEmpty();
    softly.assertThat(visibilityCheckbox.isSelected()).as("Чекбокс 'Видимость' = '%b'", false)
        .isFalse();
    softly.assertAll();
    return this;
  }

  /**
   * Сабмит формы создания страницы.
   * Дополнение возвращаемого объекта старницы актуальными ID и URI.
   * @param page page
   * @return page
   */
  private Page submitCreationForm(Page page) {
    LOGGER.info("Сохраняю");
    waitForElementBecomesClickable(submitButton).click();
    LOGGER.info("Закрываю баннер успешного создания");
    closeSuccessBanner();
    new Actions(getDriver()).pause(1_000L).build().perform();
    return new Page.Builder().using(page)
        .setId(Integer.parseInt(
            StringUtils.substringBetween(getDriver().getCurrentUrl(), "pages/", "/")))
        .setUri(getDriver().findElement(By.cssSelector(String.format("a[href *= '%s']", page.getUri()))).getText())
        .build();
  }
}
