package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.apache.log4j.LogManager;
import org.testng.SkipException;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.experiment.involvements.negative.InvolvementsTest;

public class WrongExperimentStatusTest extends BaseTest {

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'RUNNING'")
  public void runningExperimentUpdateNegativeTest() {
    final var experimentEndDate = getValidEndDate();
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
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
        null,
        null,
        getContentManager());
    final var device = default_widget.getDevice();
    final var traffic_rate = .5D;
    var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        experimentEndDate,
        traffic_rate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        actualExperiment.getUuid(),
        traffic_rate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        actualExperiment.getUuid(),
        traffic_rate,
        getContentManager());
    // TEST //
    actualExperiment = EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        actualExperiment,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно изменить активный эксперимент " + actualExperiment.getUuid());
    EXPERIMENT_STEPS.getExistingExperiment(
        actualExperiment,
        getContentManager())
        .checkActivatedExperiment(
            new Experiment.Builder()
                .setUuid(actualExperiment.getUuid())
                .setCookieValue(actualExperiment.getCookieValue())
                .setDescription(actualExperiment.getDescription())
                .setPageId(actualExperiment.getPageId())
                .setProductTypeKey(actualExperiment.getProductTypeKey())
                .setEndDate(actualExperiment.getEndDate())
                .setTrafficRate(actualExperiment.getTrafficRate())
                .setDevice(actualExperiment.getDevice())
                .setEnabled(actualExperiment.getEnabled())
                .setCreatedBy(actualExperiment.getCreatedBy())
                .setActivationDate(getCurrentDateTime().toString())
                .setActivatedBy(getContentManager().getLogin())
                .setStatus(actualExperiment.getStatus())
                .setCreationDate(actualExperiment.getCreationDate())
                .build());
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'CANCELED'")
  public void canceledExperimentUpdateNegativeTest() {
    final var experimentEndDate = getValidEndDate();
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
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
        null,
        null,
        getContentManager());
    final var device = default_widget.getDevice();
    final var traffic_rate = .5D;
    // TEST //
    var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        experimentEndDate,
        traffic_rate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        actualExperiment.getUuid(),
        traffic_rate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        actualExperiment.getUuid(),
        traffic_rate,
        getContentManager());
    actualExperiment = EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        actualExperiment,
        getContentManager());
    actualExperiment = EXPERIMENT_STEPS.stopExperimentAssumingSuccess(
        actualExperiment,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно активировать эксперимент '" + actualExperiment.getUuid()
            + "' со статусом 'CANCELLED'");
    EXPERIMENT_STEPS.getExistingExperiment(
        actualExperiment,
        getContentManager())
        .checkActivatedExperiment(
            new Experiment.Builder()
                .setUuid(actualExperiment.getUuid())
                .setCookieValue(actualExperiment.getCookieValue())
                .setDescription(actualExperiment.getDescription())
                .setPageId(actualExperiment.getPageId())
                .setProductTypeKey(actualExperiment.getProductTypeKey())
                .setEndDate(actualExperiment.getEndDate())
                .setTrafficRate(actualExperiment.getTrafficRate())
                .setDevice(actualExperiment.getDevice())
                .setEnabled(actualExperiment.getEnabled())
                .setCreationDate(actualExperiment.getCreationDate())
                .setCreatedBy(actualExperiment.getCreatedBy())
                .setActivationDate(actualExperiment.getActivationDate())
                .setActivatedBy(actualExperiment.getActivatedBy())
                .setDeactivationDate(getCurrentDateTime().toString())
                .setDeactivatedBy(getContentManager().getLogin())
                .setStatus(actualExperiment.getStatus())
                .build());
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'EXPIRED'")
  public void expiredExperimentUpdateNegativeTest() {
    LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
