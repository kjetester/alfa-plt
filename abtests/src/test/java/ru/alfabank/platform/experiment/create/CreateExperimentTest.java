package ru.alfabank.platform.experiment.create;

import static io.restassured.RestAssured.given;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.businessobjects.enums.Device.all;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ProductType.CC;
import static ru.alfabank.platform.businessobjects.enums.ProductType.COM;
import static ru.alfabank.platform.businessobjects.enums.ProductType.DC;
import static ru.alfabank.platform.businessobjects.enums.ProductType.ERR;
import static ru.alfabank.platform.businessobjects.enums.ProductType.INV;
import static ru.alfabank.platform.businessobjects.enums.ProductType.MG;
import static ru.alfabank.platform.businessobjects.enums.ProductType.PIL;
import static ru.alfabank.platform.businessobjects.enums.ProductType.SME;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.ProductType;

public class CreateExperimentTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(CreateExperimentTest.class);

  @Test(
      description = "Позитивный тест создания нового эксперимента",
      dataProvider = "Positive data provider")
  public void positiveNewExperimentCreationTest(
      @ParameterKey("Experiment 1 device") final Device experiment1device,
      @ParameterKey("Experiment 1 cookieValue") final String experiment1cookieValue,
      @ParameterKey("Experiment 1 description") final String experiment1description,
      @ParameterKey("Experiment 1 pageId") final Integer experiment1pageId,
      @ParameterKey("Experiment 1 productType") final ProductType experiment1productType,
      @ParameterKey("Experiment 1 endDate") final String experiment1endDate,
      @ParameterKey("Experiment 1 trafficRate") final Double experiment1trafficRate,
      @ParameterKey("Experiment 2 device") final Device experiment2device,
      @ParameterKey("Experiment 2 cookieValue") final String experiment2cookieValue,
      @ParameterKey("Experiment 2 description") final String experiment2description,
      @ParameterKey("Experiment 2 pageId") final Integer experiment2pageId,
      @ParameterKey("Experiment 2 productType") final ProductType experiment2productType,
      @ParameterKey("Experiment 2 endDate") final String experiment2endDate,
      @ParameterKey("Experiment 2 trafficRate") final Double experiment2trafficRate) {
    final var experiment1ToCreate = new Experiment.Builder()
        .setDescription(experiment1description)
        .setCookieValue(experiment1cookieValue)
        .setPageId(experiment1pageId)
        .setDevice(experiment1device)
        .setProductTypeKey(experiment1productType)
        .setEndDate(experiment1endDate)
        .setTrafficRate(experiment1trafficRate)
        .build();
    LOGGER.info("Выполняю запрос на создание эксперимента №1:\n"
        + describeBusinessObject(experiment1ToCreate));
    Response experiment1creationResponse =
        given()
            .spec(getAllOrCreateExperimentSpec)
            .auth().oauth2(getContentManager().getJwt().getAccessToken())
            .body(experiment1ToCreate)
        .when().post()
        .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        experiment1creationResponse.getStatusCode(),
        experiment1creationResponse.prettyPrint()));
    assertThat(experiment1creationResponse.statusCode())
        .as("Проверка статус-кода ответа")
        .isEqualTo(SC_CREATED);
    final var createdExperiment1 = experiment1creationResponse.as(Experiment.class);
    createdExperiments.put(createdExperiment1.getUuid(), createdExperiment1);
    createdExperiment1.checkCreatedExperiment(
        new Experiment.Builder()
            .using(experiment1ToCreate)
            .setEnabled(false)
            .setCreatedBy(getContentManager().getLogin())
            .setStatus(DISABLED)
            .setCreationDate(LocalDateTime.now(ZoneOffset.UTC).toString())
            .build());
    final var experiment2ToCreate = new Experiment.Builder()
        .setDescription(experiment2description)
        .setCookieValue(experiment2cookieValue)
        .setPageId(experiment2pageId)
        .setDevice(experiment2device)
        .setProductTypeKey(experiment2productType)
        .setEndDate(experiment2endDate)
        .setTrafficRate(experiment2trafficRate)
        .build();
    LOGGER.info("Выполняю запрос на создание эксперимента №2:\n"
        + describeBusinessObject(experiment2ToCreate));
    Response experiment2creationResponse =
        given()
            .spec(getAllOrCreateExperimentSpec)
            .auth().oauth2(getContentManager().getJwt().getAccessToken())
            .body(experiment2ToCreate)
            .when().post()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        experiment2creationResponse.getStatusCode(),
        experiment2creationResponse.prettyPrint()));
    assertThat(experiment2creationResponse.statusCode())
        .as("Проверка статус-кода ответа")
        .isEqualTo(SC_CREATED);
    final var createdExperiment2 = experiment2creationResponse.as(Experiment.class);
    createdExperiments.put(createdExperiment2.getUuid(), createdExperiment2);
    createdExperiment2.checkCreatedExperiment(
        new Experiment.Builder()
            .using(experiment2ToCreate)
            .setEnabled(false)
            .setCreatedBy(getContentManager().getLogin())
            .setStatus(DISABLED)
            .setCreationDate(LocalDateTime.now(ZoneOffset.UTC).toString())
            .build());
  }

  /**
   * Data provider.
   * @return test data
   */
  @DataProvider(name = "Positive data provider")
  public Object[][] positiveDataProvider() {
    return new Object[][]{
        {
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            0.01D,
            mobile,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            mobile,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            PIL,
            getValidEndDate(),
            1.00D,
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            PIL,
            getValidEndDate(),
            1.00D
        },
        {
            desktop,
            randomAlphanumeric(255),
            randomAlphanumeric(1000),
            10,
            DC,
            getValidEndDate(),
            0.11D,
            mobile,
            randomAlphanumeric(255),
            randomAlphanumeric(1000),
            10,
            DC,
            getValidEndDate(),
            0.11D
        },
        {
            mobile,
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            10,
            MG,
            getValidEndDate(),
            0.11D,
            desktop,
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            10,
            MG,
            getValidEndDate(),
            0.11D
        },
        {
            desktop,
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            10,
            SME,
            getValidEndDate(),
            0.11D,
            mobile,
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            10,
            SME,
            getValidEndDate(),
            0.11D
        },
        {
            mobile,
            randomAlphanumeric(11),
            randomAlphanumeric(11),
            10,
            INV,
            getValidEndDate(),
            0.11D,
            desktop,
            randomAlphanumeric(11),
            randomAlphanumeric(11),
            10,
            INV,
            getValidEndDate(),
            0.11D
        },
        {
            desktop,
            randomAlphanumeric(10),
            randomAlphanumeric(10),
            10,
            COM,
            getValidEndDate(),
            0.11D,
            mobile,
            randomAlphanumeric(10),
            randomAlphanumeric(10),
            10,
            COM,
            getValidEndDate(),
            0.11D
        }
    };
  }

  @Test(
      description = "Негативный тест создания нового эксперимента",
      dataProvider = "Negative data provider")
  public void negativeNewExperimentCreationTest(
      @ParameterKey("testCase") final String testCase,
      @ParameterKey("device") final Device device,
      @ParameterKey("cookieValue") final String cookieValue,
      @ParameterKey("description") final String description,
      @ParameterKey("pageId") final Integer pageId,
      @ParameterKey("productType") final ProductType productType,
      @ParameterKey("endDate") final String endDate,
      @ParameterKey("trafficRate") final Double trafficRate) {
    LOGGER.info("Test case:" + testCase);
    final Response response = createExperimentAssumingFail(
        description, cookieValue, device, pageId, productType, endDate, trafficRate, getContentManager());
    final var softly = new SoftAssertions();
    softly.assertThat(response.statusCode()).isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    final var fieldViolations = response.getBody().jsonPath().getList("fieldViolations");
    final var globalErrors = response.getBody().jsonPath().getList("globalErrors");
    if (device != null && device.equals(all)) {
      assertThat(globalErrors)
          .as("Проверка отсутствия ошибок в 'globalErrors'")
          .isNullOrEmpty();
      assertThat(fieldViolations)
          .as("Проверка присутствия ошибок в 'fieldViolations'")
          .isNotNull();
      assertThat(fieldViolations.size())
          .as("Проверка количества ошибок в 'fieldViolations'")
          .isEqualTo(1);
      softly
          .assertThat(fieldViolations.get(0).toString())
          .as("Проверка обрабоки некоррекнтого значения 'device'")
          .contains("Поле может иметь только одно из возможных значений: [DESKTOP, MOBILE]. "
              + "Чувствительность к регистру - false");
    }
    if (device == null) {
      assertThat(globalErrors)
          .as("Проверка отсутствия ошибок в 'globalErrors'")
          .isNullOrEmpty();
      assertThat(fieldViolations)
          .as("Проверка присутствия ошибок в 'fieldViolations'")
          .isNotNull();
      assertThat(fieldViolations.size())
          .as("Проверка количества ошибок в 'fieldViolations'")
          .isEqualTo(1);
      softly
          .assertThat(fieldViolations.get(0).toString())
          .as("Проверка обрабоки отсутсвия значения 'device'")
          .contains("device", "Необходимо указать тип устройства");
    }
    if (cookieValue == null || cookieValue.length() < 1) {
      assertThat(globalErrors)
          .as("Проверка отсутствия ошибок в 'globalErrors'")
          .isNullOrEmpty();
      assertThat(fieldViolations)
          .as("Проверка присутствия ошибок в 'fieldViolations'")
          .isNotNull();
      assertThat(fieldViolations.size())
          .as("Проверка количества ошибок в 'fieldViolations'")
          .isEqualTo(1);
      softly
          .assertThat(fieldViolations.get(0).toString())
          .as("Проверка обрабоки некоррекнтого значения 'cookieValue'")
          .contains("cookieValue", "Должно быть указана составная часть формировании кук");
    }
    if (description == null || description.length() < 1) {
      assertThat(globalErrors)
          .as("Проверка отсутствия ошибок в 'globalErrors'")
          .isNullOrEmpty();
      assertThat(fieldViolations)
          .as("Проверка присутствия ошибок в 'fieldViolations'")
          .isNotNull();
      assertThat(fieldViolations.size())
          .as("Проверка количества ошибок в 'fieldViolations'")
          .isEqualTo(1);
      softly
          .assertThat(fieldViolations.get(0).toString())
          .as("Проверка обрабоки некоррекнтого значения 'description'")
          .contains("description", "Должно быть указано описание эксперимента");
    }
    if (pageId == null || pageId > 999) {
      if (pageId == null) {
        assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'")
            .isNotNull();
        assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'")
            .isEqualTo(1);
        softly
            .assertThat(fieldViolations.get(0).toString())
            .as("Проверка обрабоки отсутствующего значения 'pageId'")
            .contains("pageId", "Необходимо указать адрес страницы");
      } else {
        assertThat(fieldViolations)
            .as("Проверка отсутствия ошибок в 'fieldViolations'")
            .isNullOrEmpty();
        assertThat(globalErrors)
            .as("Проверка присутствия ошибок в 'globalErrors'")
            .isNotNull();
        assertThat(globalErrors.size())
            .as("Проверка количества ошибок в 'globalErrors'")
            .isEqualTo(1);
        softly
            .assertThat(globalErrors.get(0).toString())
            .as("Проверка обрабоки некоррекнтого значения 'pageId'")
            .containsIgnoringCase("Не найдено страницы с комбинацией pageId = "
                + pageId + " и device = " + device);
      }
    }
    if (productType != null && productType.equals(ERR)) {
      assertThat(response.getBody().asString())
          .as("роверка обрабоки некоррекнтого значения 'productType'")
          .contains("Тип продукта '" + productType + "' не существует");
    }
    if (productType == null) {
      assertThat(globalErrors)
          .as("Проверка отсутствия ошибок в 'globalErrors'")
          .isNullOrEmpty();
      assertThat(fieldViolations)
          .as("Проверка присутствия ошибок в 'fieldViolations'")
          .isNotNull();
      assertThat(fieldViolations.size())
          .as("Проверка количества ошибок в 'fieldViolations'")
          .isEqualTo(1);
      softly
          .assertThat(fieldViolations.get(0).toString())
          .as("Проверка обрабоки отсутсвия значения 'productType'")
          .contains("Необходимо указать ключ типа продукта");
    }
    if (endDate == null || LocalDateTime.parse(endDate, ISO_OFFSET_DATE_TIME)
        .isBefore(LocalDateTime.now().plusHours(21).minusMinutes(1))) {
      softly
          .assertThat(response.getBody().asString())
          .as("Проверка обрабоки некоррекнтого значения 'endDate'")
          .contains(endDate == null
              ? "Необходимо указать дату окончания эксперимента"
              : "Минимально допустимая продолжительность эксперимента составляет: 1 день");
    }
    if (trafficRate == null || trafficRate < 0.01D || trafficRate > 1D) {
      var stringAssert = softly.assertThat(response.getBody().asString())
          .as("Проверка обрабоки некоррекнтого значения 'trafficRate'");
      stringAssert.contains("trafficRate");
      if (trafficRate == null) {
        stringAssert.contains("Должна быть указана доля от трафика участвующая в эксперименте");
      } else if (trafficRate < 0.01D) {
        stringAssert.contains("Доля трафика должна быть больше");
      } else if (trafficRate > 1D) {
        stringAssert.contains("Доля трафика не может превышать единицу");
      }
    }
    softly.assertAll();
  }


  /**
   * Data provider.
   * @return test data
   */
  @DataProvider(name = "Negative data provider")
  public Object[][] negativeDataProvider() {
    return new Object[][]{
        {
            "Отсутствует поле 'device'",
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'cookieValue'",
            desktop,
            null,
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'description'",
            desktop,
            randomAlphanumeric(1),
            null,
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'pageId'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            null,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'productType'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            null,
            getValidEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'endDate'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            null,
            0.01D
        },
        {
            "Отсутствует поле 'trafficRate'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            null
        },
        {
            "Указан несуществующий 'device'",
            all,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Длина строки в поле 'cookieValue' < min",
            desktop,
            randomAlphanumeric(0),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Длина строки в поле 'cookieValue' > max",
            desktop,
            randomAlphanumeric(256),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Длина строки в поле 'description' < min",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(0),
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Длина строки в поле 'description' > max",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(10001),
            10,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Указан несуществующий 'pageId'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10000,
            CC,
            getValidEndDate(),
            0.01D
        },
        {
            "Указан несуществующий 'productType'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            ERR,
            getValidEndDate(),
            0.01D
        },
        {
            "Указан 'endDate' менее чем +1 день",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getInvalidEndDate(),
            0.01D
        },
        {
            "Значение поля 'trafficRate' < min",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            0.00D
        },
        {
            "Значение поля 'trafficRate' > max",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getValidEndDate(),
            1.01D
        }
    };
  }
}
