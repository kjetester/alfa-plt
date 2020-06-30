package ru.alfabank.platform.experiment.update.positive;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.abtests.Experiment;
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
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    experiment = EXPERIMENT_STEPS.createExperiment(
        mobile,
        page_id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .05,
        getContentManager());
  }

  @Test(
      description = "Позитивный тест изменения неактивного эксперимента",
      dataProvider = "Positive data provider")
  public void inactiveExperimentUpdatePositiveTest(
      @ParameterKey("Test Case") final String testCase,
      @ParameterKey("New Value") final Object newValue) {
    LOGGER.info("Test case: " + testCase);
    var field2bChanged = StringUtils.substringBetween(testCase, "'");
    Experiment expected;
    Experiment changeSetBody;
    switch (field2bChanged) {
      case "cookieValue" -> {
        expected = new Experiment.Builder()
            .using(experiment)
            .setCookieValue((String) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setCookieValue((String) newValue)
            .build();
      }
      case "description" -> {
        expected = new Experiment.Builder()
            .using(experiment)
            .setDescription((String) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setDescription((String) newValue)
            .build();
      }
      case "productTypeKey" -> {
        expected = new Experiment.Builder()
            .using(experiment)
            .setProductTypeKey((ProductType) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setProductTypeKey((ProductType) newValue)
            .build();
      }
      case "trafficRate" -> {
        expected = new Experiment.Builder()
            .using(experiment)
            .setTrafficRate((Double) newValue)
            .build();
        changeSetBody = new Experiment.Builder()
            .setTrafficRate((Double) newValue)
            .build();
      }
      case "endDate" -> {
        expected =
            new Experiment.Builder()
                .using(experiment)
                .setEndDate((String) newValue)
                .build();
        changeSetBody = new Experiment.Builder()
            .setEndDate((String) newValue)
            .build();
      }
      default -> throw new IllegalArgumentException();
    }
    EXPERIMENT_STEPS
        .modifyExperiment(experiment, changeSetBody, getContentManager()).equals(expected);
  }

  /**
   * Positive data provider.
   *
   * @return test data
   */
  @DataProvider(name = "Positive data provider")
  public Object[][] positiveDataProvider() {
    return new Object[][]{
        {
            "Минимальная длина значения 'cookieValue'",
            randomAlphanumeric(1)},
        {
            "Максимальная длина значения 'cookieValue'",
            randomAlphanumeric(255)},
        {
            "Минимальная длина значения 'description'",
            randomAlphanumeric(1)},
        {
            "Максимальная длина значения 'description'",
            randomAlphanumeric(1000)},
        {
            "Изменение 'productTypeKey'",
            getRandomProductType()
        },
        {
            "Минимальное значение 'trafficRate'",
            0.01D
        },
        {
            "Максимальное значение 'trafficRate'",
            1.00D
        },
        {
            "Изменение 'endDate'",
            getValidExperimentEndDatePlusWeek()
        }
    };
  }
}
