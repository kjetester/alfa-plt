package ru.alfabank.platform.tests;

import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.helpers.*;
import ru.alfabank.platform.reporting.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

@Listeners({CustomListener.class})
public class BaseTest {

  private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.LogManager.getLogger(BaseTest.class);

  protected String baseUrl;
  protected User user = new User();
  protected TestDataHelper testData = new TestDataHelper();

  /**
   * Prepare environment before a test.
   * @param context test context
   * @param environment environment
   */
  @BeforeTest(alwaysRun = true)
  @Parameters({"environment"})
  public void beforeTest(final ITestContext context, String environment) {
    LOGGER.info(String.format("Начинаю тест '%s'", context.getName()));
    switch (environment) {
      case "develop": this.baseUrl = "http://develop.ci.k8s.alfa.link/";
        break;
      case "preprod": this.baseUrl = "http://preprod.ci.k8s.alfa.link/";
        break;
      case "prod": this.baseUrl = "https://alfabank.ru/";
        break;
      default: this.baseUrl = "http://develop.ci.k8s.alfa.link/";
        break;
    }
    LOGGER.info(String.format("Энв: '%s'", environment));
  }

  /**
   * Tearing down after a test.
   * @param context test context
   */
  @AfterTest(alwaysRun = true)
  public void afterTest(ITestContext context) {
    LOGGER.info(
        String.format("Заканчиваю тест '%s'",context.getName()));
    killDriver();
  }
}
