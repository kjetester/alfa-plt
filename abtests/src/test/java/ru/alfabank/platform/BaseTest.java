package ru.alfabank.platform;

import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;
import static java.time.ZoneOffset.UTC;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.businessobjects.enums.Entity.PAGE;
import static ru.alfabank.platform.businessobjects.enums.Entity.WIDGET;
import static ru.alfabank.platform.businessobjects.enums.Method.CHANGE;
import static ru.alfabank.platform.businessobjects.enums.Method.CHANGE_LINKS;
import static ru.alfabank.platform.businessobjects.enums.Method.CREATE;
import static ru.alfabank.platform.businessobjects.enums.Status.CANCELLED;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;
import static ru.alfabank.platform.helpers.KeycloakHelper.logout;
import static ru.alfabank.platform.helpers.UuidHelper.getNewUuid;
import static ru.alfabank.platform.helpers.UuidHelper.getShortRandUuid;

import com.epam.reportportal.annotations.ParameterKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.businessobjects.User;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.draft.DataDraft;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.ExperimentOptionName;
import ru.alfabank.platform.businessobjects.enums.ProductType;

public class BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

  protected static final User USER = new User();
  private static final String URL_ENDING = ".ci.k8s.alfa.link/";
  private static final String PREFIX = "/api/v1";
  // EXPERIMENTS & OPTIONS
  private static final String GET_ALL_OR_CREATE_EXPERIMENT_PATH =
      PREFIX + "/ab-test/admin-panel/experiments";
  private static final String GET_DELETE_PATCH_EXPERIMENT_PATH =
      GET_ALL_OR_CREATE_EXPERIMENT_PATH + "/{uuid}";
  private static final String GET_ALL_OR_CREATE_OPTION_PATH =
      GET_ALL_OR_CREATE_EXPERIMENT_PATH + "/{experimentUuid}/options";
  private static final String GET_DELETE_PATCH_OPTION_PATH =
      GET_ALL_OR_CREATE_EXPERIMENT_PATH + "/options/{optionUuid}";
  private static final String INVOLVEMENTS_URI = PREFIX + "/ab-test/experiments/involvements";
  // PAGES & DRAFTS
  private static final String CS = PREFIX + "/content-store/admin-panel";
  private static final String PAGE_BASE_PATH = CS + "/pages/";
  private static final String PAGE_DRAFT_BASE_PATH = CS + "/pages/{pageId}/drafts";
  private static final String DRAFT_PUBLISH_BASE_PATH = CS + "/pages/{pageId}/drafts/execute";
  // META_INFO_PAGE_CONTENTS
  private static final String META_INFO_CONTENT_PAGE_CONTROLLER = CS + "/meta-info-page-contents";

  protected static String baseUri;
  protected static RequestSpecification baseSpec;
  protected static RequestSpecification getAllOrCreateExperimentSpec;
  protected static RequestSpecification getDeletePatchExperimentSpec;
  protected static RequestSpecification involvementsSpec;
  protected static RequestSpecification getAllOrDeleteOptionSpec;
  protected static RequestSpecification getDeletePatchOptionSpec;
  protected static RequestSpecification pageControllerSpec;
  protected static RequestSpecification pageDraftControllerSpec;
  protected static RequestSpecification pageDraftControllerPublishSpec;
  protected static RequestSpecification metaInfoContentPageControllerSpec;
  protected static Map<Integer, Page> createdPages = new HashMap<>();
  protected static Map<String, Experiment> createdExperiments = new HashMap<>();

  /**
   * Prepare environment before a test.
   * @param environment environment
   */
  @BeforeSuite(
      description = "Выполенние предусловий:\n"
          + "\t1. Установка тествой среды\n"
          + "\t2. Создание объекта страницы-донора\n"
          + "\t3. Настройка базовой конфигурации HTTP запросов\n"
          + "\t4. Настройка конфигурации HTTP запросов к /experiment-controller"
          + "\t5. Настройка конфигурации HTTP запросов к /option-controller\n"
          + "\t6. Создание тестовой странцы",
      alwaysRun = true)
  @Parameters({"environment"})
  public void beforeSuite(
      @Optional("develop")
      @ParameterKey("environment")
      final String environment) {
    setUpEnvironment(environment);
    setUpBaseRequestConfiguration();
    setUpExperimentControllerRequestConfiguration();
    setUpOptionControllerRequestConfiguration();
    setUpPageControllerRequestConfiguration();
    setUpContentStoreRequestsConfiguration();
  }

  /**
   * Clean up.
   */
  @AfterTest(description = "Удаление созданных экспериментов", enabled = false)
  public void afterTest() {
    final var experimentsCount = createdExperiments.size();
    LOGGER.info(String.format("Экспериментов к удалению: %d", experimentsCount));
    if (experimentsCount > 0) {
      createdExperiments.forEach((key, experiment) -> {
        LOGGER.info(String.format("Начинаю процесс удаления эксперимента '%s'",
            experiment.getUuid()));
        var isDeletable = experiment.getStatus() != RUNNING;
        if (!isDeletable) {
          LOGGER.info(String.format("Эксперимент '%s' нуждается в остановке", experimentsCount));
          final var response = given().spec(getDeletePatchExperimentSpec).auth()
              .oauth2(getToken(USER).getAccessToken()).pathParam("uuid", experiment.getUuid())
              .body(new Experiment.Builder().setEnabled(false).build()).patch();
          isDeletable = response.getStatusCode() == SC_OK;
          if (isDeletable) {
            LOGGER.info(String.format("Эксперимент '%s' остановлен", experiment.getUuid()));
            createdExperiments.remove(key);
          } else {
            LOGGER.warn(String.format("Экспиремент не был остановлен:\n'%s'\n%s",
                response.getStatusCode(),
                response.prettyPrint()));
          }
        }
        if (isDeletable) {
          final var response = given().spec(getDeletePatchExperimentSpec).auth()
              .oauth2(getToken(USER).getAccessToken()).pathParam("uuid", experiment.getUuid())
              .when().delete();
          if (response.getStatusCode() == SC_OK) {
            LOGGER.info(String.format("Эксперимент '%s' удалён", experiment.getUuid()));
            createdExperiments.remove(key);
          } else {
            LOGGER.warn(String.format("Экспиремент не был удалён:\n'%s'\n%s",
                response.getStatusCode(),
                response.prettyPrint()));
          }
        } else {
          LOGGER.warn("Экспиремент не был удалён :-(");
        }
      });
    }
  }

  /**
   * Clean up.
   */
  @AfterSuite(description = "Удаление созданных страниц")
  public void afterSuite() {
    final var pagesCount = createdPages.size();
    LOGGER.info(String.format("Страниц к удалению: %d", pagesCount));
    if (pagesCount > 0) {
      createdPages.entrySet().parallelStream().forEach(entry -> {
        LOGGER.info(String.format("Начинаю процесс удаления страницы '%s'",
            entry.getValue().getUri()));
        if (given().spec(pageControllerSpec).basePath(PAGE_BASE_PATH + "/{id}")
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("id", entry.getValue().getId())
            .when().delete().getStatusCode() == SC_OK) {
          LOGGER.info(String.format("Страница '%s' удалена", entry.getValue().getUri()));
        } else {
          LOGGER.warn(String.format("Не удалось удалить страницу '%s'", entry.getValue().getUri()));
        }
      });
    }
    logout(USER);
  }

  /**
   * Set up environment.
   * @param environment environment
   */
  private void setUpEnvironment(
      final String environment) {
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
            "http://acms-feature-alfabankru-%s.alfabankru-reviews%s",
            System.getProperty("feature"), URL_ENDING);
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
   * Set up base request configuration.
   */
  private void setUpBaseRequestConfiguration() {
    LOGGER.info("Устанавливаю базовую конфгурацию HTTP запросов");
    RestAssured.defaultParser = JSON;
    RestAssured.config = RestAssuredConfig.config()
        .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> {
          ObjectMapper objectMapper = new ObjectMapper();
          objectMapper.registerModule(new JavaTimeModule());
          return objectMapper;
        }));
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
   * Set up /experiment-controller request configuration.
   */
  private void setUpExperimentControllerRequestConfiguration() {
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /experiment-controller");
    getAllOrCreateExperimentSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(GET_ALL_OR_CREATE_EXPERIMENT_PATH)
        .build();
    getDeletePatchExperimentSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(GET_DELETE_PATCH_EXPERIMENT_PATH)
        .build();
    involvementsSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(INVOLVEMENTS_URI)
        .build();
  }

  /**
   * Set up /option-controller request configuration.
   */
  private void setUpOptionControllerRequestConfiguration() {
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /option-controller");
    getAllOrDeleteOptionSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(GET_ALL_OR_CREATE_OPTION_PATH)
        .build();
    getDeletePatchOptionSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(GET_DELETE_PATCH_OPTION_PATH)
        .build();
  }

  /**
   * Set up page-controller request configuration.
   */
  private void setUpPageControllerRequestConfiguration() {
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к /page-controller");
    pageControllerSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(PAGE_BASE_PATH)
        .build();
  }

  /**
   * Set up SC request configuration.
   */
  private void setUpContentStoreRequestsConfiguration() {
    LOGGER.info("Устанавливаю конфгурацию HTTP запросов к Content-Store");
    pageDraftControllerSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(PAGE_DRAFT_BASE_PATH)
        .build();
    pageDraftControllerPublishSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(DRAFT_PUBLISH_BASE_PATH)
        .build();
    metaInfoContentPageControllerSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(META_INFO_CONTENT_PAGE_CONTROLLER)
        .build();
  }

  protected OffsetDateTime getCurrentDateTime() {
    return LocalDateTime.now().atOffset(UTC).minusHours(3);
  }

  protected String getInvalidEndDate() {
    return getCurrentDateTime().plusDays(1).minusMinutes(1).toString();
  }

  protected String getValidEndDate() {
    return getCurrentDateTime().plusDays(1).plusMinutes(2).toString();
  }

  protected String getValidEndDatePlusOneMonth() {
    return getCurrentDateTime().plusMonths(1).toString();
  }

  /**
   * Create new page.
   * @param start start date
   * @param end end date
   * @return created page
   */
  protected Page createPage(final String start,
                            final String end,
                            final Boolean isEnabled) {
    String pageUri = getShortRandUuid();
    var page = new Page.Builder()
        .setUri("/qr/automation/" + pageUri)
        .setTitle("title_" + pageUri)
        .setDateFrom(start)
        .setDateTo(end)
        .setEnable(isEnabled)
        .build();
    LOGGER.info(String.format("Выполняю запрос создания страницы\n%s",
        describeBusinessObject(page)));
    var response =
        given()
            .spec(pageControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .body(page)
        .when().post()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_OK);
    page = new Page.Builder()
        .using(page)
        .setId(response.then().extract().body().jsonPath().get("id"))
        .build();
    createdPages.put(page.getId(), page);
    return page;
  }

  /**
   * Create new widget at page level.
   * @param page page
   * @param parentWidget parent widget
   * @param device device
   * @param enabled is enabled
   * @param experimentOptionName experiment option name
   * @param defaultWidget is default widget
   * @param start start date
   * @param end end date
   * @return widget
   */
  protected Widget createWidget(final Page page,
                                final Widget parentWidget,
                                final Device device,
                                final Boolean enabled,
                                final ExperimentOptionName experimentOptionName,
                                final Boolean defaultWidget,
                                final String start,
                                final String end) {
    final var widget = new Widget.Builder()
        .setUid(getNewUuid())
        .setName(randomAlphanumeric(10))
        .setDateFrom(start)
        .setDateTo(end)
        .setDevice(device)
        .setLocalization("ru")
        .isEnabled(enabled)
        .setChildren(new ArrayList<>())
        .setChildUids(new ArrayList<>())
        .setVersion("v_1.0.0")
        .setExperimentOptionName(experimentOptionName.toString())
        .isDefaultWidget(defaultWidget)
        .build();
    final var widgetCreationOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setName(widget.getName())
            .setDevice(widget.getDevice())
            .setLocalization(widget.getLocalization())
            .isEnabled(widget.isEnabled())
            .setVersion(widget.getVersion())
            .setExperimentOptionName(widget.getExperimentOptionName())
            .isDefaultWidget(widget.isDefaultWidget())
            .setCityGroups(Collections.singletonList("ru"))
            .setDateFrom(widget.getDateFrom())
            .setDateTo(widget.getDateTo())
            .build(),
        WIDGET.toString(),
        CREATE.toString(),
        widget.getUid());
    WrapperDraft.OperationDraft widgetPlacementOperation;
    if (parentWidget == null) {
      final var widgetsList = page.getWidgetList()
          .stream()
          .filter(w -> w.getDevice() == device)
          .map(Widget::getUid)
          .collect(Collectors.toList());
      widgetsList.add(widget.getUid());
      widgetPlacementOperation = new WrapperDraft.OperationDraft(
          new DataDraft.Builder()
              .setChildUids(widgetsList)
              .build(),
          PAGE.toString(),
          CHANGE_LINKS.toString(),
          page.getId());
    } else {
      final var widgetsList = parentWidget.getChildUids();
      widgetsList.add(widget.getUid());
      widgetPlacementOperation = new WrapperDraft.OperationDraft(
          new DataDraft.Builder()
              .setChildUids(widgetsList)
              .build(),
          WIDGET.toString(),
          CHANGE_LINKS.toString(),
          parentWidget.getUid());
    }
    var draft = new WrapperDraft(
        List.of(
            widgetCreationOperation,
            widgetPlacementOperation),
        widget.getDevice());
    LOGGER.info(String.format("Выполняю запрос сохранения черновика\n%s",
        describeBusinessObject(draft)));
    var response =
        given()
            .spec(pageDraftControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", page.getId())
            .body(draft)
            .when().put()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_OK);
    publishDraft(page.getId(), device);
    if (parentWidget == null) {
      createdPages.get(page.getId()).getWidgetList().add(widget);
    }
    return widget;
  }

  /**
   * Change widget active dates.
   * @param widget widget
   * @param pageId page ID
   * @param start start date
   * @param end end date
   */
  protected void changeWidgetActiveDates(final Widget widget,
                                         final Integer pageId,
                                         final String start,
                                         final String end) {
    var changedWidget = new Widget.Builder()
        .using(widget)
        .setDateFrom(start)
        .setDateTo(end)
        .build();
    var widgetChangingOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setName(changedWidget.getName())
            .setDevice(changedWidget.getDevice())
            .setLocalization(changedWidget.getLocalization())
            .isEnabled(changedWidget.isEnabled())
            .setVersion(changedWidget.getVersion())
            .setExperimentOptionName(changedWidget.getExperimentOptionName())
            .isDefaultWidget(changedWidget.isDefaultWidget())
            .setCityGroups(Collections.singletonList("ru"))
            .setDateFrom(changedWidget.getDateFrom())
            .setDateTo(changedWidget.getDateTo())
            .build(),
        WIDGET.toString(),
        CHANGE.toString(),
        changedWidget.getUid());
    var draft = new WrapperDraft(List.of(widgetChangingOperation), widget.getDevice());
    LOGGER.info(String.format("Выполняю запрос сохранения черновика\n%s",
        describeBusinessObject(draft)));
    var response =
        given()
            .spec(pageDraftControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", pageId)
            .body(draft)
            .when().put()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_OK);
    publishDraft(pageId, widget.getDevice());
  }

  /**
   * Change Widget's AB-test Properties.
   * @param widget widget
   * @param pageId pageId
   * @param isEnabled isEnabled
   * @param experimentOptionName experimentOptionName
   * @param isDefaultWidget isDefaultWidget
   */
  public void changeWidgetABtestProps(final Widget widget,
                                      final Integer pageId,
                                      final Boolean isEnabled,
                                      final ExperimentOptionName experimentOptionName,
                                      final Boolean isDefaultWidget) {
    var changedWidget = new Widget.Builder()
        .using(widget)
        .isEnabled(isEnabled)
        .setExperimentOptionName(experimentOptionName.toString())
        .isDefaultWidget(isDefaultWidget)
        .build();
    var widgetChangingOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setName(changedWidget.getName())
            .setDevice(changedWidget.getDevice())
            .setLocalization(changedWidget.getLocalization())
            .isEnabled(changedWidget.isEnabled())
            .setVersion(changedWidget.getVersion())
            .setExperimentOptionName(changedWidget.getExperimentOptionName())
            .isDefaultWidget(changedWidget.isDefaultWidget())
            .setCityGroups(Collections.singletonList("ru"))
            .setDateFrom(changedWidget.getDateFrom())
            .setDateTo(changedWidget.getDateTo())
            .build(),
        WIDGET.toString(),
        CHANGE.toString(),
        changedWidget.getUid());
    var draft = new WrapperDraft(
        List.of(widgetChangingOperation),
        widget.getDevice());
    LOGGER.info(String.format("Выполняю запрос сохранения черновика\n%s",
        describeBusinessObject(draft)));
    var response =
        given()
            .spec(pageDraftControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", pageId)
            .body(draft)
            .when().put()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_OK);
    publishDraft(pageId, widget.getDevice());
  }

  /**
   * Publish draft.
   * @param pageId page
   */
  private void publishDraft(final Integer pageId, final Device device) {
    Response response;
    LOGGER.info("Выполняю запрос публикации черновика страницы с ID: " + pageId);
    response =
        given()
            .spec(pageDraftControllerPublishSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", pageId)
            .queryParams("device", device)
            .when().post()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_OK);
  }

  /**
   * Share a Widget to another Page.
   * @param widget widget
   * @param page page
   */
  protected void shareWidgetToAnotherPage(final Widget widget,
                                          final Page page) {
    Response response;
    var widgetsList = page.getWidgetList()
        .stream()
        .map(Widget::getUid)
        .collect(Collectors.toList());
    widgetsList.add(widget.getUid());
    var widgetPlacementOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setChildUids(widgetsList)
            .build(),
        PAGE.toString(),
        CHANGE_LINKS.toString(),
        page.getId());
    var draft = new WrapperDraft(
        List.of(widgetPlacementOperation),
        widget.getDevice());
    LOGGER.info(String.format("Выполняю запрос сохранения черновика шаринга\n%s",
        describeBusinessObject(draft)));
    response =
        given()
            .spec(pageDraftControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", page.getId())
            .body(draft)
            .when().put()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_OK);
    publishDraft(page.getId(), widget.getDevice());
    createdPages.get(page.getId()).getWidgetList().add(widget);
  }

  /**
   * Activate Experiment assuming success.
   * @param experiment experiment
   * @return activated experiment
   */
  protected Experiment runExperimentAssumingSuccess(final Experiment experiment) {
    var runnedExperiment = new Experiment.Builder().setEnabled(true).build();
    LOGGER.info("Выполняю запрос на активацию эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getDeletePatchExperimentSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(runnedExperiment)
        .when().patch()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    assertThat(response.statusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_OK);
    runnedExperiment = response.as(Experiment.class);
    assertThat(runnedExperiment.getStatus())
        .as("Проверка статуса эксперимента")
        .isEqualTo(RUNNING);
    createdExperiments.replace(experiment.getUuid(), runnedExperiment);
    return runnedExperiment;
  }

  /**
   * Activate Experiment assuming fail.
   * @param experiment experiment
   * @return activated experiment
   */
  protected Response runExperimentAssumingFail(final Experiment experiment) {
    var body = new Experiment.Builder().setEnabled(true).build();
    LOGGER.info("Выполняю запрос на активацию эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getDeletePatchExperimentSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(body)
            .when().patch()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }

  /**
   * Deactivate Experiment assuming success.
   * @param experiment experiment
   * @return stopped experiment
   */
  protected Experiment stopExperimentAssumingSuccess(final Experiment experiment) {
    var experiment2beStopped = new Experiment.Builder().setEnabled(false).build();
    LOGGER.info("Выполняю запрос на активацию эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getDeletePatchExperimentSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(experiment2beStopped)
            .when().patch()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    assertThat(response.statusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_OK);
    final var stoppedExperiment = response.as(Experiment.class);
    assertThat(stoppedExperiment.getStatus())
        .as("Проверка статуса эксперимента")
        .isEqualTo(CANCELLED);
    createdExperiments.replace(experiment.getUuid(), stoppedExperiment);
    return stoppedExperiment;
  }

  /**
   * Deactivate Experiment assuming fail.
   * @param experiment experiment
   * @return response
   */
  protected Response stopExperimentAssumingFail(final Experiment experiment) {
    var experiment2beStopped = new Experiment.Builder().setEnabled(false).build();
    LOGGER.info("Выполняю запрос на деактивацию эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getDeletePatchExperimentSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(experiment2beStopped)
            .when().patch()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }

  /**
   * Delete Experiment.
   * @param experiment experiment
   * @return response
   */
  protected Response deleteExperiment(Experiment experiment) {
    LOGGER.info("Выполняю запрос на удаление эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getDeletePatchExperimentSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .when().delete()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }

  /**
   * Read experiment.
   * @param experiment experiment
   * @return experiment
   */
  protected Experiment getExperiment(final Experiment experiment) {
    LOGGER.info("Выполняю запрос на чтение эксперимента");
    final var response =
        given().spec(getDeletePatchExperimentSpec).auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("uuid", experiment.getUuid()).get();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response.as(Experiment.class);
  }

  /**
   * Read absent experiment.
   * @param experiment experiment
   * @return response
   */
  protected Response getAbsentExperiment(final Experiment experiment) {
    LOGGER.info("Выполняю запрос на чтение отсутствующего эксперимента");
    final var response =
        given().spec(getDeletePatchExperimentSpec).auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("uuid", experiment.getUuid()).get();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }

  /**
   * Read experiment.
   * @param option option
   * @return option
   */
  protected Option getOption(final Option option) {
    LOGGER.info("Выполняю запрос на чтение варианта");
    final var response =
        given().spec(getDeletePatchOptionSpec).auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("optionUuid", option.getUuid()).get();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    return response.as(Option.class);
  }

  /**
   * Read absent option.
   * @param option option
   * @return response
   */
  protected Response getAbsentOption(final Option option) {
    LOGGER.info("Выполняю запрос на чтение отсутствующего варианта");
    final var response =
        given().spec(getDeletePatchOptionSpec).auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("optionUuid", option.getUuid()).get();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }

  /**
   * Create new Option.
   * @param isDefault isDefault
   * @param widgetUids widgetUids
   * @param experimentUuid experimentUuid
   * @param trafficRate trafficRate
   * @return option
   */
  protected Option createOption(final Boolean isDefault,
                                final List<String> widgetUids,
                                final String experimentUuid,
                                final Double trafficRate) {
    Response response;
    var option =
        new Option.Builder()
            .setName(randomAlphanumeric(16))
            .setDescription(randomAlphanumeric(500))
            .setDefault(isDefault)
            .setExperimentUuid(experimentUuid)
            .setTrafficRate(trafficRate)
            .setWidgetUids(widgetUids)
            .build();
    LOGGER.info("Выполняю запрос на создание варианта:\n" + describeBusinessObject(option));
    response =
        given()
            .spec(getAllOrDeleteOptionSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("experimentUuid", experimentUuid)
            .body(option)
        .when().post()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_CREATED);
    option = response.as(Option.class);
    return option;
  }

  /**
   * Create new Experiment.
   * @param device device
   * @param pageId pageId
   * @param productType productType
   * @param endDate endDate
   * @param trafficRate trafficRate
   * @return created Experiment
   */
  protected Experiment createExperiment(final Device device,
                                        final Integer pageId,
                                        final ProductType productType,
                                        final String endDate,
                                        final Double trafficRate) {
    var experiment = new Experiment.Builder()
        .setDescription(randomAlphanumeric(50))
        .setCookieValue(randomAlphanumeric(50))
        .setPageId(pageId)
        .setDevice(device)
        .setProductTypeKey(productType)
        .setEndDate(endDate)
        .setTrafficRate(trafficRate)
        .build();
    LOGGER.info("Выполняю запрос на создание эксперимента:\n"
        + describeBusinessObject(experiment));
    var response =
        given()
            .spec(getAllOrCreateExperimentSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .body(experiment)
        .when().post()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_CREATED);
    experiment = response.then().extract().as(Experiment.class);
    return experiment;
  }

  /**
   * Get Widgets list from CS.
   * @param pageId page ID
   * @param device device
   * @return list of widgets
   */
  protected List<Widget> getWidgetsList(final Integer pageId,
                                        final Device device) {
    LOGGER.info(String.format(
        "Запрос страницы '%s' в '/metaInfoContentPageController'",
        pageId));
    Response response =
        given()
            .spec(metaInfoContentPageControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .queryParam("device", device)
            .queryParam("pageId", pageId)
            .when().get()
            .then().extract().response();
    assertThat(response.statusCode()).isEqualTo(SC_OK);
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s,",
        response.getStatusLine(),
        response.prettyPrint()));
    return Arrays.asList(response.as(Widget[].class));
  }
}
