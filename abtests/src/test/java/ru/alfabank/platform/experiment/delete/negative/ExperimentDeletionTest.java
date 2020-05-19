package ru.alfabank.platform.experiment.delete.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class ExperimentDeletionTest extends BaseTest {

  @Test(description = "Негативный тест удаления эксперимента со статусом 'RUNNING'")
  public void runningExperimentDeletionNegativeTest() {
    final var experiment_end_date = getValidEndDatePlusWeek();
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var experiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        experiment_end_date,
        trafficRate,
        getContentManager());
    final var defaultOption = OPTION_STEPS.createOption(
        true,
        List.of(widget1.getUid()),
        experiment.getUuid(),
        trafficRate,
        getContentManager());
    final var nonDefaultOption = OPTION_STEPS.createOption(
        false,
        List.of(widget2.getUid()),
        experiment.getUuid(),
        trafficRate,
        getContentManager());
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        experiment,
        getContentManager());
    // TEST //
    assertThat(EXPERIMENT_STEPS.deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    final var softly = new SoftAssertions();
    softly.assertThat(
        EXPERIMENT_STEPS.getAbsentExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_OK);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(defaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_OK);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(nonDefaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_OK);
    softly.assertAll();
  }
}
