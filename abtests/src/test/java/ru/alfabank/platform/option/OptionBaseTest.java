package ru.alfabank.platform.option;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;

import io.restassured.response.Response;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.enums.User;

public class OptionBaseTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(OptionBaseTest.class);

  /**
   * Create new Option assuming fail.
   * @param isDefault isDefault
   * @param widgetUids widgetUids
   * @param experimentUuid experimentUuid
   * @param trafficRate trafficRate
   * @return response
   */
  protected Response createOptionAssumingFail(final Boolean isDefault,
                                              final List<String> widgetUids,
                                              final String experimentUuid,
                                              final Double trafficRate) {
    setUser(User.CONTENT_MANAGER);
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
            .auth().oauth2(getToken(getUser()).getAccessToken())
            .pathParam("experimentUuid", experimentUuid)
            .body(option)
        .when().post()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }

  /**
   * Create new Option.
   * @param option option
   * @return option
   */
  protected Option createOption(final Option option) {
    Response response;
    LOGGER.info("Выполняю запрос на создание варианта:\n" + describeBusinessObject(option));
    response = given()
            .spec(getAllOrDeleteOptionSpec)
            .auth().oauth2(getToken(getUser()).getAccessToken())
            .pathParam("experimentUuid", option.getExperimentUuid())
            .body(option)
        .when().post()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_CREATED);
    return response.as(Option.class);
  }

  /**
   * Modify option.
   * @param option2beModified option to be modified
   * @param modification modification
   * @return modified option
   */
  protected Option modifyOption(Option option2beModified, Option modification) {
    LOGGER.info("Выполняю запрос на изменение варианта с:\n"
        + describeBusinessObject(option2beModified) + "\nна:\n"
        + describeBusinessObject(modification));
    final var response =
        given()
            .spec(getDeletePatchOptionSpec)
            .auth().oauth2(getToken(getUser()).getAccessToken())
            .pathParam("optionUuid", option2beModified.getUuid())
            .body(modification)
        .when().patch()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    assertThat(response.statusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_OK);
    return response.as(Option.class);
  }

  /**
   * Modify option.
   * @param option2beModified option to be modified
   * @param modification modification
   * @return modified option
   */
  protected Response modifyOptionAssumingFail(Option option2beModified, Option modification) {
    LOGGER.info("Выполняю запрос на изменение варианта с:\n"
        + describeBusinessObject(option2beModified) + "\nна:\n"
        + describeBusinessObject(modification));
    final var response =
        given()
            .spec(getDeletePatchOptionSpec)
            .auth().oauth2(getToken(getUser()).getAccessToken())
            .pathParam("optionUuid", option2beModified.getUuid())
            .body(modification)
            .when().patch()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }

  /**
   * Delete option.
   * @param option2beDeleted option to be deleted
   */
  protected void deleteOption(Option option2beDeleted) {
    LOGGER.info("Выполняю запрос на удаление варианта:\n"
        + describeBusinessObject(option2beDeleted));
    final var response =
        given()
            .spec(getDeletePatchOptionSpec)
            .auth().oauth2(getToken(getUser()).getAccessToken())
            .pathParam("optionUuid", option2beDeleted.getUuid())
            .when().delete()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    assertThat(response.statusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_NO_CONTENT);
  }

  /**
   * Delete option.
   * @param option2beDeleted option to be deleted
   */
  protected Response deleteOptionAssumingFail(Option option2beDeleted) {
    LOGGER.info("Выполняю запрос на удаление варианта:\n"
        + describeBusinessObject(option2beDeleted));
    final var response =
        given()
            .spec(getDeletePatchOptionSpec)
            .auth().oauth2(getToken(getUser()).getAccessToken())
            .pathParam("optionUuid", option2beDeleted.getUuid())
            .when().delete()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }


  /**
   * Data provider.
   * @return test data
   */
  @DataProvider
  public static Object[][] dataProvider() {
    return new Object[][]{
        {true},
        {false}
    };
  }
}
