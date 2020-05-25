package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class UnassignedOptionLimitViolationTest extends OptionBaseTest {

  @Test(description = "Тест создания более одного непривязанного к виджету варианта",
      dataProvider = "dataProvider")
  public void oneDefaultOptionLimitNegativeTest(
      @ParameterKey("Дефолтный вариант") final Boolean isDefaultOption) {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    final Response result;
    if (isDefaultOption) {
      OPTION_STEPS.createOption(false, List.of(), experiment.getUuid(), .5D, getContentManager());
      result = OPTION_STEPS.createOptionAssumingFail(
          true,
          List.of(),
          experiment.getUuid(),
          .5D,
          getContentManager());
    } else {
      OPTION_STEPS.createOption(true, List.of(), experiment.getUuid(), .5D, getContentManager());
      result = OPTION_STEPS.createOptionAssumingFail(
          false,
          List.of(),
          experiment.getUuid(),
          .5D,
          getContentManager());
    }
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Только один вариант эксперимента '" + experiment.getUuid()
            + "' может иметь пустой список связанных виджетов");
  }
}
