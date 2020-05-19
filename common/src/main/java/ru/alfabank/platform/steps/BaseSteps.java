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

  private static final String BASE_URL;
  private static final String URL_ENDING = ".ci.k8s.alfa.link/";
  private static final String PREFIX = "api/v1";
  private static final String CS = PREFIX + "/content-store";
  private static final String AP = CS + "/admin-panel";
  private static final String PAGE_BASE_PATH = AP + "/pages/";
  private static final String PAGE_ID_BASE_PATH = PAGE_BASE_PATH + "/{id}";
  private static final String PAGE_CONTENTS_BASE_PATH = CS + "/page-contents";
  private static final String META_INFO_CONTENT_PAGE_BASE_PATH = AP
      + "/meta-info-page-contents";
  private static final String DRAFT_BASE_PATH = PAGE_BASE_PATH + "/{pageId}/drafts";
  private static final String DRAFT_EXEC_BASE_PATH = DRAFT_BASE_PATH + "/execute";
  private static final String AUDIT_BASE_PATH = AP + "/audit/transactions";
  private static final String AUDIT_ROLLBACK_BASE_PATH = AP
      + "/rollback/rollbackToStateBeforeTransaction/{transactionUid}";
  private static final String GET_ALL_OR_CREATE_EXPERIMENT_PATH =
      PREFIX + "/ab-test/admin-panel/experiments";
  private static final String GET_DELETE_PATCH_EXPERIMENT_PATH =
      GET_ALL_OR_CREATE_EXPERIMENT_PATH + "/{uuid}";
  private static final String GET_ALL_OR_CREATE_OPTION_PATH =
      GET_ALL_OR_CREATE_EXPERIMENT_PATH + "/{experimentUuid}/options";
  private static final String GET_DELETE_PATCH_OPTION_PATH =
      GET_ALL_OR_CREATE_EXPERIMENT_PATH + "/options/{optionUuid}";
  private static final String INVOLVEMENTS_URI = PREFIX + "/ab-test/experiments/involvements";
  private static final RequestSpecification BASE_SPEC;
  private static final RequestSpecification PAGE_SPEC;
  private static final RequestSpecification PAGE_ID_SPEC;
  private static final RequestSpecification PAGE_CONTENT_SPEC;
  private static final RequestSpecification META_INFO_CONTENT_PAGE_SPEC;
  private static final RequestSpecification DRAFT_SPEC;
  private static final RequestSpecification DRAFT_EXEC_SPEC;
  private static final RequestSpecification AUDIT_TRANSACTIONS_SPEC;
  private static final RequestSpecification AUDIT_TRANSACTION_SPEC;
  private static final RequestSpecification AUDIT_ROLLBACK_TRANSACTIONS_SPEC;
  private static final RequestSpecification GET_ALL_OR_CREATE_EXPERIMENT_SPEC;
  private static final RequestSpecification GET_DELETE_PATCH_EXPERIMENT_SPEC;
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
      BASE_URL = "http://preprod" + URL_ENDING;
    } else if (environment.contains("feature")) {
      BASE_URL = String.format(
          "http://acms-feature-alfabankru-%s.alfabankru-reviews%s",
          StringUtils.substringAfter(System.getProperty("env"), "-"),
          URL_ENDING);
    } else {
      BASE_URL = "http://develop" + URL_ENDING;
    }
    LOGGER.info(String.format("URI '%s' установлен в качестве базового", BASE_URL));
    LOGGER.info("Устанавливаю базовую конфгурацию HTTP запросов");
    RestAssured.defaultParser = JSON;
    RestAssured.config = RestAssuredConfig.config()
        .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> {
          ObjectMapper objectMapper = new ObjectMapper();
          objectMapper.registerModule(new JavaTimeModule());
          return objectMapper;
        }));
    BASE_SPEC = new RequestSpecBuilder()
        .setRelaxedHTTPSValidation()
        .setBaseUri(
            System.getProperty("feature") == null
                ? BASE_URL
                : String.format("http://develop%s", URL_ENDING))
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-controller");
    PAGE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(PAGE_BASE_PATH)
        .build();
    PAGE_ID_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(PAGE_ID_BASE_PATH)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /content-page-controller");
    PAGE_CONTENT_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(PAGE_CONTENTS_BASE_PATH)
        .addQueryParam("city_uid", "21")
        .addQueryParam("device", desktop)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /meta-info-content-page-controller");
    META_INFO_CONTENT_PAGE_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(META_INFO_CONTENT_PAGE_BASE_PATH)
        .addQueryParam("device", desktop)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-draft-controller");
    DRAFT_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(DRAFT_BASE_PATH)
        .build();
    DRAFT_EXEC_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(DRAFT_EXEC_BASE_PATH)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /audit-controller");
    AUDIT_TRANSACTIONS_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(AUDIT_BASE_PATH)
        .build();
    AUDIT_TRANSACTION_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(AUDIT_BASE_PATH + "/{uid}")
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /rollback-controller");
    AUDIT_ROLLBACK_TRANSACTIONS_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(AUDIT_ROLLBACK_BASE_PATH)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /experiment-controller");
    GET_ALL_OR_CREATE_EXPERIMENT_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(GET_ALL_OR_CREATE_EXPERIMENT_PATH)
        .build();
    GET_DELETE_PATCH_EXPERIMENT_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(GET_DELETE_PATCH_EXPERIMENT_PATH)
        .build();
    INVOLVEMENTS_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(INVOLVEMENTS_URI)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /option-controller");
    GET_ALL_OR_DELETE_OPTION_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(GET_ALL_OR_CREATE_OPTION_PATH)
        .build();
    GET_DELETE_PATCH_OPTION_SPEC = new RequestSpecBuilder()
        .addRequestSpecification(BASE_SPEC)
        .setBasePath(GET_DELETE_PATCH_OPTION_PATH)
        .build();
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-controller");

  }

  public static RequestSpecification getBaseSpec() {
    return BASE_SPEC;
  }

  public static RequestSpecification getPageSpec() {
    return PAGE_SPEC;
  }

  public static RequestSpecification getPageIdSpec() {
    return PAGE_ID_SPEC;
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

  public static RequestSpecification getGetDeletePatchExperimentSpec() {
    return GET_DELETE_PATCH_EXPERIMENT_SPEC;
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
}
