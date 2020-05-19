package ru.alfabank.platform.experiment.delete.positive;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.apache.log4j.LogManager;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.experiment.involvements.negative.InvolvementsTest;

public class ExperimentDeletionTest extends BaseTest {

  @Test(description = "Позитивный тест удаления эксперимента со статусом 'DISABLED'")
  public void disabledExperimentDeletionNegativeTest() {
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
    // TEST //
    assertThat(EXPERIMENT_STEPS.deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NO_CONTENT);
    final var softly = new SoftAssertions();
    softly.assertThat(
        EXPERIMENT_STEPS.getAbsentExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(defaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(nonDefaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertAll();
  }

  @Test(description = "Позитивный тест удаления эксперимента со статусом 'CANCELLED'")
  public void cancelledExperimentDeletionNegativeTest() {
    final var experiment_and_date = getValidEndDatePlusWeek();
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
        experiment_and_date,
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
    EXPERIMENT_STEPS.stopExperimentAssumingSuccess(
        experiment,
        getContentManager());
    // TEST //
    assertThat(
        EXPERIMENT_STEPS.deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NO_CONTENT);
    final var softly = new SoftAssertions();
    softly.assertThat(
        EXPERIMENT_STEPS.getAbsentExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(defaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(nonDefaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertAll();
  }

  @Test(description = "Позитивный тест удаления эксперимента со статусом 'EXPIRED'")
  public void expiredExperimentDeletionNegativeTest() {
    LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
