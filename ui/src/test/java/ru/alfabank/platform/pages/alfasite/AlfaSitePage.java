package ru.alfabank.platform.pages.alfasite;

import org.assertj.core.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.slf4j.*;

import java.time.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;


public class AlfaSitePage {

  private static final Logger LOGGER = LoggerFactory.getLogger(AlfaSitePage.class);

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
    LOGGER.info(String.format("Navigating to '%s'", testPage));
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
    LOGGER.info("Start checking the page title is empty");
    setCityCookieAndRefreshPage(city);
    Assertions
        .assertThat(getDriver().getTitle())
        .as("Checking title before")
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
    LOGGER.info("Start checking title is '{}'", expectedTitle);
    wait(deadline);
    boolean b = expectedTitle.equals(getDriver().getTitle());
    int count = 10;
    long start = Instant.now().getEpochSecond();
    while (!b && count > 0) {
      Thread.sleep(5_000);
      setCityCookieAndRefreshPage(city);
      b = expectedTitle.equals(getDriver().getTitle());
      count--;
      LOGGER.debug(b ? "SUCCESS" : "Title doesn't match. Will try {} times more", count);
    }
    Assertions
        .assertThat(getDriver().getTitle())
        .as("Title is incorrect")
        .isEqualTo(expectedTitle);
    LOGGER.info("It takes {} seconds since the deadline to get it success", (Instant.now().getEpochSecond() - start));
    return this;
  }

  /**
   * Waiting until deadline.
   * @param deadline deadline
   */
  public void wait(final LocalDateTime deadline) {
    while (LocalDateTime.now().isBefore(deadline)) {
      LOGGER.info("Waiting for {} secs", deadline.compareTo(LocalDateTime.now()));
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
