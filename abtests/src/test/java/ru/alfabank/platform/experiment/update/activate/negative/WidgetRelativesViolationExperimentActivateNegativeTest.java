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
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class WidgetRelativesViolationExperimentActivateNegativeTest extends BaseTest {

  @Test(description = """
      Тест создания дефлотного варианта с привязкой к виджету:
      \t* enable=true
      \t* experimentOptionName=default
      \t* defaultWidget=true
      \t* с негативным условием:
      \t\t1. Прямой потомок:
      \t\t\t* enable=false
      \t\t\t* experimentOptionName=default
      \t\t\t* defaultWidget=true
      \t\t2. Прямой потомок:
      \t\t\t* enable=false
      \t\t\t* experimentOptionName=forABtest
      \t\t\t* defaultWidget=false""")
  public void defaultWidgetHasInvalidRelativesExperimentActivateNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget_1 = DRAFT_STEPS.createWidget(
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
    final var default_widget_1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        default_widget_1,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var default_widget_1_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        default_widget_1,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var abTest_widget_1 = DRAFT_STEPS.createWidget(
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
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget_1.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget_1.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        default_widget_1_1,
        page_id,
        false,
        DEFAULT,
        true,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        default_widget_1_2,
        page_id,
        false,
        FOR_AB_TEST,
        false,
        getContentManager());
    // TEST //
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains("Для варианта по умолчанию ",
            default_widget_1_1.getUid(), default_widget_1_2.getUid(),
            "' должны быть с дефолтным названием варианта, быть включенными и быть виджетами по "
                + "умолчанию");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .equals(actualExperiment);
  }

  @Test(description = """
      Тест создания недефлотного варианта с привязкой к виджету:
      \t* enable=false
      \t* experimentOptionName=forABtest
      \t* defaultWidget=false
      \t* с негативным условием:
      \t\t1. Предок:
      \t\t\t* enable=false
      \t\t\t* experimentOptionName=forABtest
      \t\t\t* defaultWidget=false""")
  public void nonDefaultWidgetHasInvalidAncestorExperimentActivateNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
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
    final var widget_2 = DRAFT_STEPS.createWidget(
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
    final var widget_2_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_2,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var widget_2_1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_2_1,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_1.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget_2_1_1.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        widget_2,
        page_id,
        false,
        FOR_AB_TEST,
        false,
        getContentManager());
    // TEST //
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget_2_1_1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .equals(actualExperiment);
  }

  @Test(description = """
      Тест создания недефлотного варианта с привязкой к виджету:
      \t* enable=false
      \t* experimentOptionName=forABtest
      \t* defaultWidget=false
      \t* с негативным условием:
      \t\t1. Прямой предок:
      \t\t\t* enable=false
      \t\t\t* experimentOptionName=forABtest
      \t\t\t* defaultWidget=false""")
  public void nonDefaultWidgetHasInvalidFatherExperimentActivateNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget_1 = DRAFT_STEPS.createWidget(
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
    final var default_widget_2 = DRAFT_STEPS.createWidget(
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
    final var abTest_widget_2_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        default_widget_2,
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
        List.of(default_widget_1.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget_2_1.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        default_widget_2,
        page_id,
        false,
        FOR_AB_TEST,
        false,
        getContentManager());
    // TEST //
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(experiment, getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains("Виджет '" + abTest_widget_2_1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }

  @Test(description = """
      Тест создания недефлотного варианта с привязкой к виджету:
      \t* enable=false
      \t* experimentOptionName=forABtest
      \t* defaultWidget=false
      \t* с негативным условием:
      \t\t1. Ребенок:
      \t\t\t* enable=false
      \t\t\t* experimentOptionName=forABtest
      \t\t\t* defaultWidget=false""")
  public void nonDefaultWidgetHasInvalidChildrenExperimentActivateNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var desktop_widget_1 = DRAFT_STEPS.createWidget(
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
    final var abTest_widget_2 = DRAFT_STEPS.createWidget(
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
    final var abTest_widget_2_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        abTest_widget_2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var abTest_widget_2_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        abTest_widget_2,
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
        List.of(desktop_widget_1.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget_2.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        abTest_widget_2_1,
        page_id,
        true,
        DEFAULT,
        true,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        abTest_widget_2_2,
        page_id,
        false,
        DEFAULT,
        true,
        getContentManager());
    // TEST //
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(experiment, getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains("Для варианта '", abTest_widget_2_1.getUid(), abTest_widget_2_2.getUid(),
            "' должны быть помечены как 'forABtest',"
                + " быть выключенными и не должны быть виджетами по умолчанию");
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }
}
