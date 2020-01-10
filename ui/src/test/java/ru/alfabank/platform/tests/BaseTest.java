package ru.alfabank.platform.tests;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.killDriver;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import ru.alfabank.platform.pages.acms.MainPage;

public class BaseTest {

  protected static final String   TEST_PAGE_URI = "sme-new";

  /**
   * Setting up driver.
   */
  @BeforeTest(description = "Setting up the Driver and navigate to The Main Page")
  public void setUp() {
    getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    getDriver().manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    getDriver().manage().window().setSize(new Dimension(1500, 800));
    getDriver().manage().window().setPosition(new Point(0, 0));
  }

  /**
   * Killing driver.
   */
  @AfterTest(alwaysRun = true)
  public void tearDown() {
    if (getDriver() != null) {
      killDriver();
    }
  }
}
