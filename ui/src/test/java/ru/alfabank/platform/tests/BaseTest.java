package ru.alfabank.platform.tests;

import static ru.alfabank.platform.helpers.DriverHelper.killDriver;
import static ru.alfabank.platform.helpers.UuidHelper.getShortRandUuid;

import com.epam.reportportal.annotations.ParameterKey;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.buisenessobjects.User;

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
    LOGGER.info(String.format("Окружение: '%s'", environment));
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
    LOGGER.info(String.format("Base URI: '%s'", baseUri));
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
