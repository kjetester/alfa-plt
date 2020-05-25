package ru.alfabank.platform.experiment.update.activate.negative;

import static java.util.Collections.emptyList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class VariantsAssignedToSharedWidgetsExperimentActivateNegativeTest extends BaseTest {

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tВарианты привязаны к шаренным виджетам")
  public void bothVariantsAssignedToSharedWidgetsExperimentActivateNegativeTest() {
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var widget_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var trafficRate = .5D;
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_1_id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_1.getUid()),
        experiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget_2.getUid()),
        experiment.getUuid(),
        trafficRate,
        getContentManager());
    final var page_2_id = PAGES_STEPS.createEnabledPage(getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(widget_1,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(widget_2,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(experiment, getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains(widget_2.getUid(), widget_1.getUid());
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tДефолтный вариант привязан к шаренному виджету")
  public void defaultVariantAssignedToSharedWidgetsExperimentActivateNegativeTest() {
    final var date_from = getValidWidgetDateFrom();
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
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
        CREATED_PAGES.get(page_1_id),
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
        page_1_id,
        getRandomProductType(),
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
        emptyList(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    final var page_2_id = PAGES_STEPS.createEnabledPage(getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        default_widget,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(experiment, getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains("Отвяжите следующие виджеты: [" + default_widget.getUid() + "]");
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tНедефолтный вариант привязан к шаренному виджету")
  public void nonDefaultVariantAssignedToSharedWidgetsExperimentActivateNegativeTest() {
    final var date_from = getValidWidgetDateFrom();
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
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
        CREATED_PAGES.get(page_1_id),
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
        page_1_id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        emptyList(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    final var page_2_id = PAGES_STEPS.createEnabledPage(getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        abTest_widget,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        experiment,
        getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains("Отвяжите следующие виджеты: [" + abTest_widget.getUid() + "]");
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }

  //TODO:
  // Тест создания (не)дефолтного варианта с привязкой к виджету, имеющего шаренного предка
  //TODO:
  // Тест создания (не)дефолтного варианта с привязкой к виджету, имеющего прямого шаренного предка
  //TODO:
  // Тест создания (не)дефолтного варианта с привязкой к виджету, имеющего шаренного потомка
  //TODO:
  // Тест создания (не)дефолтного варианта с привязкой к виджету, имеющего прямого шаренного потомка
}
