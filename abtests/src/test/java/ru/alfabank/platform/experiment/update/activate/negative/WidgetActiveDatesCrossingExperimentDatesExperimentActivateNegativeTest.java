package ru.alfabank.platform.experiment.update.activate.negative;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class WidgetActiveDatesCrossingExperimentDatesExperimentActivateNegativeTest
    extends BaseTest {

  @Test(description = """
      Тест активации эксперимента с негативным условием:
      \t1. Дата начала отображения виджета больше даты начала эксперимента
      \t2. Дата окончания отображения виджета меньше даты окончания эксперимента""")
  public void widgetActiveDatesCrossingExperimentActiveDatesExperimentActivateNegativeTest() {
    final var date_from = getCurrentDateTime().plus(6, HOURS).toString();
    final var date_to = getCurrentDateTime().plus(12, HOURS).toString();
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
    final var experiment = EXPERIMENT_STEPS.createExperiment(
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
    DRAFT_STEPS.changeWidgetActiveDates(
        default_widget,
        page_id,
        date_from,
        date_to,
        getContentManager());
    DRAFT_STEPS.changeWidgetActiveDates(
        abTest_widget,
        page_id,
        date_from,
        date_to,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        experiment,
        getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains(
            default_widget.getUid(),
            abTest_widget.getUid(),
            "должны быть активны в момент начала и завершения эксперимента");
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата начала отображения виджета больше даты начала эксперимента")
  public void widgetStartDateCrossingExperimentActiveDatesExperimentActivateNegativeTest() {
    final var date_from = getCurrentDateTime().plus(6, HOURS).toString();
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
    final var experiment = EXPERIMENT_STEPS.createExperiment(
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
    DRAFT_STEPS.changeWidgetActiveDates(
        default_widget,
        page_id,
        date_from,
        null,
        getContentManager());
    DRAFT_STEPS.changeWidgetActiveDates(
        abTest_widget,
        page_id,
        date_from,
        null,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        experiment,
        getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains(
            default_widget.getUid(),
            abTest_widget.getUid(),
            "должны быть активны в момент начала и завершения эксперимента");
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата окончания отображения виджета меньше даты окончания эксперимента")
  public void widgetEndDateCrossingExperimentActiveDatesExperimentActivateNegativeTest() {
    final var date_from = getCurrentDateTime().plus(12, HOURS).toString();
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
    final var experiment = EXPERIMENT_STEPS.createExperiment(
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
    DRAFT_STEPS.changeWidgetActiveDates(
        default_widget,
        page_id,
        null,
        date_from,
        getContentManager());
    DRAFT_STEPS.changeWidgetActiveDates(
        abTest_widget,
        page_id,
        null,
        date_from,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        experiment,
        getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains(
            "Виджеты", default_widget.getUid(), abTest_widget.getUid(),
            "должны быть активны в момент начала и завершения эксперимента");
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }
}
