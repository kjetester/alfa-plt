package ru.alfabank.platform.helpers;

import static java.time.Duration.ofSeconds;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverSingleton {

  private static WebDriver driver;

  /**
   * Getting driver.
   * @return driver
   */
  public static WebDriver getDriver() {
    if (driver == null) {
      WebDriverManager.chromedriver().setup();
      driver = new ChromeDriver();
    }
    return driver;
  }

  /**
   * Waiting for Element Becomes Visible.
   * @param element element
   * @param timeout timeout seconds
   */
  public static void waitForElementBecomesVisible(WebElement element, int timeout) {
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    new WebDriverWait(driver, ofSeconds(timeout)).until(ExpectedConditions.visibilityOf(element));
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
  }

  /**
   * Killing driver.
   */
  public static void killDriver() {
    driver.close();
    driver.quit();
    driver = null;
  }
}
