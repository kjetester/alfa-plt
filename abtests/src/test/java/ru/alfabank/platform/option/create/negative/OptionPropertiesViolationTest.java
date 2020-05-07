package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.ProductType.getRandomProductType;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import java.util.UUID;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionPropertiesViolationTest extends OptionBaseTest {

  /**
   * Data provider.
   * @return test data
   */
  @DataProvider
  public Object[][] valuesProvider() {
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var experiment = createExperiment(
        desktop, pageId, getRandomProductType(), experimentEnd, .5D);
    return new Object[][]{
        {1, true, List.of("absentWidgetUUID"), experiment.getUuid(), .1D},
        {2, true, List.of(), UUID.randomUUID().toString(), .1D},
        {3, true, List.of(), experiment.getUuid(), 1D},
        {4, true, List.of(), experiment.getUuid(), 0D},
        {5, true, List.of(), experiment.getUuid(), null}
    };
  }

  @Test (description = "Тест проверки обработки некорректных значений полей варианта",
      dataProvider = "valuesProvider")
  public void optionsTrafficRateSumLimitViolationTest(
      final int testCase,
      @ParameterKey("isDefault") final Boolean isDefault,
      @ParameterKey("widgetUids") final List<String> widgetUids,
      @ParameterKey("experimentUuid") final String experimentUuid,
      @ParameterKey("trafficRate") final Double trafficRate) {
    final var result = createOptionAssumingFail(isDefault, widgetUids, experimentUuid, trafficRate);
    switch (testCase) {
      case 1: {
        assertThat(result.getStatusCode())
                .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
        assertThat(result.asString())
                .as("Проверка сообщения об ошибке")
                .contains("В content-store не найдено виджетов с uid '["
                        + widgetUids.get(0) + "]'");
        break;
      }
      case 2: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
        assertThat(result.asString())
                .as("Проверка сообщения об ошибке")
                .contains("Эксперимент по uuid '" + experimentUuid + "' не найден");
        break;
      }
      case 3: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
        assertThat(result.asString())
                .as("Проверка сообщения об ошибке")
                .contains("trafficRate", "Доля траффика должна быть меньше единицы");
        break;
      }
      case 4: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
        assertThat(result.asString())
                .as("Проверка сообщения об ошибке")
                .contains("trafficRate", "Доля траффика должна быть больше нуля");
        break;
      }
      case 5: {
        assertThat(result.getStatusCode())
            .as("Проверка статус-кода")
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
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
}
