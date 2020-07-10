package ru.alfabank.platform.experiment.update.negative;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ProductType.CREDIT_CARD_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.UNLISTED_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;
import static ru.alfabank.platform.businessobjects.enums.Team.UNCLAIMED_TEAM;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.abtests.Experiment;
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
    final var dateFrom = getValidWidgetDateFrom();
    final var dateTo = getValidExperimentEndDate();
    final var page_id = PAGES_STEPS.createPage(dateFrom, dateTo, true, List.of(UNCLAIMED_TEAM), getContentManager());
    experiment = EXPERIMENT_STEPS.createExperiment(
        mobile,
        page_id,
        UNLISTED_PRODUCT_TYPE,
        dateTo,
        .05,
        getContentManager());
  }

  @Test(
      description = "Негативный тест изменения неактивного эксперимента",
      dataProvider = "inactiveWidgetNegativeDataProvider")
  public void inactiveExperimentUpdateNegativeTest(
      @ParameterKey("Test Case") final String testCase,
      @ParameterKey("New Value") final Object newValue) {
    LOGGER.info("Test case: " + testCase);
    var field2bChanged = StringUtils.substringBetween(testCase, "'");
    Experiment changeSetBody;
    switch (field2bChanged) {
      case "body" -> changeSetBody = new Experiment.Builder().build();
      case "cookieValue" -> changeSetBody = new Experiment.Builder()
          .setCookieValue((String) newValue).build();
      case "description" -> changeSetBody = new Experiment.Builder()
          .setDescription((String) newValue).build();
      case "productTypeKey" -> changeSetBody = new Experiment.Builder()
          .setProductTypeKey((ProductType) newValue).build();
      case "endDate" -> changeSetBody = new Experiment.Builder()
          .setEndDate((String) newValue).build();
      case "trafficRate" -> changeSetBody = new Experiment.Builder()
          .setTrafficRate((Double) newValue).build();
      case "enabled/cookieValue" -> changeSetBody = new Experiment.Builder()
          .setEnabled((Boolean) newValue).setCookieValue(randomAlphanumeric(100)).build();
      case "enabled/description" -> changeSetBody = new Experiment.Builder()
          .setEnabled((Boolean) newValue).setDescription(randomAlphanumeric(100)).build();
      case "enabled/productTypeKey" -> changeSetBody = new Experiment.Builder()
          .setEnabled((Boolean) newValue).setProductTypeKey(CREDIT_CARD_PRODUCT_TYPE).build();
      case "enabled/endDate" -> changeSetBody = new Experiment.Builder()
          .setEnabled((Boolean) newValue).setEndDate(getValidExperimentEndDate()).build();
      case "enabled/trafficRate" -> changeSetBody = new Experiment.Builder()
          .setEnabled((Boolean) newValue).setTrafficRate(0.23D).build();
      case "enabled" -> changeSetBody = new Experiment.Builder().setEnabled((Boolean) newValue)
          .setCookieValue(randomAlphanumeric(100)).setDescription(randomAlphanumeric(100))
          .setProductTypeKey(CREDIT_CARD_PRODUCT_TYPE).setEndDate(getValidExperimentEndDate()).setTrafficRate(0.23D)
          .build();
      case "pageId" -> changeSetBody = new Experiment.Builder().setPageId((Integer) newValue)
          .build();
      case "uuid" -> changeSetBody = new Experiment.Builder().setUuid((String) newValue).build();
      case "device" -> changeSetBody = new Experiment.Builder().setDevice((Device) newValue)
          .build();
      case "createdBy" -> changeSetBody = new Experiment.Builder().setCreatedBy((String) newValue)
          .build();
      case "activatedBy" -> changeSetBody = new Experiment.Builder()
          .setActivatedBy((String) newValue).build();
      case "creationDate" -> changeSetBody = new Experiment.Builder()
          .setCreationDate((String) newValue).build();
      case "activationDate" -> changeSetBody = new Experiment.Builder()
          .setActivationDate((String) newValue).build();
      case "deactivatedBy" -> changeSetBody = new Experiment.Builder()
          .setActivatedBy((String) newValue).build();
      case "deactivationDate" -> changeSetBody = new Experiment.Builder()
          .setDeactivationDate((String) newValue).build();
      case "status" -> changeSetBody = new Experiment.Builder().setStatus((Status) newValue)
          .build();
      default -> throw new IllegalArgumentException();
    }
    final var response = EXPERIMENT_STEPS.modifyExperimentAssumingFail(
        experiment,
        changeSetBody,
        getContentManager());
    // CHECKS //
    final var experiment = EXPERIMENT_STEPS.getExistingExperiment(
        this.experiment,
        getContentManager());
    final var fieldViolations = response.getBody().jsonPath().getList("fieldViolations");
    final var globalErrors = response.getBody().jsonPath().getList("globalErrors");
    switch (field2bChanged) {
      case "cookieValue" -> {
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
      }
      case "description" -> {
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
      }
      case "productTypeKey" -> {
        response.then().statusCode(SC_BAD_REQUEST);
        assertThat(response.getBody().jsonPath().getString("message"))
            .as("Проверка обрабоки некоррекнтого 'productTypeKey'")
            .contains("Тип продукта '" + UNLISTED_PRODUCT_TYPE + "' не существует");
      }
      case "endDate" -> {
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
      }
      case "trafficRate" -> {
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
      }
      default -> {
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
      }
    }
    experiment.equals(this.experiment);
  }

  /**
   * Negative data provider.
   *
   * @return test data
   */
  @DataProvider(name = "inactiveWidgetNegativeDataProvider")
  public Object[][] inactiveWidgetNegativeDataProvider() {
    return new Object[][]{
        {
            "Пустое 'body'",
            null
        },
        {
            "Длина значения поля 'cookieValue' < min",
            ""
        },
        {
            "Длина значения поля 'cookieValue' > max",
            randomAlphanumeric(256)
        },
        {
            "Длина значения поля 'description' < min",
            ""
        },
        {
            "Длина значения поля 'description' > max",
            randomAlphanumeric(1001)
        },
        {
            "Невалидное значение поля 'productTypeKey'",
            UNLISTED_PRODUCT_TYPE
        },
        {
            "Указан 'endDate' менее чем +1 день",
            getInvalidExperimentEndDate()
        },
        {
            "значения поля 'trafficRate' < min",
            0D
        },
        {
            "Длина значения поля 'trafficRate' > max",
            1.01D
        },
        {
            "Одновременное изменение параметров и признака 'enabled'",
            true
        },
        {
            "Одновременное изменение параметра и признака 'enabled/cookieValue'",
            true
        },
        {
            "Одновременное изменение параметра и признака 'enabled/description'",
            true
        },
        {
            "Одновременное изменение параметра и признака 'enabled/productTypeKey'",
            true
        },
        {
            "Одновременное изменение параметра и признака 'enabled/endDate'",
            true
        },
        {
            "Одновременное изменение параметра и признака 'enabled/trafficRate'",
            true
        },
        {
            "Одновременное изменение всех параметров и признака 'enabled'",
            true
        },
        {
            "Изменение поля 'uuid'",
            UUID.randomUUID().toString()
        },
        {
            "Изменение поля 'pageId'",
            11
        },
        {
            "Изменение поля 'device'",
            mobile
        },
        {
            "Изменение поля 'createdBy'",
            "random user"
        },
        {
            "Изменение поля 'activatedBy'",
            "random user"
        },
        {
            "Изменение поля 'creationDate'",
            getValidExperimentEndDate()
        },
        {
            "Изменение поля 'activationDate'",
            getValidExperimentEndDate()
        },
        {
            "Изменение поля 'deactivatedBy'",
            "random user"
        },
        {
            "Изменение поля 'deactivationDate'",
            getValidExperimentEndDate()
        },
        {
            "Изменение поля 'status'",
            RUNNING
        },
    };
  }
}
