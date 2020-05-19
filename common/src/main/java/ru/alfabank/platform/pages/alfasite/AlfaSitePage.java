package ru.alfabank.platform.pages.alfasite;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.setCityCookieAndRefreshPage;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesVisible;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.alfabank.platform.businessobjects.enums.Geo;

public class AlfaSitePage {

  private static final Logger LOGGER = LogManager.getLogger(AlfaSitePage.class);

  @FindBy(css = ".site-assr")
  private WebElement site;
  @FindBy(id = "alfa")
  private WebElement mainBlock;

  /**
   * Open AlfaSite page.
   *
   * @param testPage test page URI
   * @return this
   */
  public AlfaSitePage open(String testPage) {
    LOGGER.info(String.format("Перехожу по адресу '%s'", testPage));
    getDriver().get(testPage);
    waitForElementBecomesVisible(site);
    return this;
  }

  /**
   * Assertion page title while widget is inactive.
   *
   * @param geos cities
   * @return this
   */
  public AlfaSitePage checkPageTitleBefore(Geo... geos) {
    LOGGER.info("Проверяю, что заголовок страницы пуст");
    setCityCookieAndRefreshPage(Arrays.asList(geos));
    Assertions
        .assertThat(getDriver().getTitle())
        .isEqualTo("");
    return this;
  }

  /**
   * Assertion page titles according to time and geo-group.
   *
   * @param deadline      deadline when to start assertion
   * @param expectedTitle title to expect
   * @param geos          city where to expect
   * @return this
   * @throws InterruptedException InterruptedException
   */
  public AlfaSitePage checkPageTitleAfter(LocalDateTime deadline,
                                          String expectedTitle,
                                          Geo... geos) throws InterruptedException {
    LOGGER.info(String.format(
        "Проверяю, что заголовок соответствует значению '%s'", expectedTitle));
    wait(deadline);
    boolean isTitleCorrect = expectedTitle.equals(getDriver().getTitle());
    int count = 3;
    long start = Instant.now().getEpochSecond();
    List<Geo> geoList = Arrays.asList(geos);
    while (!isTitleCorrect && count > 0) {
      TimeUnit.SECONDS.sleep(15);
      setCityCookieAndRefreshPage(geoList);
      String actualTitle = getDriver().getTitle();
      isTitleCorrect = expectedTitle.equals(actualTitle);
      count--;
      LOGGER.debug(String.format(
          isTitleCorrect ? "Успех" : "Текущий заголовок '%s' не соответствует ожидаемому '%s'."
              + "Попробую еще %d раз(а)", actualTitle, expectedTitle, count));
    }
    Assertions.assertThat(getDriver().getTitle()).isEqualTo(expectedTitle);
    LOGGER.info(String.format(
        "Ожидание корректного заголовка страницы заняло %d секунд(ы) с момета дедлайна",
        (Instant.now().getEpochSecond() - start)));
    return this;
  }

  /**
   * Wait until deadline.
   *
   * @param deadline deadline
   */
  public void wait(final LocalDateTime deadline) {
    while (LocalDateTime.now().isBefore(deadline)) {
      LOGGER.info(
          String.format("Жду '%d' секунд", deadline.compareTo(LocalDateTime.now())));
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Check page url.
   *
   * @param path page path
   * @return this
   */
  public AlfaSitePage compareUrl(String path) {
    Assertions
        .assertThat(getDriver().getCurrentUrl())
        .as("Проверка, туда ли перешли")
        .contains(path);
    return this;
  }
}
