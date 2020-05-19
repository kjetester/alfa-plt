package ru.alfabank.platform.experiment.update.activate.negative;

import static java.util.Collections.emptyList;
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

public class VariantsAssignedToSharedWidgetsTest extends BaseTest {

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tВарианты привязаны к шаренным виджетам")
  public void bothVariantsAssignedToSharedWidgetExperimentUpdateNegativeTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDate();
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
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
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_1_id,
        getRandomProductType(),
        end,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_1.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget_2.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    final var page_2_id = PAGES_STEPS.createEnabledPage(getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        widget_1,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        widget_2,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
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
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains(widget_2.getUid(), widget_1.getUid());
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tДефолтный вариант привязан к шаренному виджету")
  public void defaultVariantAssignedToSharedWidgetExperimentUpdateNegativeTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDate();
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
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
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_1_id,
        getRandomProductType(),
        end,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_1.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        emptyList(),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    final var page_2_id = PAGES_STEPS.createPage(
        start,
        end,
        true,
        getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        widget_1,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
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
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains("Отвяжите следующие виджеты: [" + widget_1.getUid() + "]");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tНедефолтный вариант привязан к шаренному виджету")
  public void nonDefaultVariantAssignedToSharedWidgetExperimentUpdateNegativeTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDate();
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
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
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_1_id,
        getRandomProductType(),
        end,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        emptyList(),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget_2.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    final var page_2_id = PAGES_STEPS.createPage(
        start,
        end,
        true,
        getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        widget_2,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
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
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains("Отвяжите следующие виджеты: [" + widget_2.getUid() + "]");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
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
