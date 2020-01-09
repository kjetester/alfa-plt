package ru.alfabank.platform.helpers;

import static java.time.Duration.ofSeconds;
import static ru.alfabank.platform.helpers.DateHelper.getNowPlus;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverHelper {

  private static WebDriver driver;
  private static final int TIMEOUT = 25;

  /**
   * Getting driver.
   * @return driver
   */
  public static WebDriver getDriver() {
    if (driver == null) {
      WebDriverManager.chromedriver().setup();
      ChromeOptions opts = new ChromeOptions();
      opts.setAcceptInsecureCerts(true);
      driver = new ChromeDriver(opts);
    }
    return driver;
  }

  /**
   * Waiting for WebElement becomes visible.
   * @param element element
   */
  public static void waitForElementBecomesVisible(WebElement element) {
    implicitlyWait(false);
    new WebDriverWait(driver, ofSeconds(TIMEOUT)).until(ExpectedConditions.visibilityOf(element));
    implicitlyWait(true);
  }

  /**
   * Waiting for all WebElements becomes visible.
   * @param elements elements
   */
  public static void waitForElementsBecomeVisible(List<WebElement> elements) {
    implicitlyWait(false);
    new WebDriverWait(driver, ofSeconds(TIMEOUT))
        .until(ExpectedConditions.visibilityOfAllElements(elements));
    implicitlyWait(true);
  }

  /**
   * Waiting for WebElement becomes clickable.
   * @param element element
   */
  public static WebElement waitForElementBecomesClickable(WebElement element) {
    implicitlyWait(false);
    new WebDriverWait(driver, ofSeconds(TIMEOUT))
        .until(ExpectedConditions.elementToBeClickable(element));
    implicitlyWait(true);
    return element;
  }

  /**
   * Waiting for Element Becomes invisible.
   * @param element element
   *
   */
  public static void waitForElementBecomesInvisible(WebElement element) {
    implicitlyWait(false);
    new WebDriverWait(driver, ofSeconds(TIMEOUT)).until(ExpectedConditions.invisibilityOf(element));
    implicitlyWait(true);
  }

  /**
   * Turns implicit waitings off or on.
   * @param active boolean
   */
  public static void implicitlyWait(Boolean active) {
    if (active) {
      driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    } else {
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }
  }

  /**
   * Killing driver.
   */
  public static void killDriver() {
    driver.close();
    driver.quit();
    driver = null;
  }

  /**
   * Setting Cookie.
   * @param cities cities
   */
  public static void setCityCookieAndRefreshPage(String... cities) {
    driver.manage().deleteAllCookies();
    IntStream.range(0, cities.length).forEach(i -> {
      driver.manage().addCookie(new Cookie(
          "site_city", cities[i],"develop.ci.k8s.alfa.link", "/",
          getNowPlus(3, Calendar.HOUR).getTime(), false, false));
      driver.manage().addCookie(new Cookie(
          "site_city", cities[i], ".develop.ci.k8s.alfa.link", "/",
          getNowPlus(3, Calendar.HOUR).getTime(), false, false));
    });
    driver.navigate().refresh();
  }
}
