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

public class WidgetRelativesViolationTest extends BaseTest {

  @Test(description = "Тест создания дефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=true"
      + "\n\t* experimentOptionName=default"
      + "\n\t* defaultWidget=true"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Прямой потомок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=default"
      + "\n\t\t\t* defaultWidget=true"
      + "\n\t\t2. Прямой потомок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void defaultWidgetHasInvalidRelativesExperimentUpdateNegativeTest() {
    final var start = getValidEndDatePlus10Seconds();
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
    final var widget1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_1,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget1_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_1,
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
    final var device = widget_1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        getValidEndDate(),
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
        List.of(widget2.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        widget1_1,
        page_id,
        false,
        DEFAULT,
        true,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        widget1_2,
        page_id,
        false,
        FOR_AB_TEST,
        false,
        getContentManager());
    // TEST //
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для варианта по умолчанию ", widget1_1.getUid(), widget1_2.getUid(),
            "' должны быть с дефолтным названием варианта, быть включенными и быть виджетами по "
                + "умолчанию");
    EXPERIMENT_STEPS.getExistingExperiment(
        actualExperiment,
        getContentManager())
        .equals(new Experiment.Builder()
            .setUuid(actualExperiment.getUuid())
            .setCookieValue(actualExperiment.getCookieValue())
            .setDescription(actualExperiment.getDescription())
            .setPageId(actualExperiment.getPageId())
            .setProductTypeKey(actualExperiment.getProductTypeKey())
            .setEndDate(actualExperiment.getEndDate())
            .setTrafficRate(actualExperiment.getTrafficRate())
            .setDevice(actualExperiment.getDevice())
            .setCreatedBy(getContentManager().getLogin())
            .setStatus(DISABLED)
            .setEnabled(false)
            .setCreationDate(start)
            .build());
  }

  @Test(description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Предок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void nonDefaultWidgetHasInvalidAncestorExperimentUpdateNegativeTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var experiment_end = getValidEndDate();
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
        true,
        DEFAULT,
        true,
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
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        experiment_end,
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
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget_2_1_1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
    EXPERIMENT_STEPS.getExistingExperiment(
        actualExperiment,
        getContentManager())
        .equals(new Experiment.Builder()
            .setUuid(actualExperiment.getUuid())
            .setCookieValue(actualExperiment.getCookieValue())
            .setDescription(actualExperiment.getDescription())
            .setPageId(actualExperiment.getPageId())
            .setProductTypeKey(actualExperiment.getProductTypeKey())
            .setEndDate(actualExperiment.getEndDate())
            .setTrafficRate(actualExperiment.getTrafficRate())
            .setDevice(actualExperiment.getDevice())
            .setCreatedBy(getContentManager().getLogin())
            .setStatus(DISABLED)
            .setEnabled(false)
            .setCreationDate(start)
            .build());
  }

  @Test(description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Прямой предок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void nonDefaultWidgetHasInvalidFatherExperimentUpdateNegativeTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var experiment_end = getValidEndDate();
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
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget_2_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        experiment_end,
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
        List.of(widget_2_1.getUid()),
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
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget_2_1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
    EXPERIMENT_STEPS.getExistingExperiment(
        actualExperiment,
        getContentManager())
        .equals(new Experiment.Builder()
            .setUuid(actualExperiment.getUuid())
            .setCookieValue(actualExperiment.getCookieValue())
            .setDescription(actualExperiment.getDescription())
            .setPageId(actualExperiment.getPageId())
            .setProductTypeKey(actualExperiment.getProductTypeKey())
            .setEndDate(actualExperiment.getEndDate())
            .setTrafficRate(actualExperiment.getTrafficRate())
            .setDevice(actualExperiment.getDevice())
            .setCreatedBy(getContentManager().getLogin())
            .setStatus(DISABLED)
            .setEnabled(false)
            .setCreationDate(start)
            .build());
  }

  @Test(description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Ребенок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void nonDefaultWidgetHasInvalidChildrenExperimentUpdateNegativeTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var experiment_end = getValidEndDate();
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
    final var widget_2_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget_2_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        experiment_end,
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
        List.of(widget_2.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        widget_2_1,
        page_id,
        true,
        DEFAULT,
        true,
        getContentManager());
    DRAFT_STEPS.changeWidgetABtestProps(
        widget_2_2,
        page_id,
        false,
        DEFAULT,
        true,
        getContentManager());
    // TEST //
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для варианта '", widget_2_1.getUid(), widget_2_2.getUid(),
            "' должны быть помечены как 'forABtest',"
                + " быть выключенными и не должны быть виджетами по умолчанию");
    EXPERIMENT_STEPS.getExistingExperiment(
        actualExperiment,
        getContentManager())
        .equals(new Experiment.Builder()
            .setUuid(actualExperiment.getUuid())
            .setCookieValue(actualExperiment.getCookieValue())
            .setDescription(actualExperiment.getDescription())
            .setPageId(actualExperiment.getPageId())
            .setProductTypeKey(actualExperiment.getProductTypeKey())
            .setEndDate(actualExperiment.getEndDate())
            .setTrafficRate(actualExperiment.getTrafficRate())
            .setDevice(actualExperiment.getDevice())
            .setCreatedBy(getContentManager().getLogin())
            .setStatus(DISABLED)
            .setEnabled(false)
            .setCreationDate(start)
            .build());
  }
}
