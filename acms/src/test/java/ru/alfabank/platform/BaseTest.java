package ru.alfabank.platform;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL;
import static ru.alfabank.platform.businessobjects.enums.Team.SME;
import static ru.alfabank.platform.helpers.UuidHelper.getShortRandUuid;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.users.AccessibleUser;

public class BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);
  private static final String URL_ENDING = ".ci.k8s.alfa.link/";
  protected static final AccessibleUser USER = getContentManager();
  protected static final String TEST_WIDGET = "MetaTitle";
  protected static final String TEST_PROPERTY = "title";
  protected static final String PAGE_CONTROLLER_BASE_PATH =
      "api/v1/content-store/admin-panel/pages/";
  protected static final String PAGE_DRAFT_CONTROLLER_BASE_PATH =
      "api/v1/content-store/admin-panel/pages/{pageId}/drafts";
  protected static final String PAGE_DRAFT_CONTROLLER_PUBLISH_BASE_PATH =
      "api/v1/content-store/admin-panel/pages/{pageId}/drafts/execute";
  public static final String CONTENT_PAGE_CONTROLLER_BASE_PATH =
      "/api/v1/content-store/page-contents";
  private static final String META_INFO_CONTENT_PAGE_CONTROLLER =
      "/api/v1/content-store/admin-panel/meta-info-page-contents";

  protected static Page basePage;
  protected static Page sourcePage;
  protected static RequestSpecification baseSpec;
  protected static RequestSpecification contentPageControllerSpec;
  protected static RequestSpecification pageControllerSpec;
  protected static RequestSpecification pageDraftControllerSpec;
  protected static RequestSpecification metaInfoContentPageControllerSpec;
  protected static RequestSpecification pageDraftControllerPublishSpec;
  protected static String baseUri;
  protected static String testProperty;
  protected static String testPropertyValue;

  /**
   * Prepare environment before a test.
   * @param environment environment
   */
  @BeforeSuite(
      description = "Выполенние предусловий:\n"
          + "\t1. Установка тествой среды\n"
          + "\t2. Создание объекта страницы-донора\n"
          + "\t5. Настройка базовой конфигурации HTTP запросов\n"
          + "\t6. Настройка конфигурации HTTP запросов к page-controller\n"
          + "\t7. Настройка конфигурации HTTP запросов к content-page-controller\n"
          + "\t8. Настройка конфигурации HTTP запросов к page-draft-controller",
      alwaysRun = true)
  @Parameters({"environment"})
  public void beforeSuite(
      @Optional("develop")
      @ParameterKey("environment")
      final String environment) {
    setUpEnvironment(environment);
    setUpSourcePage();
    setUpBaseRequestConfiguration();
    setUpPageControllerRequestConfiguration();
    setUpContentPageControllerRequestConfiguration();
    setUpMetaInfoContentPageControllerRequestConfiguration();
    setUpPageDraftControllerRequestConfiguration();
  }

  @BeforeMethod(
      description = "Выполенние предусловий:\n"
          + "\t3. Создание базового объекта создоваемой страницы\n")
  public void beforeCreationPageMethods() {
    setUpBasePage();
  }

  /**
   * Set up environment.
   * @param environment environment
   */
  private void setUpEnvironment(
      @ParameterKey("environment")
      @Optional("develop") String environment) {
    LOGGER.info(String.format("Тестовая среда - '%s'", environment));

    switch (environment) {
      case "develop": {
        baseUri = String.format("http://develop%s", URL_ENDING);
        break;
      }
      case "preprod": {
        baseUri = String.format("http://preprod%s", URL_ENDING);
        break;
      }
      case "feature": {
        baseUri = String.format(
            "http://acms-feature-alfabankru-%s.alfabankru-reviews%s", System.getProperty("feature"), URL_ENDING);
        break;
      }
      default: {
        baseUri = "http://develop" + URL_ENDING;
        break;
      }
    }
    LOGGER.info(String.format("URI '%s' установлен в качестве базового", baseUri));
  }

  /**
   * Set up the Source Page.
   */
  private void setUpSourcePage() {
    LOGGER.info("Собираю pojo страницы-источника");
    sourcePage = new Page.Builder()
        .setUri("/about/")
        .setId(10)
        .setTitle("null")
        .setDescription("null")
        .setDateFrom(null)
        .setDateTo(null)
        .setEnable(true)
        .setChildUids(null)
        .build();
    LOGGER.info(String.format("Страница-источник:\n\t %s", sourcePage.toString()));
  }

  /**
   * Set up the Base Page.
   */
  private void setUpBasePage() {
    String pageUrl = getShortRandUuid();
    LOGGER.info("Собираю POJO базы новой страницы");
    basePage = new Page.Builder()
        .setUri(pageUrl)
        .setTitle("title_" + pageUrl)
        .setDescription("description_" + pageUrl)
        .setDateFrom(LocalDateTime.now().toString())
        .setDateTo(LocalDateTime.now().plusMinutes(30).toString())
        .setTeamsList(List.of(
                 SME,
                 COMMON,
                 CREDIT_CARD,
                 DEBIT_CARD,
                 INVEST,
                 MORTGAGE,
                 PIL))
        .setEnable(true)
        .build();
    LOGGER.info(String.format("Скелет новой страницы:\n\t %s", basePage.toString()));
  }

  /**
   * Set up base request configuration.
   */
  private void setUpBaseRequestConfiguration() {
    LOGGER.info("Устанавливаю базовую конфгурацию HTTP запросов");
    RestAssured.defaultParser = Parser.JSON;
    baseSpec = new RequestSpecBuilder()
        .log(LogDetail.ALL)
        .setRelaxedHTTPSValidation()
        .setBaseUri(
            System.getProperty("feature") == null
                ? baseUri
                : String.format("http://develop%s", URL_ENDING))
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .build();
  }

  /**
   * Set up page-controller request configuration.
   */
  private void setUpPageControllerRequestConfiguration() {
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-controller");
    pageControllerSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(PAGE_CONTROLLER_BASE_PATH)
        .build();
  }

  /**
   * Set up content-page-controller request configuration.
   */
  private void setUpContentPageControllerRequestConfiguration() {
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /content-page-controller");
    contentPageControllerSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(CONTENT_PAGE_CONTROLLER_BASE_PATH)
        .addQueryParam("city_uid", "21")
        .addQueryParam("device", desktop)
        .build();
  }

  /**
   * Set up meta-info-content-page-controller request configuration.
   */
  private void setUpMetaInfoContentPageControllerRequestConfiguration() {
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /meta-info-content-page-controller");
    metaInfoContentPageControllerSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(META_INFO_CONTENT_PAGE_CONTROLLER)
        .addQueryParam("device", desktop)
        .build();
  }

  /**
   * Set up page-draft-controller request configuration.
   */
  private void setUpPageDraftControllerRequestConfiguration() {
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-draft-controller");
    pageDraftControllerSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(PAGE_DRAFT_CONTROLLER_BASE_PATH)
        .build();
    pageDraftControllerPublishSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(PAGE_DRAFT_CONTROLLER_PUBLISH_BASE_PATH)
        .build();
  }
}
