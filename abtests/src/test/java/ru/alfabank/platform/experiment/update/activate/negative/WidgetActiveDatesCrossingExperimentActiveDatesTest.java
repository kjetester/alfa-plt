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

import java.time.temporal.ChronoUnit;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class WidgetActiveDatesCrossingExperimentActiveDatesTest extends BaseTest {

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата начала отображения виджета больше даты начала эксперимента"
      + "\n\t2. Дата окончания отображения виджета меньше даты окончания эксперимента")
  public void widgetActiveDatesCrossingExperimentActiveDatesExperimentUpdateNegativeTest() {
    final var start = getCurrentDateTime().plus(6, ChronoUnit.HOURS).toString();
    final var end = getCurrentDateTime().plus(12, ChronoUnit.HOURS).toString();
    final var experiment_end = getValidEndDate();
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
    final var actual_experiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        experiment_end,
        traffic_rate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        actual_experiment.getUuid(),
        traffic_rate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        actual_experiment.getUuid(),
        traffic_rate,
        getContentManager());
    DRAFT_STEPS.changeWidgetActiveDates(
        default_widget,
        page_id,
        start,
        end,
        getContentManager());
    DRAFT_STEPS.changeWidgetActiveDates(
        abTest_widget,
        page_id,
        start,
        end,
        getContentManager());
    final var experimentStart = getValidEndDatePlus10Seconds();
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
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actual_experiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            default_widget.getUid(),
            abTest_widget.getUid(),
            "должны быть активны в момент начала и завершения эксперимента");
    EXPERIMENT_STEPS.getExistingExperiment(actual_experiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата начала отображения виджета больше даты начала эксперимента")
  public void widgetStartDateCrossingExperimentActiveDatesExperimentUpdateNegativeTest() {
    final var widget_start_date = getCurrentDateTime().plus(6, ChronoUnit.HOURS).toString();
    final var experiment_end_date = getValidEndDate();
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
    final var trafficRate = .5D;
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
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
    DRAFT_STEPS.changeWidgetActiveDates(
        default_widget,
        page_id,
        widget_start_date,
        null,
        getContentManager());
    DRAFT_STEPS.changeWidgetActiveDates(
        abTest_widget,
        page_id,
        widget_start_date,
        null,
        getContentManager());
    final var experimentStart = getValidEndDatePlus10Seconds();
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
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("должны", "быть выключенными");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата окончания отображения виджета меньше даты окончания эксперимента")
  public void widgetEndDateCrossingExperimentActiveDatesExperimentUpdateNegativeTest() {
    final var widgetStart = getCurrentDateTime().plus(12, ChronoUnit.HOURS).toString();
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
    final var trafficRate = .5D;
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        getValidEndDate(),
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
    DRAFT_STEPS.changeWidgetActiveDates(
        default_widget,
        page_id,
        null,
        widgetStart,
        getContentManager());
    DRAFT_STEPS.changeWidgetActiveDates(
        abTest_widget,
        page_id,
        null,
        widgetStart,
        getContentManager());
    final var experimentStart = getValidEndDatePlus10Seconds();
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
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("должны", "быть выключенными");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }
}
