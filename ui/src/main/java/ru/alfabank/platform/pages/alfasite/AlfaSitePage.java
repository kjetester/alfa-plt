package ru.alfabank.platform.pages.alfasite;

import org.apache.log4j.*;
import org.assertj.core.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import java.time.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class AlfaSitePage {

  private static final Logger LOGGER = LogManager.getLogger(AlfaSitePage.class);

  @FindBy(css = "li > p > span")
  private WebElement citySelectLink;
  @FindBy(css = "input[type='text']")
  private WebElement cityInputField;
  @FindBy(css = "div[alfa-portal-container] div > div li > p")
  private WebElement cityLink;
  @FindBy(id = "alfa")
  private WebElement mainBlock;

  /**
   * Opening AlfaSite page.
   * @param testPage test page URI
   * @return this
   */
  public AlfaSitePage open(String testPage) {
    LOGGER.info(String.format("Перехожу по адресу '%s'", testPage));
    getDriver().get(testPage);
    waitForElementBecomesVisible(mainBlock);
    return this;
  }

  /**
   * Assertion page title while widget is inactive.
   * @param city city
   * @return this
   */
  public AlfaSitePage checkPageTitleBefore(String... city) {
    LOGGER.info("Проверяю, что заголовок страницы пуст");
    setCityCookieAndRefreshPage(city);
    Assertions
        .assertThat(getDriver().getTitle())
        .isEqualTo("");
    return this;
  }

  /**
   * Assertion page titles according to time and geo-group.
   * @param deadline deadline when to start assertion
   * @param expectedTitle title to expect
   * @param city city where to expect
   * @return this
   * @throws InterruptedException InterruptedException
   */
  public AlfaSitePage checkPageTitleAfter(LocalDateTime deadline,
                                          String expectedTitle,
                                          String... city) throws InterruptedException {
    LOGGER.info(String.format(
        "Проверяю, что заголовок соответствует значению '%s'", expectedTitle));
    wait(deadline);
    boolean b = expectedTitle.equals(getDriver().getTitle());
    int count = 3;
    long start = Instant.now().getEpochSecond();
    while (!b && count > 0) {
      Thread.sleep(10_000);
      setCityCookieAndRefreshPage(city);
      String actualTitle = getDriver().getTitle();
      b = expectedTitle.equals(actualTitle);
      count--;
      LOGGER.debug(String.format(
          b ? "Успех" : "Текущий заголовок '%s' не соответствует ожидаемому '%s'."
              + "Попробую еще %d раз(а)", actualTitle, expectedTitle, count));
    }
    Assertions.assertThat(getDriver().getTitle()).isEqualTo(expectedTitle);
    LOGGER.info(String.format(
            "Ожидание корректного заголовка страницы заняло %d секунд(ы) с момета дедлайна",
        (Instant.now().getEpochSecond() - start)));
    return this;
  }

  /**
   * Waiting until deadline.
   * @param deadline deadline
   */
  public void wait(final LocalDateTime deadline) {
    while (LocalDateTime.now().isBefore(deadline)) {
      LOGGER.info(
          String.format("Жду '%d' секунд", deadline.compareTo(LocalDateTime.now())));
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
