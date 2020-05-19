package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class RunningExperimentExistsTest extends BaseTest {

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tЕсть запущенный эксперимент")
  public void runningExperimentExistsExperimentUpdateNegativeTest() {
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
    final var widget_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget_3 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget_4 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    var device = widget_1.getDevice();
    final var trafficRate = .5D;
    final var experiment_to_be_runed = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        end,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_1.getUid()),
        experiment_to_be_runed.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget_2.getUid()),
        experiment_to_be_runed.getUuid(),
        trafficRate,
        getContentManager());
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        experiment_to_be_runed,
        getContentManager());
    final var actual_experiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        end,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_3.getUid()),
        actual_experiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget_4.getUid()),
        actual_experiment.getUuid(),
        trafficRate,
        getContentManager());
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actual_experiment.getUuid())
        .setCookieValue(actual_experiment.getCookieValue())
        .setDescription(actual_experiment.getDescription())
        .setPageId(actual_experiment.getPageId())
        .setProductTypeKey(actual_experiment.getProductTypeKey())
        .setEndDate(actual_experiment.getEndDate())
        .setTrafficRate(actual_experiment.getTrafficRate())
        .setDevice(actual_experiment.getDevice())
        .setEnabled(false)
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actual_experiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .containsIgnoringCase("Для страницы '" + page_id + "' и типа устройства '"
            + expectedExperiment.getDevice() + "' уже существует активный эксперимент");
    EXPERIMENT_STEPS.getExistingExperiment(actual_experiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }
}
