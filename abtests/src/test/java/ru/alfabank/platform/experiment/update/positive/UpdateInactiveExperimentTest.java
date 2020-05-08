package ru.alfabank.platform.experiment.update.positive;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;

import com.epam.reportportal.annotations.ParameterKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.enums.ProductType;

public class UpdateInactiveExperimentTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(UpdateInactiveExperimentTest.class);

  private Experiment experiment;

  /**
   * Before method.
   */
  @BeforeMethod(description = "Создание неактивного эксперимента "
      + "для позитивного теста изменения неактивного эксперимента")
  public void beforeMethod() {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(start, end, true);
    final var randomAlphanumeric = randomAlphanumeric(50);
    experiment =
        given()
            .spec(getAllOrCreateExperimentSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
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
      description = "Позитивный тест изменения неактивного эксперимента",
      dataProvider = "Positive data provider")
  public void positiveInactiveExperimentUpdateTest(
      @ParameterKey("Test Case") final String testCase,
      @ParameterKey("New Value") final Object newValue) {
    LOGGER.info("Test case: " + testCase);
    var field2bChanged = StringUtils.substringBetween(testCase, "'");
    Experiment expected;
    Experiment changeSetBody;
    switch (field2bChanged) {
      case "cookieValue" : {
        expected = new Experiment.Builder()
            .using(experiment)
            .setCookieValue((String) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setCookieValue((String) newValue)
            .build();
        break;
      }
      case "description" : {
        expected = new Experiment.Builder()
            .using(experiment)
            .setDescription((String) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setDescription((String) newValue)
            .build();
        break;
      }
      case "productTypeKey" : {
        expected = new Experiment.Builder()
            .using(experiment)
            .setProductTypeKey((ProductType) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setProductTypeKey((ProductType) newValue)
            .build();
        break;
      }
      case "trafficRate" : {
        expected = new Experiment.Builder()
            .using(experiment)
            .setTrafficRate((Double) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setTrafficRate((Double) newValue)
            .build();
        break;
      }
      case "endDate" : {
        expected =
            new Experiment.Builder()
            .using(experiment)
            .setEndDate((String) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setEndDate((String) newValue)
            .build();
        break;
      }
      default: {
        throw new IllegalArgumentException();
      }
    }
    LOGGER.info("Выполняю запрос на изменение:\n" + describeBusinessObject(changeSetBody));
    var response = given()
        .spec(getDeletePatchExperimentSpec)
        .auth().oauth2(getToken(USER).getAccessToken())
        .pathParam("uuid", experiment.getUuid())
        .body(changeSetBody)
        .when().patch()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isEqualTo(SC_OK);
    var updatedExperiment = response.as(Experiment.class);
    updatedExperiment.checkUpdatedExperiment(expected);
  }

  /**
   * Positive data provider.
   * @return test data
   */
  @DataProvider(name = "Positive data provider")
  public Object[][] positiveDataProvider() {
    return new Object[][]{
        {"Минимальная длина значения 'cookieValue'", randomAlphanumeric(1)},
        {"Максимальная длина значения 'cookieValue'", randomAlphanumeric(255)},
        {"Минимальная длина значения 'description'", randomAlphanumeric(1)},
        {"Максимальная длина значения 'description'", randomAlphanumeric(1000)},
        {"Изменение 'productTypeKey'", getRandomProductType()},
        {"Минимальное значение 'trafficRate'", 0.01D},
        {"Максимальное значение 'trafficRate'", 1.00D},
        {"Изменение 'endDate'", getValidEndDatePlusOneMonth()}
    };
  }
}
