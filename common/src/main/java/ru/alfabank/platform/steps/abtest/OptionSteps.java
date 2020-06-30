package ru.alfabank.platform.steps.abtest;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;

import io.reactivex.annotations.NonNull;
import io.restassured.response.Response;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.abtests.Option;
import ru.alfabank.platform.users.AccessibleUser;

public class OptionSteps extends ExperimentSteps {

  private static final Logger LOGGER = LogManager.getLogger(OptionSteps.class);

  /**
   * Create new Option assuming fail.
   *
   * @param isDefault      isDefault
   * @param widgetUids     widgetUids
   * @param experimentUuid experimentUuid
   * @param trafficRate    trafficRate
   * @param user           user
   * @return response
   */
  public Response createOptionAssumingFail(final Boolean isDefault,
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
            .when().post()
            .then().extract().response();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Create new Option.
   *
   * @param option option
   * @param user   user
   * @return option
   */
  @NonNull
  public Option createOption(final Option option,
                             final AccessibleUser user) {
    Response response;
    LOGGER.info("Выполняю запрос на создание варианта:\n" + describeBusinessObject(option));
    response = given()
        .spec(getGetAllOrDeleteOptionSpec())
        .auth().oauth2(user.getJwt().getAccessToken())
        .pathParam("experimentUuid", option.getExperimentUuid())
        .body(option)
        .when().post()
        .then().extract().response();
    describeResponse(LOGGER, response);
    response.then().statusCode(SC_CREATED);
    return response.as(Option.class);
  }

  /**
   * Modify option.
   *
   * @param option2beModified option to be modified
   * @param modification      modification
   * @param user              user
   * @return modified option
   */
  public Option modifyOption(final Option option2beModified,
                             final Option modification,
                             final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на изменение варианта с:\n"
        + describeBusinessObject(option2beModified) + "\nна:\n"
        + describeBusinessObject(modification));
    final var response =
        given()
            .spec(getGetDeletePatchOptionSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("optionUuid", option2beModified.getUuid())
            .body(modification)
            .when().patch()
            .then().extract().response();
    describeResponse(LOGGER, response);
    assertThat(response.statusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_OK);
    return response.as(Option.class);
  }

  /**
   * Modify option.
   *
   * @param option2beModified option to be modified
   * @param modification      modification
   * @param user              user
   * @return modified option
   */
  public Response modifyOptionAssumingFail(final Option option2beModified,
                                           final Option modification,
                                           final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на изменение варианта с:\n"
        + describeBusinessObject(option2beModified) + "\nна:\n"
        + describeBusinessObject(modification));
    final var response =
        given()
            .spec(getGetDeletePatchOptionSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("optionUuid", option2beModified.getUuid())
            .body(modification)
            .when().patch()
            .then().extract().response();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Delete option.
   *
   * @param option2beDeleted option to be deleted
   * @param user             user
   */
  public void deleteOption(final Option option2beDeleted,
                           final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на удаление варианта:\n"
        + describeBusinessObject(option2beDeleted));
    final var response =
        given()
            .spec(getGetDeletePatchOptionSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("optionUuid", option2beDeleted.getUuid())
            .when().delete()
            .then().extract().response();
    describeResponse(LOGGER, response);
    assertThat(response.statusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_NO_CONTENT);
  }

  /**
   * Delete option.
   *
   * @param option2beDeleted option to be deleted
   * @param user             user
   * @return respomse
   */
  public Response deleteOptionAssumingFail(final Option option2beDeleted,
                                           final AccessibleUser user) {
    LOGGER.info("Выполняю запрос на удаление варианта:\n"
        + describeBusinessObject(option2beDeleted));
    final var response =
        given()
            .spec(getGetDeletePatchOptionSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("optionUuid", option2beDeleted.getUuid())
            .when().delete()
            .then().extract().response();
    describeResponse(LOGGER, response);
    return response;
  }
}
