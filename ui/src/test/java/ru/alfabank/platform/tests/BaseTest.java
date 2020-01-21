package ru.alfabank.platform.tests;

import org.testng.annotations.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class BaseTest {

  protected static final String TEST_PAGE_URI = "about/";

  /**
   * Setting up driver.
   */
  @BeforeTest(description = "Setting up the Driver")
  public void setUp() {

  }

  /**
   * Killing driver.
   */
  @AfterTest(alwaysRun = true)
  public void tearDown() {
    killDriver();
  }
}
