package ru.alfabank.platform.experiment.update.negative;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ProductType.CC;
import static ru.alfabank.platform.businessobjects.enums.ProductType.ERR;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.ProductType;
import ru.alfabank.platform.businessobjects.enums.Status;

public class UpdateInactiveExperimentTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(UpdateInactiveExperimentTest.class);

  private Experiment experiment;

  /**
   * Before method.
   */
  @BeforeMethod(description = "Создание неактивного эксперимента "
      + "для негативного теста изменения неактивного эксперимента")
  public void beforeMethod() {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(start, end, true, getContentManager());
    final var randomAlphanumeric = randomAlphanumeric(50);
    experiment =
        given()
            .spec(getAllOrCreateExperimentSpec)
            .auth().oauth2(getContentManager().getJwt().getAccessToken())
            .body(
                new Experiment.Builder()
                    .setDevice(desktop)
                    .setCookieValue(randomAlphanumeric)
                    .setDescription(randomAlphanumeric)
                    .setPageId(page.getId())
                    .setProductTypeKey(getRandomProductType())
                    .setEndDate(getValidEndDate())
                    .setTrafficRate(0.50D)
                    .build())
            .when()
            .post()
            .then()
            .statusCode(SC_CREATED)
            .extract().as(Experiment.class);
    createdExperiments.put(experiment.getUuid(), experiment);
  }

  @Test (
      description = "Негативный тест изменения неактивного эксперимента",
      dataProvider = "inactiveWidgetNegativeDataProvider")
  public void negativeInactiveExperimentUpdateTest(
      @ParameterKey ("Test Case") final String testCase,
      @ParameterKey("New Value") final Object newValue) {
    LOGGER.info("Test case: " + testCase);
    var field2bChanged = StringUtils.substringBetween(testCase, "'");
    Experiment changeSetBody;
    switch (field2bChanged) {
      case "body" : {
        changeSetBody = new Experiment.Builder()
            .build();
        break;
      }
      case "cookieValue" : {
        changeSetBody = new Experiment.Builder()
            .setCookieValue((String) newValue)
            .build();
        break;
      }
      case "description" : {
        changeSetBody = new Experiment.Builder()
            .setDescription((String) newValue)
            .build();
        break;
      }
      case "productTypeKey" : {
        changeSetBody = new Experiment.Builder()
            .setProductTypeKey((ProductType) newValue)
            .build();
        break;
      }
      case "endDate" : {
        changeSetBody = new Experiment.Builder()
            .setEndDate((String) newValue)
            .build();
        break;
      }
      case "trafficRate" : {
        changeSetBody = new Experiment.Builder()
            .setTrafficRate((Double) newValue)
            .build();
        break;
      }
      case "enabled/cookieValue" : {
        changeSetBody = new Experiment.Builder()
            .setEnabled((Boolean) newValue)
            .setCookieValue(randomAlphanumeric(100))
            .build();
        break;
      }
      case "enabled/description" : {
        changeSetBody = new Experiment.Builder()
            .setEnabled((Boolean) newValue)
            .setDescription(randomAlphanumeric(100))
            .build();
        break;
      }
      case "enabled/productTypeKey" : {
        changeSetBody = new Experiment.Builder()
            .setEnabled((Boolean) newValue)
            .setProductTypeKey(CC)
            .build();
        break;
      }
      case "enabled/endDate" : {
        changeSetBody = new Experiment.Builder()
            .setEnabled((Boolean) newValue)
            .setEndDate(getValidEndDate())
            .build();
        break;
      }
      case "enabled/trafficRate" : {
        changeSetBody = new Experiment.Builder()
            .setEnabled((Boolean) newValue)
            .setTrafficRate(0.23D)
            .build();
        break;
      }
      case "enabled" : {
        changeSetBody = new Experiment.Builder()
            .setEnabled((Boolean) newValue)
            .setCookieValue(randomAlphanumeric(100))
            .setDescription(randomAlphanumeric(100))
            .setProductTypeKey(CC)
            .setEndDate(getValidEndDate())
            .setTrafficRate(0.23D)
            .build();
        break;
      }
      case "pageId" : {
        changeSetBody = new Experiment.Builder()
            .setPageId((Integer) newValue)
            .build();
        break;
      }
      case "uuid" : {
        changeSetBody = new Experiment.Builder()
            .setUuid((String) newValue)
            .build();
        break;
      }
      case "device" : {
        changeSetBody = new Experiment.Builder()
            .setDevice((Device) newValue)
            .build();
        break;
      }
      case "createdBy" : {
        changeSetBody = new Experiment.Builder()
            .setCreatedBy((String) newValue)
            .build();
        break;
      }
      case "activatedBy" : {
        changeSetBody = new Experiment.Builder()
            .setActivatedBy((String) newValue)
            .build();
        break;
      }
      case "creationDate" : {
        changeSetBody = new Experiment.Builder()
            .setCreationDate((String) newValue)
            .build();
        break;
      }
      case "activationDate" : {
        changeSetBody = new Experiment.Builder()
            .setActivationDate((String) newValue)
            .build();
        break;
      }
      case "deactivatedBy" : {
        changeSetBody = new Experiment.Builder()
            .setActivatedBy((String) newValue)
            .build();
        break;
      }
      case "deactivationDate" : {
        changeSetBody = new Experiment.Builder()
            .setDeactivationDate((String) newValue)
            .build();
        break;
      }
      case "status" : {
        changeSetBody = new Experiment.Builder()
            .setStatus((Status) newValue)
            .build();
        break;
      }
      default: {
        throw new IllegalArgumentException();
      }
    }
    LOGGER.info("Выполняю запрос изменения эксперимента:\n"
        + describeBusinessObject(changeSetBody));
    Response response = given().spec(getDeletePatchExperimentSpec)
        .auth().oauth2(getContentManager().getJwt().getAccessToken())
        .pathParam("uuid", experiment.getUuid())
        .body(changeSetBody)
        .when().patch()
        .then().log().ifError().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    // CHECKS //
    final var actual = given().spec(getDeletePatchExperimentSpec)
        .auth().oauth2(getContentManager().getJwt().getAccessToken())
        .pathParam("uuid", experiment.getUuid())
        .when().get().then().extract().as(Experiment.class);
    final var fieldViolations = response.getBody().jsonPath().getList("fieldViolations");
    final var globalErrors = response.getBody().jsonPath().getList("globalErrors");
    switch (field2bChanged) {
      case "cookieValue" : {
        response.then().statusCode(SC_BAD_REQUEST);
        assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'")
            .isNotNull();
        assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'")
            .isEqualTo(1);
        assertThat(fieldViolations.get(0).toString())
            .as("Проверка обрабоки некоррекнтого " + field2bChanged)
            .contains(field2bChanged, "Длина составной части для формирования кук должна быть в"
                + " интервале от 1 до 255 символов");
        break;
      }
      case "description" : {
        response.then().statusCode(SC_BAD_REQUEST);
        assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'")
            .isNotNull();
        assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'")
            .isEqualTo(1);
        assertThat(fieldViolations.get(0).toString())
            .as("Проверка обрабоки некоррекнтого " + field2bChanged)
            .contains(field2bChanged, "Длина описания эксперимента должна быть в интервале от 1 до "
                + "1000 символов");
        break;
      }
      case "productTypeKey" : {
        response.then().statusCode(SC_BAD_REQUEST);
        assertThat(response.getBody().jsonPath().getString("message"))
            .as("Проверка обрабоки некоррекнтого 'productTypeKey'")
            .contains("Тип продукта '" + ERR + "' не существует");
        break;
      }
      case "endDate" : {
        response.then().statusCode(SC_BAD_REQUEST);
        assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'").isNotNull();
        assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'").isEqualTo(1);
        assertThat(fieldViolations.get(0).toString())
            .as("Проверка обрабоки некоррекнтого " + field2bChanged)
            .contains(field2bChanged, "Минимально допустимая продолжительность эксперимента "
                + "составляет: 1 день");
        break;
      }
      case "trafficRate" : {
        response.then().statusCode(SC_BAD_REQUEST);
        assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'")
            .isNotNull();
        assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'")
            .isEqualTo(1);
        if ((Double) newValue < 0.01D) {
          assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки некоррекнтого " + field2bChanged)
              .contains(field2bChanged, "Доля трафика должна быть больше нуля");
        } else {
          assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки некоррекнтого " + field2bChanged)
              .contains(field2bChanged, "Доля трафика не может превышать единицу");
        }
        break;
      }
      default: {
        assertThat(fieldViolations)
            .as("Проверка отсутствия ошибок в 'fieldViolations'")
            .isNullOrEmpty();
        assertThat(globalErrors)
            .as("Проверка присутствия ошибок в 'globalErrors'")
            .isNotNull();
        assertThat(globalErrors.size())
            .as("Проверка количества ошибок в 'globalErrors'")
            .isEqualTo(1);
        assertThat(globalErrors.get(0).toString())
            .as("Проверка обрабоки некоррекнтого " + field2bChanged)
            .contains(
                "[[enabled], [description, cookieValue, endDate, trafficRate, productTypeKey]]");
        break;
      }
    }
    actual.checkUpdatedExperiment(experiment);
  }

  /**
   * Negative data provider.
   * @return test data
   */
  @DataProvider(name = "inactiveWidgetNegativeDataProvider")
  public Object[][] inactiveWidgetNegativeDataProvider() {
    return new Object[][]{
        {"Пустое 'body'", null},
        {"Длина значения поля 'cookieValue' < min", ""},
        {"Длина значения поля 'cookieValue' > max", randomAlphanumeric(256)},
        {"Длина значения поля 'description' < min", ""},
        {"Длина значения поля 'description' > max", randomAlphanumeric(1001)},
        {"Невалидное значение поля 'productTypeKey'", ERR},
        {"Указан 'endDate' менее чем +1 день", getInvalidEndDate()},
        {"значения поля 'trafficRate' < min", 0D},
        {"Длина значения поля 'trafficRate' > max", 1.01D},
        {"Одновременное изменение параметров и признака 'enabled'", true},
        {"Одновременное изменение параметра и признака 'enabled/cookieValue'", true},
        {"Одновременное изменение параметра и признака 'enabled/description'", true},
        {"Одновременное изменение параметра и признака 'enabled/productTypeKey'", true},
        {"Одновременное изменение параметра и признака 'enabled/endDate'", true},
        {"Одновременное изменение параметра и признака 'enabled/trafficRate'", true},
        {"Одновременное изменение всех параметров и признака 'enabled'", true},
        {"Изменение поля 'uuid'", UUID.randomUUID().toString()},
        {"Изменение поля 'pageId'", 11},
        {"Изменение поля 'device'", mobile},
        {"Изменение поля 'createdBy'", "random user"},
        {"Изменение поля 'activatedBy'", "random user"},
        {"Изменение поля 'creationDate'", getValidEndDate()},
        {"Изменение поля 'activationDate'", getValidEndDate()},
        {"Изменение поля 'deactivatedBy'", "random user"},
        {"Изменение поля 'deactivationDate'", getValidEndDate()},
        {"Изменение поля 'status'", RUNNING},
    };
  }
}
