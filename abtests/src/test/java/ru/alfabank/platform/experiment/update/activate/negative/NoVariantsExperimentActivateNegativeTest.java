package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class NoVariantsExperimentActivateNegativeTest extends BaseTest {

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tНет вариантов")
  public void noVariantsExperimentActivateNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(experiment, getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .containsIgnoringCase("У эксперимента '"
            + experiment.getUuid() + "' должно быть минимум 2 вариаций");
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }
}
