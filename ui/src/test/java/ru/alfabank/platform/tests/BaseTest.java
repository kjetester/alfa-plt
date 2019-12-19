package ru.alfabank.platform.tests;

import static ru.alfabank.platform.helpers.DriverSingleton.getDriver;
import static ru.alfabank.platform.helpers.DriverSingleton.killDriver;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

  /**
   * Setting up driver.
   */
  @BeforeSuite
  public void setUp() {
    getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    getDriver().manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    getDriver().manage().window().setSize(new Dimension(1200, 800));
    getDriver().manage().window().setPosition(new Point(0, 0));
  }

  /**
   * Killing driver.
   */
  @AfterSuite
  public void tearDown() {
    if (getDriver() != null) {
      killDriver();
    }
  }
}
