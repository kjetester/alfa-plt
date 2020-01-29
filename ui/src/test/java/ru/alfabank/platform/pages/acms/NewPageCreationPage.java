package ru.alfabank.platform.pages.acms;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.helpers.*;

import java.time.*;
import java.time.temporal.*;
import java.util.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class NewPageCreationPage extends BasePage {

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
  @FindBy(css = "button[type = 'submit']")
  private WebElement submitButton;

  /**
   * Filling and submitting the creation new page form.
   * @param testDataHelper testDataHelper instance
   */
  public void fillAndSubmitCreationForm(TestDataHelper testDataHelper) throws InterruptedException {
    String path = UUID.randomUUID().toString().substring(24).toLowerCase();
    String title = "title_" + path;
    String description = "description_" + path;
    LocalDateTime dateFrom = LocalDateTime.now().minus(0, ChronoUnit.HOURS);
    LocalDateTime dateTo = LocalDateTime.now().plus(5, ChronoUnit.HOURS);
    waitForElementBecomesClickable(pathInput).sendKeys(path);
    titleInput.sendKeys(title);
    descriptionInput.sendKeys(description);
    startDate.click();
    PageFactory.initElements(getDriver(), DatePickerPage.class).setDateTo(dateFrom);
    endDate.click();
    PageFactory.initElements(getDriver(), DatePickerPage.class).setDateTo(dateTo);
    waitForElementBecomesClickable(submitButton).click();
    waitForElementBecomesClickable(bannerCloseBttn).click();
    testDataHelper.setCreatedPage(new Page(path, getDriver().getCurrentUrl(), title,
        description, "keyword_" + path, dateFrom, dateTo));
  }
}
