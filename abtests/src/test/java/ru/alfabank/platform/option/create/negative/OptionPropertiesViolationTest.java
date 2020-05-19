package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import java.util.UUID;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionPropertiesViolationTest extends OptionBaseTest {

  @Test(description = "Тест проверки обработки некорректных значений полей варианта",
      dataProvider = "valuesProvider")
  public void optionsTrafficRateSumLimitViolationNegativeTest(
      final int testCase,
      @ParameterKey("isDefault") final Boolean isDefault,
      @ParameterKey("widgetUids") final List<String> widgetUids,
      @ParameterKey("experimentUuid") final String experimentUuid,
      @ParameterKey("trafficRate") final Double trafficRate) {
    final var result = OPTION_STEPS.createOptionAssumingFail(
        isDefault,
        widgetUids,
        experimentUuid,
        trafficRate,
        getContentManager());
    switch (testCase) {
      case 1: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        assertThat(result.asString())
            .as("Проверка сообщения об ошибке")
            .contains("В content-store не найдено виджетов с uid '["
                + widgetUids.get(0) + "]'");
        break;
      }
      case 2: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        assertThat(result.asString())
            .as("Проверка сообщения об ошибке")
            .contains("Эксперимент по uuid '" + experimentUuid + "' не найден");
        break;
      }
      case 3: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        assertThat(result.asString())
            .as("Проверка сообщения об ошибке")
            .contains("trafficRate", "Доля траффика должна быть меньше единицы");
        break;
      }
      case 4: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        assertThat(result.asString())
            .as("Проверка сообщения об ошибке")
            .contains("trafficRate", "Доля траффика должна быть больше нуля");
        break;
      }
      case 5: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        assertThat(result.asString())
            .as("Проверка сообщения об ошибке")
            .contains("trafficRate", "Должна быть указана доля трафика");
        break;
      }
      default: {
        throw new IllegalArgumentException("Такой тест-кейс не добавлен");
      }
    }
  }

  /**
   * Data provider.
   *
   * @return test data
   */
  @DataProvider
  public Object[][] valuesProvider() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop, page_id, getRandomProductType(), getValidEndDate(), .5D, getContentManager());
    return new Object[][]{
        {1, true, List.of("absentWidgetUUID"), experiment.getUuid(), .1D},
        {2, true, List.of(), UUID.randomUUID().toString(), .1D},
        {3, true, List.of(), experiment.getUuid(), 1D},
        {4, true, List.of(), experiment.getUuid(), 0D},
        {5, true, List.of(), experiment.getUuid(), null}
    };
  }
}
