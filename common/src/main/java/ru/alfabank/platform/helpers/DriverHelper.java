package ru.alfabank.platform.helpers;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.alfabank.platform.businessobjects.enums.Geo;

public class DriverHelper {

  private static final Logger LOGGER = LogManager.getLogger(DriverHelper.class);
  private static final int TIMEOUT = 60;

  private static RemoteWebDriver driver;

  /**
   * WebDriver singleton.
   *
   * @return driver
   */
  public static WebDriver getDriver() {
    if (driver == null) {
      LOGGER.debug("Запускаю Chrome");
      WebDriverManager.chromedriver().setup();
      ChromeOptions opts = new ChromeOptions();
      opts
          .addArguments("--headless", "--disable-gpu")
          .setAcceptInsecureCerts(true);
      driver = new ChromeDriver(opts);
      driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
      driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
      driver.manage().window().setSize(new Dimension(1500, 800));
      driver.manage().window().setPosition(new Point(0, 0));
    }
    return driver;
  }

  /**
   * Waiting for WebElement becomes visible.
   *
   * @param element element
   */
  public static void waitForElementBecomesVisible(WebElement element) {
    LOGGER.debug("Ожидаю отображения элемента");
    implicitlyWait(false);
    new WebDriverWait(driver, ofSeconds(TIMEOUT)).until(visibilityOf(element));
    implicitlyWait(true);
  }

  /**
   * Waiting for all WebElements becomes visible.
   *
   * @param elements elements
   */
  public static void waitForElementsBecomeVisible(List<WebElement> elements) {
    LOGGER.debug("Ожидаю отображения всего списка элементов");
    implicitlyWait(false);
    new WebDriverWait(driver, ofSeconds(TIMEOUT))
        .until(visibilityOfAllElements(elements));
    implicitlyWait(true);
  }

  /**
   * Waiting for WebElement becomes clickable.
   *
   * @param element element
   */
  public static WebElement waitForElementBecomesClickable(WebElement element) {
    implicitlyWait(false);
    LOGGER.debug("Ожидаю кликабельности элемента");
    new WebDriverWait(driver, ofSeconds(TIMEOUT))
        .until(elementToBeClickable(element));
    implicitlyWait(true);
    return element;
  }

  /**
   * Turns implicit waitings off or on.
   *
   * @param active boolean
   */
  public static void implicitlyWait(Boolean active) {
    if (active) {
      LOGGER.trace("Таймаут неявного ожидания = 5");
      driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    } else {
      LOGGER.trace("Таймаут неявного ожидания = 0");
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }
  }

  /**
   * Killing driver.
   */
  public static void killDriver() {
    if (driver != null) {
      LOGGER.debug("Закрываю браузер");
      driver.close();
      driver.quit();
      driver = null;
    }
  }

  /**
   * Setting Cookie.
   *
   * @param geoList cities
   */
  public static void setCityCookieAndRefreshPage(List<Geo> geoList) {
    LOGGER.debug("Сетаю кукисы");
    Date expireDate = java.sql.Date.from(
        java.time.ZonedDateTime.now().plus(3, ChronoUnit.HOURS).toInstant());
    driver.manage().deleteAllCookies();
    IntStream.range(0, geoList.size()).forEach(i -> {
      driver.manage().addCookie(new Cookie(
          "site_city",
          geoList.get(i).toString(),
          "develop.ci.k8s.alfa.link",
          "/",
          expireDate,
          false,
          false));
      driver.manage().addCookie(new Cookie(
          "site_city",
          geoList.get(i).toString(),
          ".develop.ci.k8s.alfa.link",
          "/",
          expireDate,
          false,
          false));
    });
    LOGGER.debug("Обновляю страницу");
    driver.navigate().refresh();
  }
}