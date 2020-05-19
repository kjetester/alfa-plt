package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class NoVariantsTest extends BaseTest {

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tНет вариантов")
  public void noVariantsExperimentUpdateNegativeTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDate();
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        EXPERIMENT_STEPS.createExperiment(
            device,
            page_id,
            getRandomProductType(),
            end,
            trafficRate,
            getContentManager());
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(false)
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .containsIgnoringCase("У эксперимента '"
            + actualExperiment.getUuid() + "' должно быть минимум 2 вариаций");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }
}
