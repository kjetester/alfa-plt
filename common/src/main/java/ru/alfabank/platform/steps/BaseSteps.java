package ru.alfabank.platform.steps;

import static io.restassured.parsing.Parser.JSON;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.LinkedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.users.AccessibleUser;

public class BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(BaseSteps.class);

  // Base
  private static String CS_BASE_URL;
  private static String AB_TEST_BASE_URL;
  private static final String URL_ENDING = ".ci.k8s.alfa.link/";
  private static final String PREFIX = "api/v1";
  // Content-Store paths
  private static String contentStoreBasePath;
  private static String pageBasePath;
  private static String pageIdBasePath;
  private static String pageIdCopyBasePath;
  private static String pageContentsBasePath;
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
  // Content-Store request specifications
  private static final RequestSpecification BASE_CS_SPEC;
  private static final RequestSpecification PAGE_SPEC;
  private static final RequestSpecification PAGE_ID_SPEC;
  private static final RequestSpecification PAGE_ID_COPY_SPEC;
  private static final RequestSpecification PAGE_CONTENT_SPEC;
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

  public static final LinkedHashMap<AccessibleUser, AccessToken> LOGGED_IN_USERS =
      new LinkedHashMap<>();
  public static final LinkedHashMap<Integer, Page> CREATED_PAGES = new LinkedHashMap<>();
  public static final LinkedHashMap<String, Experiment> CREATED_EXPERIMENTS = new LinkedHashMap<>();

  static {
    var environment = System.getProperty("env");
    LOGGER.info("Тестовая среда - " + environment);
    if (environment.contains("preprod")) {
      getPreProdEnvironmentSettings();
    } else if (environment.contains("acms_feature")) {
      getAcmsFeatureEnvironmentSettings();
    } else if (environment.contains("cs_feature")) {
      getContentStoreFeatureEnvironmentSettings();
    } else {
      getDevelopEnvironmentSettings();
    }
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для CS",
        CS_BASE_URL));
    LOGGER.info(String.format(
        "URI '%s' установлен в качестве базового для AB-TEST",
        AB_TEST_BASE_URL));
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
    PAGE_CONTENT_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_CS_SPEC)
        .setBasePath(pageContentsBasePath)
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
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-controller");

  }

  private static void getDevelopEnvironmentSettings() {
    CS_BASE_URL = "http://develop" + URL_ENDING;
    AB_TEST_BASE_URL = CS_BASE_URL;
    contentStoreBasePath = PREFIX + "/content-store";
    abTestsBasePath = PREFIX + "/ab-test";
    setUpEndpoints();
  }

  private static void getPreProdEnvironmentSettings() {
    CS_BASE_URL = "http://preprod" + URL_ENDING;
    AB_TEST_BASE_URL = CS_BASE_URL;
    contentStoreBasePath = PREFIX + "/content-store";
    abTestsBasePath = PREFIX + "/ab-test";
    setUpEndpoints();
  }

  private static void getAcmsFeatureEnvironmentSettings() {
    CS_BASE_URL = String.format("http://acms-feature-alfabankru-%s.alfabankru-reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    AB_TEST_BASE_URL = CS_BASE_URL;
    contentStoreBasePath = PREFIX + "/content-store";
    abTestsBasePath = PREFIX + "/ab-test";
    setUpEndpoints();
  }

  private static void getContentStoreFeatureEnvironmentSettings() {
    CS_BASE_URL = String.format("http://feature-alfabankru-%s.content-store.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    AB_TEST_BASE_URL = String.format("http://feature-alfabankru-%s.ab-testing.reviews%s",
        StringUtils.substringAfter(System.getProperty("env"), "-"), URL_ENDING);
    contentStoreBasePath = "";
    abTestsBasePath = "";
    setUpEndpoints();
  }

  private static void setUpEndpoints() {
    final var adminPanelBasePath = contentStoreBasePath + "/admin-panel";
    pageBasePath = adminPanelBasePath + "/pages";
    pageIdBasePath = pageBasePath + "/{id}";
    pageIdCopyBasePath = pageIdBasePath + "/copy";
    pageContentsBasePath = contentStoreBasePath + "/page-contents";
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

  public static RequestSpecification getPageContentSpec() {
    return PAGE_CONTENT_SPEC;
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
