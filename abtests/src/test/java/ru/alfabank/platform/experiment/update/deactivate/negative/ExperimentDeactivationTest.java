package ru.alfabank.platform.experiment.update.deactivate.negative;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Status.CANCELLED;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.abtests.Experiment;

public class ExperimentDeactivationTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(ExperimentDeactivationTest.class);

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'DISABLED'")
  public void disabledExperimentUpdateNegativeTest() {
    final var experiment_end_date = getValidExperimentEndDatePlusWeek();
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
    final var trafficRate = .5D;
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        default_widget.getDevice(),
        page_id,
        null,
        experiment_end_date,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    // TEST //
    final var result = EXPERIMENT_STEPS.stopExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно деактивировать эксперимент '" + actualExperiment.getUuid()
            + "' со статусом 'DISABLED'");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .equals(new Experiment.Builder()
            .setUuid(actualExperiment.getUuid())
            .setCookieValue(actualExperiment.getCookieValue())
            .setDescription(actualExperiment.getDescription())
            .setPageId(actualExperiment.getPageId())
            .setProductTypeKey(actualExperiment.getProductTypeKey())
            .setEndDate(actualExperiment.getEndDate())
            .setTrafficRate(actualExperiment.getTrafficRate())
            .setDevice(actualExperiment.getDevice())
            .setCreationDate(actualExperiment.getCreationDate())
            .setCreatedBy(actualExperiment.getCreatedBy())
            .setEnabled(false)
            .setStatus(DISABLED)
            .build());
  }

  @Test(description = "Тест деактивации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'CANCELED'")
  public void cancelledExperimentUpdateNegativeTest() {
    final var experimentEndDate = getValidExperimentEndDatePlusWeek();
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
    final var trafficRate = .5D;
    var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        default_widget.getDevice(),
        page_id,
        null,
        experimentEndDate,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    actualExperiment = EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        actualExperiment,
        getContentManager());
    actualExperiment = EXPERIMENT_STEPS.stopExperimentAssumingSuccess(
        actualExperiment,
        getContentManager());
    // TEST //
    final var result = EXPERIMENT_STEPS.stopExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно деактивировать эксперимент '" + actualExperiment.getUuid()
            + "' со статусом 'CANCELLED'");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .equals(new Experiment.Builder()
            .setUuid(actualExperiment.getUuid())
            .setCookieValue(actualExperiment.getCookieValue())
            .setDescription(actualExperiment.getDescription())
            .setPageId(actualExperiment.getPageId())
            .setProductTypeKey(actualExperiment.getProductTypeKey())
            .setEndDate(actualExperiment.getEndDate())
            .setTrafficRate(actualExperiment.getTrafficRate())
            .setDevice(actualExperiment.getDevice())
            .setCreationDate(actualExperiment.getCreationDate())
            .setCreatedBy(actualExperiment.getCreatedBy())
            .setActivationDate(actualExperiment.getActivationDate())
            .setActivatedBy(actualExperiment.getActivatedBy())
            .setDeactivationDate(getCurrentDateTime().toString())
            .setDeactivatedBy(getContentManager().getLogin())
            .setEnabled(false)
            .setStatus(CANCELLED)
            .build());
  }

  @Test(description = "Тест деактивации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'EXPIRED'")
  public void expiredExperimentUpdateNegativeTest() {
    LOGGER.warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
