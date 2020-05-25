package ru.alfabank.platform.experiment.create;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
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
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.TestNGException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Experiment.Builder;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.ProductType;

public class CreateExperimentTest extends BaseTest {

  @Test(
      description = "Позитивный тест создания нового эксперимента",
      dataProvider = "Positive data provider")
  public void experimentCreationPositiveTest(
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
    final var experiment_1_body = new Experiment.Builder()
        .setDescription(experiment1description)
        .setCookieValue(experiment1cookieValue)
        .setPageId(experiment1pageId)
        .setDevice(experiment1device)
        .setProductTypeKey(experiment1productType)
        .setEndDate(experiment1endDate)
        .setTrafficRate(experiment1trafficRate)
        .build();
    final var created_experiment_1 = EXPERIMENT_STEPS.createExperiment(
        experiment_1_body,
        getContentManager());
    created_experiment_1.equals(
        new Experiment.Builder()
            .using(experiment_1_body)
            .setUuid(created_experiment_1.getUuid())
            .setEnabled(false)
            .setCreatedBy(getContentManager().getLogin())
            .setStatus(DISABLED)
            .setCreationDate(getCurrentDateTime().toString())
            .build());
    final var experiment_2_body = new Experiment.Builder()
        .setDescription(experiment2description)
        .setCookieValue(experiment2cookieValue)
        .setPageId(experiment2pageId)
        .setDevice(experiment2device)
        .setProductTypeKey(experiment2productType)
        .setEndDate(experiment2endDate)
        .setTrafficRate(experiment2trafficRate)
        .build();
    final var created_experiment_2 = EXPERIMENT_STEPS.createExperiment(
        experiment_2_body,
        getContentManager());
    created_experiment_2.equals(
        new Experiment.Builder()
            .using(experiment_2_body)
            .setUuid(created_experiment_2.getUuid())
            .setEnabled(false)
            .setCreatedBy(getContentManager().getLogin())
            .setStatus(DISABLED)
            .setCreationDate(getCurrentDateTime().toString())
            .build());
  }

  /**
   * Data provider.
   *
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
            getValidExperimentEndDate(),
            0.01D,
            mobile,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            mobile,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            PIL,
            getValidExperimentEndDate(),
            1.00D,
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            10,
            PIL,
            getValidExperimentEndDate(),
            1.00D
        },
        {
            desktop,
            randomAlphanumeric(255),
            randomAlphanumeric(1000),
            10,
            DC,
            getValidExperimentEndDate(),
            0.11D,
            mobile,
            randomAlphanumeric(255),
            randomAlphanumeric(1000),
            10,
            DC,
            getValidExperimentEndDate(),
            0.11D
        },
        {
            mobile,
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            10,
            MG,
            getValidExperimentEndDate(),
            0.11D,
            desktop,
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            10,
            MG,
            getValidExperimentEndDate(),
            0.11D
        },
        {
            desktop,
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            10,
            SME,
            getValidExperimentEndDate(),
            0.11D,
            mobile,
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            10,
            SME,
            getValidExperimentEndDate(),
            0.11D
        },
        {
            mobile,
            randomAlphanumeric(11),
            randomAlphanumeric(11),
            10,
            INV,
            getValidExperimentEndDate(),
            0.11D,
            desktop,
            randomAlphanumeric(11),
            randomAlphanumeric(11),
            10,
            INV,
            getValidExperimentEndDate(),
            0.11D
        },
        {
            desktop,
            randomAlphanumeric(10),
            randomAlphanumeric(10),
            10,
            COM,
            getValidExperimentEndDate(),
            0.11D,
            mobile,
            randomAlphanumeric(10),
            randomAlphanumeric(10),
            10,
            COM,
            getValidExperimentEndDate(),
            0.11D
        }
    };
  }

  @Test(
      description = "Негативный тест создания нового эксперимента",
      dataProvider = "Negative data provider")
  public void experimentCreationNegativeTest(
      @ParameterKey("testCase") final String testCase,
      @ParameterKey("device") final Device device,
      @ParameterKey("cookieValue") final String cookieValue,
      @ParameterKey("description") final String description,
      @ParameterKey("pageId") final Integer pageId,
      @ParameterKey("productType") final ProductType productType,
      @ParameterKey("endDate") final String endDate,
      @ParameterKey("trafficRate") final Double trafficRate) {
    final var experiment = new Builder()
        .setDescription(description)
        .setCookieValue(cookieValue)
        .setPageId(pageId)
        .setDevice(device)
        .setProductTypeKey(productType)
        .setEndDate(endDate)
        .setTrafficRate(trafficRate)
        .build();
    final var createResponse = EXPERIMENT_STEPS.createExperimentAssumingFail(
        experiment,
        getContentManager());
    final var softly = new SoftAssertions();
    softly.assertThat(createResponse.statusCode()).isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    final var fieldViolations = createResponse.getBody().jsonPath().getList("fieldViolations");
    final var globalErrors = createResponse.getBody().jsonPath().getList("globalErrors");
    switch (StringUtils.substringBetween(testCase, "'")) {
      case "device": {
        assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'")
            .isNotNull();
        assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'")
            .isEqualTo(1);
        if (device == null) {
          softly
              .assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки отсутсвия значения 'device'")
              .contains("device", "Необходимо указать тип устройства");
        } else {
          softly
              .assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки некоррекнтого значения 'device'")
              .contains("Поле может иметь только одно из возможных значений: [DESKTOP, MOBILE]. "
                  + "Чувствительность к регистру - false");
        }
        break;
      }
      case "cookieValue": {
        softly
            .assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        softly
            .assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'")
            .isNotNull();
        softly
            .assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'")
            .isEqualTo(1);
        if (cookieValue == null) {
          softly
              .assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки некоррекнтого значения 'cookieValue'")
              .contains("cookieValue", "Должно быть указана составная часть формировании кук");
        } else {
          softly
              .assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки некоррекнтого значения 'cookieValue'")
              .contains("cookieValue", cookieValue.length() > 255
                  ? "length must be between 0 and 255"
                  : "Должно быть указана составная часть формировании кук");
        }
        break;
      }
      case "description": {
        softly
            .assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        softly
            .assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'")
            .isNotNull();
        softly
            .assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'")
            .isEqualTo(1);
        if (description == null) {
          softly
              .assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки некоррекнтого значения 'description'")
              .contains("description", "Должно быть указано описание эксперимента");
        } else {
          softly
              .assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки некоррекнтого значения 'description'")
              .contains("description", description.length() > 1000
                  ? "length must be between 0 and 1000"
                  : "Должно быть указано описание эксперимента");
        }
        break;
      }
      case "pageId": {
        if (pageId == null) {
          softly
              .assertThat(globalErrors)
              .as("Проверка отсутствия ошибок в 'globalErrors'")
              .isNullOrEmpty();
          softly
              .assertThat(fieldViolations)
              .as("Проверка присутствия ошибок в 'fieldViolations'")
              .isNotNull();
          softly
              .assertThat(fieldViolations.size())
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
        break;
      }
      case "productType": {
        if (productType != null && productType.equals(ERR)) {
          softly
              .assertThat(createResponse.getBody().asString())
              .as("роверка обрабоки некоррекнтого значения 'productType'")
              .contains("Тип продукта '" + productType + "' не существует");
        } else {
          softly
              .assertThat(globalErrors)
              .as("Проверка отсутствия ошибок в 'globalErrors'")
              .isNullOrEmpty();
          softly
              .assertThat(fieldViolations)
              .as("Проверка присутствия ошибок в 'fieldViolations'")
              .isNotNull();
          softly
              .assertThat(fieldViolations.size())
              .as("Проверка количества ошибок в 'fieldViolations'")
              .isEqualTo(1);
          softly
              .assertThat(fieldViolations.get(0).toString())
              .as("Проверка обрабоки отсутсвия значения 'productType'")
              .contains("Необходимо указать ключ типа продукта");
        }
        break;
      }
      case "endDate": {
        softly
            .assertThat(globalErrors)
            .as("Проверка отсутствия ошибок в 'globalErrors'")
            .isNullOrEmpty();
        softly
            .assertThat(fieldViolations)
            .as("Проверка присутствия ошибок в 'fieldViolations'")
            .isNotNull();
        softly
            .assertThat(fieldViolations.size())
            .as("Проверка количества ошибок в 'fieldViolations'")
            .isEqualTo(1);
        softly
            .assertThat(createResponse.getBody().asString())
            .as("Проверка обрабоки некоррекнтого значения 'endDate'")
            .contains(endDate == null
                ? "Необходимо указать дату окончания эксперимента"
                : "Минимально допустимая продолжительность эксперимента составляет: 1 день");
        break;
      }
      case "trafficRate": {
        var stringAssert = softly.assertThat(createResponse.getBody().asString())
            .as("Проверка обрабоки некоррекнтого значения 'trafficRate'");
        stringAssert.contains("trafficRate");
        if (trafficRate == null) {
          stringAssert.contains("Должна быть указана доля от трафика участвующая в эксперименте");
        } else if (trafficRate < 0.01D) {
          stringAssert.contains("Доля трафика должна быть больше");
        } else if (trafficRate > 1D) {
          stringAssert.contains("Доля трафика не может превышать единицу");
          break;
        }
        break;
      }
      default: {
        throw new TestNGException("Неописанный кейс");
      }
    }
    if (experiment.getPageId() != null) {
      final var readResponse =
          EXPERIMENT_STEPS.getAbsentExperimentByPageId(experiment.getPageId(), getContentManager());
      softly.assertThat(readResponse.jsonPath().getInt("numberOfElements")).isEqualTo(0);
    }
    softly.assertAll();
  }


  /**
   * Data provider.
   *
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
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'cookieValue'",
            desktop,
            null,
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'description'",
            desktop,
            randomAlphanumeric(1),
            null,
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'pageId'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            null,
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'productType'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            null,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Отсутствует поле 'endDate'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            null,
            0.01D
        },
        {
            "Отсутствует поле 'trafficRate'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            null
        },
        {
            "Указан несуществующий 'device'",
            all,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Длина строки в поле 'cookieValue' < min",
            desktop,
            randomAlphanumeric(0),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Длина строки в поле 'cookieValue' > max",
            desktop,
            randomAlphanumeric(256),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Длина строки в поле 'description' < min",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(0),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Длина строки в поле 'description' > max",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(10001),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Указан несуществующий 'pageId'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            0,
            CC,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Указан несуществующий 'productType'",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            ERR,
            getValidExperimentEndDate(),
            0.01D
        },
        {
            "Указан 'endDate' менее чем +1 день",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getInvalidExperimentEndDate(),
            0.01D
        },
        {
            "Значение поля 'trafficRate' < min",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            0.00D
        },
        {
            "Значение поля 'trafficRate' > max",
            desktop,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            PAGES_STEPS.createEnabledPage(getContentManager()),
            CC,
            getValidExperimentEndDate(),
            1.01D
        }
    };
  }
}
