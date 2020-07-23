package ru.alfabank.platform.steps;

import static io.restassured.parsing.Parser.JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.abtests.Experiment;
import ru.alfabank.platform.businessobjects.contentstore.Page;
import ru.alfabank.platform.businessobjects.geofacade.GeoGroup;
import ru.alfabank.platform.users.AccessibleUser;

public class BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(BaseSteps.class);

  // Base
  private static String CS_BASE_URL;
  private static String AB_TEST_BASE_URL;
  private static String GEO_FACADE_BASE_URL;
  private static String CITIES_BASE_URL;
  private static String METRO_BASE_URL;
  private static String FEEDBACK_BASE_URL;
  private static String OFFICES_BASE_URL;
  private static String SHORT_URL_BASE_URL;
  private static final String URL_ENDING = ".ci.k8s.alfa.link/";
  private static final String PREFIX = "api/v1";
  // Content-Store paths
  private static String contentStoreBasePath;
  private static String pageBasePath;
  private static String pageIdBasePath;
  private static String pageIdCopyBasePath;
  private static String pageContentsBasePath;
  private static String pageContentsNoCacheBasePath;
  private static String metaInfoContentPageBasePath;
  private static String draftBasePath;
  private static String draftExecBasePath;
  private static String auditBasePath;
  private static String auditRollbackBasePath;
  // AB-Testing paths
  private static String abTestsBasePath;
  private static String getAllOrCreateExperimentPath;
  private static String getDeletePatchExperimentByUuidPath;
  private static String getAllOrCreateOptionPath;
  private static String getDeletePatchOptionPath;
  private static String involvementsUri;
  // GEO-facade paths
  private static String geoFacadeBasePath;
  private static String geoFacadeGeoGroupsPath;
  private static String geoFacadeGeoGroupsIdPath;
  private static String geoFacadeGeoGroupsIdCitiesPath;
  private static String geoFacadeGeoGroupsCitiesPath;
  private static String geoFacadeCityPath;
  // Cities paths
  private static String citiesBasePath;
  private static String cityPath;
  // Metro paths
  private static String metroBasePath;
  // Feedback paths
  private static String feedbackBasePath;
  private static String feedbackAdditionPath;
  // Offices paths
  private static String officesBasePath;
  private static String officesImportPath;
  private static String officesInPath;
  private static String officesErrPath;
  // Short Url paths
  private static String shortUrlBasePath;
  private static String shortUrlCreatePath;
  private static String shortUrlCreateArrayPath;
  private static String shortUrlReadPath;
  private static String shortUrlUpdateDeletePath;

  // Content-Store request specifications
  private static final RequestSpecification BASE_CS_SPEC;
  private static final RequestSpecification PAGE_SPEC;
  private static final RequestSpecification PAGE_ID_SPEC;
  private static final RequestSpecification PAGE_ID_COPY_SPEC;
  private static final RequestSpecification PAGE_CONTENTS_SPEC;
  private static final RequestSpecification PAGE_CONTENTS_NO_CACHE_SPEC;
  private static final RequestSpecification META_INFO_CONTENT_PAGE_SPEC;
  private static final RequestSpecification DRAFT_SPEC;
  private static final RequestSpecification DRAFT_EXEC_SPEC;
  private static final RequestSpecification AUDIT_TRANSACTIONS_SPEC;
  private static final RequestSpecification AUDIT_TRANSACTION_SPEC;
  private static final RequestSpecification AUDIT_ROLLBACK_TRANSACTIONS_SPEC;
  // AB-Testing request specifications
  private static final RequestSpecification BASE_AB_TEST_SPEC;
  private static final RequestSpecification GET_ALL_OR_CREATE_EXPERIMENT_SPEC;
  private static final RequestSpecification GET_DELETE_PATCH_EXPERIMENT_BY_UUID_SPEC;
  private static final RequestSpecification INVOLVEMENTS_SPEC;
  private static final RequestSpecification GET_ALL_OR_DELETE_OPTION_SPEC;
  private static final RequestSpecification GET_DELETE_PATCH_OPTION_SPEC;
  // GEO-facade request specifications
  private static final RequestSpecification GEO_FACADE_BASE_SPEC;
  private static final RequestSpecification GEO_FACADE_GEO_GROUPS_SPEC;
  private static final RequestSpecification GEO_FACADE_GEO_GROUPS_ID_SPEC;
  private static final RequestSpecification GEO_FACADE_GEO_GROUPS_ID_CITIES_SPEC;
  private static final RequestSpecification GEO_FACADE_GEO_GROUPS_CITIES_SPEC;
  private static final RequestSpecification GEO_FACADE_CITY_SPEC;
  // Cities request specifications
  private static final RequestSpecification CITIES_SPEC;
  private static final RequestSpecification CITY_SPEC;
  // Metro request specifications
  private static final RequestSpecification METRO_SPEC;
  // Feedback request specifications
  private static final RequestSpecification FEEDBACK_BASE_SPEC;
  private static final RequestSpecification FEEDBACK_ADDITION_SPEC;
  // Offices request specifications
  private static final RequestSpecification OFFICES_BASE_SPEC;
  private static final RequestSpecification OFFICES_IMPORT_SPEC;
  private static final RequestSpecification OFFICES_QUEUE_IN_SPEC;
  private static final RequestSpecification OFFICES_QUEUE_ERR_SPEC;
  // Short-url
  private static final RequestSpecification SHORT_URL_BASE_SPEC;
  private static final RequestSpecification SHORT_URL_CREATE_SPEC;
  private static final RequestSpecification SHORT_URL_CREATE_ARRAY_SPEC;
  private static final RequestSpecification SHORT_URL_READ_SPEC;
  private static final RequestSpecification SHORT_URL_UPDATE_DELETE_SPEC;

  public static final LinkedHashMap<AccessibleUser, AccessToken> LOGGED_IN_USERS =
      new LinkedHashMap<>();
  public static final LinkedHashMap<Integer, Page> CREATED_PAGES = new LinkedHashMap<>();
  public static final LinkedHashMap<String, Experiment> CREATED_EXPERIMENTS = new LinkedHashMap<>();
  public static final LinkedHashMap<Integer, GeoGroup> CREATED_GEO_GROUPS = new LinkedHashMap<>();
  public static final List<String> LIST_OF_CREATED_ENTITIES = new ArrayList<>();

  static {
    var environment = System.getProperty("env");
    LOGGER.info("Тестовая среда - " + environment);
    switch (environment) {
      case "develop" -> getDevelopEnvironmentSettings();
      case "preprod" -> getPreProdEnvironmentSettings();
      case "acms_feature" -> getAcmsFeatureEnvironmentSettings();
      case "cs_feature" -> getContentStoreFeatureEnvironmentSettings();
      case "prod" -> getProdEnvironmentSettings();
      default -> throw new IllegalArgumentException("""
              Указана некорректная тестовая среда. Доступны:
              1. develop
              2. preprod
              3. acms_feature-####
              4. cs_feature-####
              5. prod""");
    }
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для CS",
        CS_BASE_URL));
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для AB-TEST",
        AB_TEST_BASE_URL));
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для GEO-FACADE",
        GEO_FACADE_BASE_URL));
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для METRO",
        METRO_BASE_URL));
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для FEEDBACK",
        FEEDBACK_BASE_URL));
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для FEEDBACK",
        FEEDBACK_BASE_URL));
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для OFFICES",
        OFFICES_BASE_URL));
    LOGGER.info("Устанавливаю базовую конфгурацию HTTP запросов");
    RestAssured.defaultParser = JSON;
    RestAssured.config = RestAssuredConfig.config()
        .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> {
          ObjectMapper objectMapper = new ObjectMapper();
          objectMapper.registerModule(new JavaTimeModule());
          return objectMapper;
        }));
    BASE_CS_SPEC = new RequestSpecBuilder()
        .setRelaxedHTTPSValidation()
        .setBaseUri(CS_BASE_URL)
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .build();
    BASE_AB_TEST_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBaseUri(AB_TEST_BASE_URL)
        .build();
    GEO_FACADE_BASE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBaseUri(GEO_FACADE_BASE_URL)
        .build();
    CITIES_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBaseUri(CITIES_BASE_URL)
        .build();
    FEEDBACK_BASE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBaseUri(FEEDBACK_BASE_URL)
        .build();
    OFFICES_BASE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBaseUri(OFFICES_BASE_URL)
        .build();
    SHORT_URL_BASE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBaseUri(SHORT_URL_BASE_URL)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-controller");
    PAGE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(pageBasePath)
        .build();
    PAGE_ID_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(pageIdBasePath)
        .build();
    PAGE_ID_COPY_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(pageIdCopyBasePath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /content-page-controller");
    PAGE_CONTENTS_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(pageContentsBasePath)
        .build();
    PAGE_CONTENTS_NO_CACHE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(pageContentsNoCacheBasePath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /meta-info-content-page-controller");
    META_INFO_CONTENT_PAGE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(metaInfoContentPageBasePath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-draft-controller");
    DRAFT_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(draftBasePath)
        .build();
    DRAFT_EXEC_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(draftExecBasePath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /audit-controller");
    AUDIT_TRANSACTIONS_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(auditBasePath)
        .build();
    AUDIT_TRANSACTION_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(auditBasePath + "/{uid}")
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /rollback-controller");
    AUDIT_ROLLBACK_TRANSACTIONS_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(auditRollbackBasePath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /experiment-controller");
    GET_ALL_OR_CREATE_EXPERIMENT_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_AB_TEST_SPEC)
        .setBasePath(getAllOrCreateExperimentPath)
        .build();
    GET_DELETE_PATCH_EXPERIMENT_BY_UUID_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_AB_TEST_SPEC)
        .setBasePath(getDeletePatchExperimentByUuidPath)
        .build();
    INVOLVEMENTS_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_AB_TEST_SPEC)
        .setBasePath(involvementsUri)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /option-controller");
    GET_ALL_OR_DELETE_OPTION_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_AB_TEST_SPEC)
        .setBasePath(getAllOrCreateOptionPath)
        .build();
    GET_DELETE_PATCH_OPTION_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_AB_TEST_SPEC)
        .setBasePath(getDeletePatchOptionPath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /cities");
    GEO_FACADE_GEO_GROUPS_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(GEO_FACADE_BASE_SPEC)
        .setBasePath(geoFacadeGeoGroupsPath)
        .build();
    GEO_FACADE_GEO_GROUPS_ID_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(GEO_FACADE_BASE_SPEC)
        .setBasePath(geoFacadeGeoGroupsIdPath)
        .build();
    GEO_FACADE_GEO_GROUPS_ID_CITIES_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(GEO_FACADE_BASE_SPEC)
        .setBasePath(geoFacadeGeoGroupsIdCitiesPath)
        .build();
    GEO_FACADE_GEO_GROUPS_CITIES_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(GEO_FACADE_BASE_SPEC)
        .setBasePath(geoFacadeGeoGroupsCitiesPath)
        .build();
    GEO_FACADE_CITY_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(GEO_FACADE_BASE_SPEC)
        .setBasePath(geoFacadeCityPath)
        .build();
    CITY_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(CITIES_SPEC)
        .setBasePath(cityPath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /metro");
    METRO_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(metroBasePath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /feedback");
    FEEDBACK_ADDITION_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(FEEDBACK_BASE_SPEC)
        .setBasePath(feedbackAdditionPath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /offices");
    OFFICES_IMPORT_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(OFFICES_BASE_SPEC)
        .setContentType("multipart/form-data")
        .setBasePath(officesImportPath)
        .build();
    OFFICES_QUEUE_IN_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(OFFICES_BASE_SPEC)
        .setBasePath(officesInPath)
        .build();
    OFFICES_QUEUE_ERR_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(OFFICES_BASE_SPEC)
        .setBasePath(officesErrPath)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /short-url");
    SHORT_URL_CREATE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(SHORT_URL_BASE_SPEC)
        .setBasePath(shortUrlCreatePath)
        .build();
    SHORT_URL_CREATE_ARRAY_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(SHORT_URL_BASE_SPEC)
        .setBasePath(shortUrlCreateArrayPath)
        .build();
    SHORT_URL_READ_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(SHORT_URL_BASE_SPEC)
        .setBasePath(shortUrlReadPath)
        .build();
    SHORT_URL_UPDATE_DELETE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(SHORT_URL_BASE_SPEC)
        .setBasePath(shortUrlUpdateDeletePath)
        .build();
  }

  private static void getDevelopEnvironmentSettings() {
    CS_BASE_URL = "http://develop" + URL_ENDING;
    setEnv();
    setUpEndpoints();
  }

  private static void getPreProdEnvironmentSettings() {
    CS_BASE_URL = "http://preprod" + URL_ENDING;
    setEnv();
    setUpEndpoints();
  }

  private static void getAcmsFeatureEnvironmentSettings() {
    CS_BASE_URL = String.format("http://acms-feature-alfabankru-%s.alfabankru-reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    setEnv();
    setUpEndpoints();
  }

  private static void setEnv() {
    AB_TEST_BASE_URL = CS_BASE_URL;
    GEO_FACADE_BASE_URL = CS_BASE_URL;
    CITIES_BASE_URL = CS_BASE_URL;
    METRO_BASE_URL = CS_BASE_URL;
    FEEDBACK_BASE_URL = CS_BASE_URL;
    OFFICES_BASE_URL = CS_BASE_URL;
    SHORT_URL_BASE_URL = CS_BASE_URL;
    contentStoreBasePath = PREFIX + "/content-store";
    abTestsBasePath = PREFIX + "/ab-test";
    geoFacadeBasePath = PREFIX + "/geo-facade";
    citiesBasePath = PREFIX + "/cities";
    metroBasePath = PREFIX + "/metro/";
    feedbackBasePath = PREFIX + "/feedback";
    officesBasePath = PREFIX + "/offices";
    shortUrlBasePath = PREFIX + "/schuller/";
  }

  private static void getContentStoreFeatureEnvironmentSettings() {
    CS_BASE_URL = String.format("http://feature-alfabankru-%s.content-store.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    AB_TEST_BASE_URL = String.format("http://feature-alfabankru-%s.ab-testing.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    GEO_FACADE_BASE_URL = String.format("http://feature-alfabankru-%s.geo-facade.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    CITIES_BASE_URL = String.format("http://feature-alfabankru-%s.cities.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    METRO_BASE_URL = String.format("http://feature-alfabankru-%s.metro.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    OFFICES_BASE_URL = String.format("http://feature-alfabankru-%s.offices.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    SHORT_URL_BASE_URL = String.format("http://feature-alfabankru-%s.short-url.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    contentStoreBasePath = "";
    abTestsBasePath = "";
    geoFacadeBasePath = "";
    citiesBasePath = "";
    metroBasePath = "";
    feedbackBasePath = "";
    officesBasePath = "";
    setUpEndpoints();
  }

  private static void getProdEnvironmentSettings() {
    CS_BASE_URL = "http://alfabank.ru";
    setEnv();
    setUpEndpoints();
  }

  private static void setUpEndpoints() {
    final var adminPanelBasePath = contentStoreBasePath + "/admin-panel";
    pageBasePath = adminPanelBasePath + "/pages";
    pageIdBasePath = pageBasePath + "/{id}";
    pageIdCopyBasePath = pageIdBasePath + "/copy";
    pageContentsBasePath = contentStoreBasePath + "/page-contents";
    pageContentsNoCacheBasePath = contentStoreBasePath + "/page-contents-nocache";
    metaInfoContentPageBasePath = adminPanelBasePath + "/meta-info-page-contents";
    draftBasePath = pageBasePath + "/{pageId}/drafts";
    draftExecBasePath = draftBasePath + "/execute";
    auditBasePath = adminPanelBasePath + "/audit/transactions";
    auditRollbackBasePath =
        adminPanelBasePath + "/rollback/rollbackToStateBeforeTransaction/{transactionUid}";
    getAllOrCreateExperimentPath = abTestsBasePath + "/admin-panel/experiments";
    getDeletePatchExperimentByUuidPath = getAllOrCreateExperimentPath + "/{uuid}";
    getAllOrCreateOptionPath = getAllOrCreateExperimentPath + "/{experimentUuid}/options";
    getDeletePatchOptionPath = getAllOrCreateExperimentPath + "/options/{optionUuid}";
    involvementsUri = abTestsBasePath + "/experiments/involvements";
    geoFacadeGeoGroupsPath = geoFacadeBasePath + "/geo-groups";
    geoFacadeGeoGroupsIdPath = geoFacadeGeoGroupsPath + "/{id}";
    geoFacadeGeoGroupsIdCitiesPath = geoFacadeGeoGroupsIdPath + "/cities";
    geoFacadeGeoGroupsCitiesPath = geoFacadeGeoGroupsPath + "/cities";
    geoFacadeCityPath = geoFacadeBasePath + "/city";
    cityPath = citiesBasePath + "/city";
    feedbackAdditionPath = feedbackBasePath + "/feedbacks";
    officesImportPath = officesBasePath + "/rpc/offices/import/fromfile";
    officesInPath = officesBasePath + "/test/mq/offices/in";
    officesErrPath = officesBasePath + "/test/mq/offices/err";
    shortUrlCreatePath = shortUrlBasePath;
    shortUrlCreateArrayPath = shortUrlBasePath + "arrayLinks";
    shortUrlReadPath = shortUrlBasePath;
    shortUrlUpdateDeletePath = shortUrlBasePath + "{id}";
  }

  public static RequestSpecification getPageSpec() {
    return PAGE_SPEC;
  }

  public static RequestSpecification getPageIdSpec() {
    return PAGE_ID_SPEC;
  }

  public static RequestSpecification getPageIdCopySpec() {
    return PAGE_ID_COPY_SPEC;
  }

  public static RequestSpecification getPageContentsSpec() {
    return PAGE_CONTENTS_SPEC;
  }

  public static RequestSpecification getPageContentsNoCacheSpec() {
    return PAGE_CONTENTS_NO_CACHE_SPEC;
  }

  public static RequestSpecification getMetaInfoContentPageSpec() {
    return META_INFO_CONTENT_PAGE_SPEC;
  }

  public static RequestSpecification getDraftSpec() {
    return DRAFT_SPEC;
  }

  public static RequestSpecification getDraftExecSpec() {
    return DRAFT_EXEC_SPEC;
  }

  public static RequestSpecification getAuditTransactionsSpec() {
    return AUDIT_TRANSACTIONS_SPEC;
  }

  public static RequestSpecification getAuditTransactionSpec() {
    return AUDIT_TRANSACTION_SPEC;
  }

  public static RequestSpecification getAuditRollbackTransactionsSpec() {
    return AUDIT_ROLLBACK_TRANSACTIONS_SPEC;
  }

  public static RequestSpecification getGetAllOrCreateExperimentSpec() {
    return GET_ALL_OR_CREATE_EXPERIMENT_SPEC;
  }

  public static RequestSpecification getGetDeletePatchExperimentByUuidSpec() {
    return GET_DELETE_PATCH_EXPERIMENT_BY_UUID_SPEC;
  }

  public static RequestSpecification getInvolvementsSpec() {
    return INVOLVEMENTS_SPEC;
  }

  public static RequestSpecification getGetAllOrDeleteOptionSpec() {
    return GET_ALL_OR_DELETE_OPTION_SPEC;
  }

  public static RequestSpecification getGetDeletePatchOptionSpec() {
    return GET_DELETE_PATCH_OPTION_SPEC;
  }

  public static RequestSpecification getGeoFacadeGeoGroupsSpec() {
    return GEO_FACADE_GEO_GROUPS_SPEC;
  }

  public static RequestSpecification getGeoFacadeGeoGroupsIdSpec() {
    return GEO_FACADE_GEO_GROUPS_ID_SPEC;
  }

  public static RequestSpecification getGeoFacadeGeoGroupsIdCitiesSpec() {
    return GEO_FACADE_GEO_GROUPS_ID_CITIES_SPEC;
  }

  public static RequestSpecification getGeoFacadeGeoGroupsCitiesSpec() {
    return GEO_FACADE_GEO_GROUPS_CITIES_SPEC;
  }

  public static RequestSpecification getGeoFacadeCitySpec() {
    return GEO_FACADE_CITY_SPEC;
  }

  public static RequestSpecification getCitySpec() {
    return CITY_SPEC;
  }

  public static RequestSpecification getMetroSpec() {
    return METRO_SPEC;
  }

  public static RequestSpecification getFeedbackAdditionSpec() {
    return FEEDBACK_ADDITION_SPEC;
  }

  public static RequestSpecification getOfficesQueueInSpec() {
    return OFFICES_QUEUE_IN_SPEC;
  }

  public static RequestSpecification getOfficesImportSpec() {
    return OFFICES_IMPORT_SPEC;
  }

  public static RequestSpecification getOfficesQueueErrSpec() {
    return OFFICES_QUEUE_ERR_SPEC;
  }

  public static RequestSpecification getShortUrlCreateSpec() {
    return SHORT_URL_CREATE_SPEC;
  }

  public static RequestSpecification getShortUrlCreateArraySpec() {
    return SHORT_URL_CREATE_ARRAY_SPEC;
  }

  public static RequestSpecification getShortUrlReadSpec() {
    return SHORT_URL_READ_SPEC;
  }

  public static RequestSpecification getShortUrlUpdateDeleteSpec() {
    return SHORT_URL_UPDATE_DELETE_SPEC;
  }

  /**
   * Describe response.
   *
   * @param logger   Logger
   * @param response Response
   */
  protected static void describeResponse(final Logger logger, final Response response) {
    logger.info(String.format("Получен ответ: %d\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
  }
}
