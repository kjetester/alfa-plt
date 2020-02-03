package ru.alfabank.platform.helpers;

import io.github.bonigarcia.wdm.*;
import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.time.temporal.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static java.time.Duration.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class DriverHelper {

  private static final Logger LOGGER = LogManager.getLogger(DriverHelper.class);
  private static final int TIMEOUT = 25;

  private static WebDriver driver;

  /**
   * Getting driver.
   * @return driver
   */
  public static WebDriver getDriver() {
    if (driver == null) {
      LOGGER.debug("Запускаю Chrome");
      WebDriverManager.chromedriver().setup();
      ChromeOptions opts = new ChromeOptions();
      opts.setAcceptInsecureCerts(true);
      driver = new ChromeDriver(opts);
      driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
      driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
      driver.manage().window().setSize(new Dimension(1500, 800));
      driver.manage().window().setPosition(new Point(0, 0));
    }
    return driver;
  }

  /**
   * Waiting for WebElement becomes visible.
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
   * @param element element
   */
  public static WebElement waitForElementBecomesClickable(WebElement element) {
    LOGGER.debug("Ожидаю кликабельности элемента");
    implicitlyWait(false);
    new WebDriverWait(driver, ofSeconds(TIMEOUT))
        .until(elementToBeClickable(element));
    implicitlyWait(true);
    return element;
  }

  /**
   * Turns implicit waitings off or on.
   * @param active boolean
   */
  public static void implicitlyWait(Boolean active) {
    if (active) {
      LOGGER.trace("Таймаут неявного ожидания = 5");
      driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
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
   * @param cities cities
   */
  public static void setCityCookieAndRefreshPage(String... cities) {
    LOGGER.debug("Сетаю кукисы");
    Date expireDate = java.sql.Date.from(
        java.time.ZonedDateTime.now().plus(3, ChronoUnit.HOURS).toInstant());
    driver.manage().deleteAllCookies();
    IntStream.range(0, cities.length).forEach(i -> {
      driver.manage().addCookie(new Cookie(
          "site_city", cities[i],"develop.ci.k8s.alfa.link", "/", expireDate, false, false));
      driver.manage().addCookie(new Cookie(
          "site_city", cities[i], ".develop.ci.k8s.alfa.link", "/", expireDate, false, false));
    });
    LOGGER.debug("Обновляю страницу");
    driver.navigate().refresh();
  }
}