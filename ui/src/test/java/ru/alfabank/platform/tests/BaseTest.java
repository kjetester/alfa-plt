package ru.alfabank.platform.tests;

import com.epam.reportportal.annotations.*;
import org.apache.log4j.*;
import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.helpers.UUIDHelper.*;

public class BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

  protected static final User USER = new User();
  protected static final String TEST_WIDGET = "MetaTitle";
  protected static final String TEST_PROPERTY = "title";
  protected static String baseUri;

  protected String pageUrl = getShortRandUuid();
  protected Page basePage = new Page.PageBuilder()
      .setUri(pageUrl + "/")
      .setTitle("title_" + pageUrl)
      .setDescription("description_" + pageUrl)
      .setKeywords("keywords_" + pageUrl)
      .setEnable(true).build();
  protected String testProperty;
  protected String testPropertyValue;

  /**
   * Prepare environment before a test.
   * @param environment environment
   */
  @BeforeSuite
  @Parameters({"environment"})
  public void beforeSuite(
      @Optional("develop")
      @ParameterKey("environment")
      final String environment) {
    LOGGER.info(String.format("Base URI: '%s'", environment));
    switch (environment) {
      case "develop": baseUri = "http://develop.ci.k8s.alfa.link/";
        break;
      case "preprod": baseUri = "http://preprod.ci.k8s.alfa.link/";
        break;
      case "prod": baseUri = "https://alfabank.ru/";
        break;
      default: baseUri = "http://develop.ci.k8s.alfa.link/";
        break;
    }
  }

  /**
   * Begin test.
   * @param testContext test testContext
   */
  @BeforeTest
  public void beforeTest(final ITestContext testContext) {
    LOGGER.info(String.format("Начинаю тест '%s'", testContext.getName()));
  }

  /**
   * Tearing down after a test.
   * @param testContext test testContext
   */
  @AfterTest(alwaysRun = true)
  public void afterTest(ITestContext testContext) {
    LOGGER.info(
        String.format("Заканчиваю тест '%s'", testContext.getName()));
    killDriver();
  }
}
