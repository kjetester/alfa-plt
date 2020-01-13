package ru.alfabank.platform.pages.alfasite;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.setCityCookieAndRefreshPage;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesVisible;

import io.qameta.allure.Step;
import java.time.Instant;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AlfaSitePage {

  private static final String BASE_PATH = "http://develop.ci.k8s.alfa.link/";

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
   * @param resource resource
   * @return this
   */
  @Step
  public AlfaSitePage open(String resource) {
    String url = BASE_PATH + resource;
    System.out.println(String.format("Navigating to '%s'", url));
    getDriver().get(url);
    waitForElementBecomesVisible(mainBlock);
    return this;
  }

  /**
   * Assertion page title while widget is inactive.
   * @param city city
   * @return this
   */
  @Step
  public AlfaSitePage checkPageTitleBefore(String... city) {
    System.out.println("Start checking title is empty");
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
  @Step
  public AlfaSitePage checkPageTitleAfter(LocalDateTime deadline,
                                          String expectedTitle,
                                          String... city) throws InterruptedException {
    System.out.println(String.format("Start checking title is '%s'", expectedTitle));
    wait(deadline);
    boolean b = expectedTitle.equals(getDriver().getTitle());
    int count = 10;
    System.out.println(b ? "SUCCESS" : "Title doesn't match. Will try " + count + "times more");
    long start = Instant.now().getEpochSecond();
    while (!b && count > 0) {
      Thread.sleep(5_000);
      setCityCookieAndRefreshPage(city);
      b = expectedTitle.equals(getDriver().getTitle());
      count--;
      System.out.println(b ? "SUCCESS" : "Title doesn't match. Will try " + count + "times more");
    }
    Assertions
        .assertThat(getDriver().getTitle())
        .as("Title is incorrect")
        .isEqualTo(expectedTitle);
    System.out.println(String.format(
        "It takes %d seconds since the deadline to get it success",
        (Instant.now().getEpochSecond() - start)));
    return this;
  }

  /**
   * Waiting until deadline.
   * @param deadline deadline
   */
  @Step
  public void wait(final LocalDateTime deadline) {
    while (LocalDateTime.now().isBefore(deadline)) {
      System.out.println(String.format("Waiting for %d secs",
          deadline.compareTo(LocalDateTime.now())));
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
