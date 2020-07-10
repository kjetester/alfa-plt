package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class WrongExperimentStatusExperimentActivateNegativeTest extends BaseTest {

  private static final Logger LOGGER = LogManager
      .getLogger(WrongExperimentStatusExperimentActivateNegativeTest.class);

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'RUNNING'")
  public void runningExperimentActivateNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget = DRAFT_STEPS.createWidget(
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
    final var abTest_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    // TEST //
    experiment = EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        experiment,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        experiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно изменить активный эксперимент " + experiment.getUuid());
    EXPERIMENT_STEPS.getExistingExperiment(
        experiment, getContentManager())
        .equals(experiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'CANCELED'")
  public void canceledExperimentActivateNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget = DRAFT_STEPS.createWidget(
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
    final var abTest_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    // TEST //
    var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    experiment = EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        experiment,
        getContentManager());
    experiment = EXPERIMENT_STEPS.stopExperimentAssumingSuccess(
        experiment,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        experiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно активировать эксперимент '" + experiment.getUuid()
            + "' со статусом 'CANCELLED'");
    EXPERIMENT_STEPS.getExistingExperiment(
        experiment,
        getContentManager())
        .equals(experiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'EXPIRED'")
  public void expiredExperimentActivateNegativeTest() {
    LOGGER.warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
