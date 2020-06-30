package ru.alfabank.platform.steps.abtest;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.businessobjects.enums.Status.CANCELLED;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.alfabank.platform.businessobjects.abtests.Experiment;
import ru.alfabank.platform.businessobjects.abtests.Experiment.Builder;
import ru.alfabank.platform.businessobjects.abtests.Option;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.ProductType;
import ru.alfabank.platform.steps.BaseSteps;
import ru.alfabank.platform.users.AccessibleUser;

public class ExperimentSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(ExperimentSteps.class);

  /**
   * Delete created experiments.
   */
  public void deleteCreatedExperiments() {
    final var experimentsCount = CREATED_EXPERIMENTS.size();
    LOGGER.info(String.format("Экспериментов к удалению: %d", experimentsCount));
    if (experimentsCount > 0) {
      CREATED_EXPERIMENTS.forEach((key, experiment) -> {
        if (getAbsentExperimentByUuid(experiment.getUuid(), getContentManager()).getStatusCode()
            != SC_OK) {
          LOGGER.info(String.format("Эксперимент '%s' не найден", experiment.getUuid()));
        } else {
          LOGGER.info(String.format(
              "Начинаю процесс удаления эксперимента '%s'",
              experiment.getUuid()));
          if (getExistingExperiment(experiment, getContentManager()).getStatus() == RUNNING) {
            LOGGER.info(String.format("Эксперимент '%s' нуждается в остановке", experimentsCount));
            final var stoppingResponse = given()
                .spec(getGetDeletePatchExperimentByUuidSpec())
                .auth().oauth2(getContentManager().getJwt().getAccessToken())
                .pathParam("uuid", experiment.getUuid())
                .body(new Experiment.Builder().setEnabled(false).build())
                .patch();
            if (stoppingResponse.getStatusCode() == SC_OK
                && getExistingExperiment(experiment, getContentManager()).getStatus() == RUNNING) {
              CREATED_EXPERIMENTS.replace(key, new Builder().setStatus(CANCELLED).build());
              LOGGER.info(String.format("Эксперимент '%s' остановлен", experiment.getUuid()));
              final var deletingResponse = given()
                  .spec(getGetDeletePatchExperimentByUuidSpec())
                  .auth().oauth2(getContentManager().getJwt().getAccessToken())
                  .pathParam("uuid", experiment.getUuid())
                  .delete();
              if (deletingResponse.getStatusCode() == SC_OK) {
                LOGGER.info(String.format("Эксперимент '%s' удалён", experiment.getUuid()));
              } else {
                LOGGER.warn(String.format(
                    "Экспиремент не был удалён:\n'%s'\n%s",
                    deletingResponse.getStatusCode(),
                    deletingResponse.prettyPrint()));
              }
            } else {
              LOGGER.warn(String.format(
                  "Экспиремент не был остановлен:\n'%s'\n%s",
                  stoppingResponse.getStatusCode(),
                  stoppingResponse.prettyPrint()));
            }
          }
        }
      });
    }
  }

  /**
   * Activate Experiment assuming success.
   *
   * @param experiment experiment
   * @param user       user
   * @return activated experiment
   */
  public Experiment runExperimentAssumingSuccess(final Experiment experiment,
                                                 final AccessibleUser user) {
    var runningExperiment = new Experiment.Builder().setEnabled(true).build();
    LOGGER.info("Выполняю запрос на активацию эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getGetDeletePatchExperimentByUuidSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(runningExperiment)
            .patch();
    describeResponse(LOGGER, response);
    assertThat(response.statusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_OK);
    runningExperiment = response.as(Experiment.class);
    assertThat(runningExperiment.getStatus())
        .as("Проверка статуса эксперимента")
        .isEqualTo(RUNNING);
    CREATED_EXPERIMENTS.replace(experiment.getUuid(), runningExperiment);
    return runningExperiment;
  }

  /**
   * Activate Experiment assuming fail.
   *
   * @param experiment experiment
   * @param user       user
   * @return activated experiment
   */
  public Response runExperimentAssumingFail(final Experiment experiment,
                                            final AccessibleUser user) {
    var body = new Experiment.Builder().setEnabled(true).build();
    LOGGER.info("Выполняю запрос на активацию эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getGetDeletePatchExperimentByUuidSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(body)
            .patch();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Deactivate Experiment assuming success.
   *
   * @param experiment experiment
   * @param user       user
   * @return stopped experiment
   */
  public Experiment stopExperimentAssumingSuccess(final Experiment experiment,
                                                  final AccessibleUser user) {
    var experiment2beStopped = new Experiment.Builder().setEnabled(false).build();
    LOGGER.info("Выполняю запрос на активацию эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getGetDeletePatchExperimentByUuidSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(experiment2beStopped)
            .patch();
    describeResponse(LOGGER, response);
    assertThat(response.statusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_OK);
    final var stoppedExperiment = response.as(Experiment.class);
    assertThat(stoppedExperiment.getStatus())
        .as("Проверка статуса эксперимента")
        .isEqualTo(CANCELLED);
    CREATED_EXPERIMENTS.replace(experiment.getUuid(), stoppedExperiment);
    return stoppedExperiment;
  }

  /**
   * Deactivate Experiment assuming fail.
   *
   * @param experiment experiment
   * @param user       user
   * @return response
   */
  public Response stopExperimentAssumingFail(final Experiment experiment,
                                             final AccessibleUser user) {
    var experiment2beStopped = new Experiment.Builder().setEnabled(false).build();
    LOGGER.info("Выполняю запрос на деактивацию эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getGetDeletePatchExperimentByUuidSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(experiment2beStopped)
            .patch();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Delete Experiment.
   *
   * @param experiment experiment
   * @param user       user
   * @return response
   */
  public Response deleteExperiment(final Experiment experiment,
                                   final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на удаление эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getGetDeletePatchExperimentByUuidSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .delete();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Read existing experiment.
   *
   * @param experiment Experiment
   * @param user       user
   * @return experiment
   */
  public Experiment getExistingExperiment(final Experiment experiment,
                                          final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на чтение эксперимента");
    final var response =
        given()
            .spec(getGetDeletePatchExperimentByUuidSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .get();
    describeResponse(LOGGER, response);
    return response.as(Experiment.class);
  }

  /**
   * Get absent experiment by its UUID.
   *
   * @param experimentUuid Experiment UUID
   * @param user           user
   * @return response
   */
  public Response getAbsentExperimentByUuid(@NotNull final String experimentUuid,
                                            @NotNull final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на чтение, возможно, отсутствующего эксперимента по UUID");
    final var response = given()
        .spec(getGetDeletePatchExperimentByUuidSpec())
        .auth().oauth2(user.getJwt().getAccessToken())
        .pathParams("uuid", experimentUuid)
        .get();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Get absent experiment by Page ID.
   *
   * @param pageId Page ID
   * @param user   User
   * @return response
   */
  public Response getAbsentExperimentByPageId(@NotNull final Integer pageId,
                                              @NotNull final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на чтение отсутствующего эксперимента по 'Page ID'");
    final var response = given()
        .spec(getGetAllOrCreateExperimentSpec())
        .auth().oauth2(user.getJwt().getAccessToken())
        .queryParams("pageId.equals", String.valueOf(pageId))
        .get();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Read experiment.
   *
   * @param option option
   * @param user   user
   * @return option
   */
  public Option getOption(final Option option,
                          final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на чтение варианта");
    final var response =
        given()
            .spec(getGetDeletePatchOptionSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("optionUuid", option.getUuid())
            .get();
    describeResponse(LOGGER, response);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    return response.as(Option.class);
  }

  /**
   * Read absent option.
   *
   * @param option option
   * @param user   user
   * @return response
   */
  public Response getAbsentOption(final Option option,
                                  final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на чтение отсутствующего варианта");
    final var response =
        given()
            .spec(getGetDeletePatchOptionSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("optionUuid", option.getUuid())
            .get();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Create new Option.
   *
   * @param isDefault      isDefault
   * @param widgetUids     widgetUids
   * @param experimentUuid experimentUuid
   * @param trafficRate    trafficRate
   * @param user           user
   * @return option
   */
  public Option createOption(final Boolean isDefault,
                             final List<String> widgetUids,
                             final String experimentUuid,
                             final Double trafficRate,
                             final AccessibleUser user) {
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
            .spec(getGetAllOrDeleteOptionSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("experimentUuid", experimentUuid)
            .body(option)
            .post();
    describeResponse(LOGGER, response);
    response.then().statusCode(SC_CREATED);
    option = response.as(Option.class);
    return option;
  }

  /**
   * Create new Experiment.
   *
   * @param device      device
   * @param pageId      pageId
   * @param productType productType
   * @param endDate     endDate
   * @param trafficRate trafficRate
   * @param user        user
   * @return created Experiment
   */
  public Experiment createExperiment(final Device device,
                                     final Integer pageId,
                                     final ProductType productType,
                                     final String endDate,
                                     final Double trafficRate,
                                     final AccessibleUser user) {
    return createExperiment(
        new Builder()
            .setDescription(randomAlphanumeric(50))
            .setCookieValue(randomAlphanumeric(50))
            .setPageId(pageId)
            .setDevice(device)
            .setProductTypeKey(productType)
            .setEndDate(endDate)
            .setTrafficRate(trafficRate)
            .build(),
        user);
  }

  /**
   * Create new Experiment.
   *
   * @param experiment experiment
   * @return experiment
   */
  public Experiment createExperiment(final Experiment experiment,
                                     final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на создание эксперимента:\n"
        + describeBusinessObject(experiment));
    var response =
        given()
            .spec(getGetAllOrCreateExperimentSpec()).log().all()
            .auth().oauth2(user.getJwt().getAccessToken())
            .body(experiment)
            .post();
    describeResponse(LOGGER, response);
    assertThat(response.statusCode())
        .as("Проверка статус-кода ответа")
        .isEqualTo(SC_CREATED);
    final var created_experiment = response.as(Experiment.class);
    CREATED_EXPERIMENTS.put(created_experiment.getUuid(), created_experiment);
    return created_experiment;
  }

  /**
   * Create new Experiment assuming fail.
   *
   * @param device      device
   * @param pageId      pageId
   * @param productType productType
   * @param endDate     endDate
   * @param trafficRate trafficRate
   * @param user        user
   * @return response
   */
  public Response createExperimentAssumingFail(final String description,
                                               final String cookieValue,
                                               final Device device,
                                               final Integer pageId,
                                               final ProductType productType,
                                               final String endDate,
                                               final Double trafficRate,
                                               final AccessibleUser user) {
    return createExperimentAssumingFail(
        new Builder()
            .setDescription(description)
            .setCookieValue(cookieValue)
            .setPageId(pageId)
            .setDevice(device)
            .setProductTypeKey(productType)
            .setEndDate(endDate)
            .setTrafficRate(trafficRate)
            .build(),
        user);
  }

  /**
   * Create new Experiment assuming fail.
   *
   * @param experiment experiment
   * @param user       user
   * @return response
   */
  public Response createExperimentAssumingFail(final Experiment experiment,
                                               final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на создание эксперимента:\n"
        + describeBusinessObject(experiment));
    final var response =
        given()
            .spec(getGetAllOrCreateExperimentSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .body(experiment)
            .post();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Update existed Experiment.
   *
   * @param experiment    experiment
   * @param changeSetBody changeSetBody
   * @param user          user
   * @return updated experiment
   */
  public Experiment modifyExperiment(final Experiment experiment,
                                     final Experiment changeSetBody,
                                     final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на изменение:\n" + describeBusinessObject(changeSetBody));
    var response =
        given()
            .spec(getGetDeletePatchExperimentByUuidSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(changeSetBody)
            .patch();
    describeResponse(LOGGER, response);
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_OK);
    return response.as(Experiment.class);
  }

  /**
   * Update Experiment assuming fail.
   *
   * @param experiment    experiment
   * @param changeSetBody changeSetBody
   * @param user          user
   * @return response
   */
  public Response modifyExperimentAssumingFail(final Experiment experiment,
                                               final Experiment changeSetBody,
                                               final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на изменение:\n" + describeBusinessObject(changeSetBody));
    var response =
        given()
            .spec(getGetDeletePatchExperimentByUuidSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(changeSetBody)
            .patch();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Получение флага.
   *
   * @param pageId    pageId
   * @param device    device
   * @param geoGroups geos
   * @param user      user
   * @return response
   */
  public Response getInvolvements(final Integer pageId,
                                  final Device device,
                                  final List<String> geoGroups,
                                  final AccessibleUser user) {
    LOGGER.info(String.format(
        "Выполняю запрос на получение флага для страницы '%d', девайса '%s' и гео-групп '%s'",
        pageId, device, Arrays.toString(geoGroups.toArray())));
    final var response =
        given()
            .spec(getInvolvementsSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .queryParam("pageId", pageId)
            .queryParam("device", device)
            .queryParam("geoGroups", geoGroups)
            .when().get()
            .then().extract().response();
    describeResponse(LOGGER, response);
    return response;
  }
}
