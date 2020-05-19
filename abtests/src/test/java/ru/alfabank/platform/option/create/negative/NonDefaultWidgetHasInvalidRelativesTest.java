package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class NonDefaultWidgetHasInvalidRelativesTest extends OptionBaseTest {

  @Test(description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Предок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void nonDefaultWidgetHasInvalidAncestorNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget0 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget0,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget1,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var device = widget1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());
    final var result = OPTION_STEPS.createOptionAssumingFail(
        false,
        List.of(widget1_1.getUid()),
        actualExperiment.getUuid(),
        .05D,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget1_1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
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
  public void nonDefaultWidgetHasInvalidFatherNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget0 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget0,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var device = widget1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());
    final var result = OPTION_STEPS.createOptionAssumingFail(
        false,
        List.of(widget1.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
  }

  @Test(description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Ребенок:"
      + "\n\t\t\t* enable=true/false"
      + "\n\t\t\t* experimentOptionName=default"
      + "\n\t\t\t* defaultWidget=true")
  public void nonDefaultWidgetHasInvalidChildrenNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget1,
        desktop,
        false,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget1_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget1,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var device = widget1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());
    final var result = OPTION_STEPS.createOptionAssumingFail(
        false,
        List.of(widget1.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для варианта '", widget1_1.getUid(), widget1_2.getUid(),
            "' должны быть помечены как 'forABtest', быть выключенными и не должны быть "
                + "виджетами по умолчанию");
  }
}