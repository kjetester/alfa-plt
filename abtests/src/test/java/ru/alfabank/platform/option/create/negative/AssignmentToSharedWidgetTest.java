package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class AssignmentToSharedWidgetTest extends OptionBaseTest {

  @Test(description = "Тест создания (не)дефолтного варианта с привязкой к шаренному виджету",
      dataProvider = "dataProvider")
  public void assignmentToSharedWidgetNegativeTest(
      @ParameterKey("Дефолтный вариант") final Boolean isDefaultOption) {
    final var page_1_id = PAGES_STEPS.createPage(null, null, true, getContentManager());
    Widget widget;
    if (isDefaultOption) {
      widget = DRAFT_STEPS.createWidget(
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
    } else {
      widget = DRAFT_STEPS.createWidget(
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
    }
    final var page_2_id = PAGES_STEPS.createPage(
        null,
        null,
        true,
        getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        widget,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        widget.getDevice(),
        page_1_id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    Response response;
    if (isDefaultOption) {
      response = OPTION_STEPS.createOptionAssumingFail(
          true,
          List.of(widget.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
    } else {
      response = OPTION_STEPS.createOptionAssumingFail(
          false,
          List.of(widget.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
    }
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(response.getBody().asString())
        .as("Проверка сообщения")
        .contains("В эксперименте не могут участвовать виджеты, общие для нескольких страниц."
            + " Отвяжите следующие виджеты:", widget.getUid());
  }

  @Test(description = "Тест создания (не)дефолтного варианта с привязкой к виджету,"
      + " имеющего шаренного предка", dataProvider = "dataProvider")
  public void assignmentToWidgetWhichHasSharedAncestorNegativeTest(
      @ParameterKey("Дефолтный вариант") final Boolean isDefaultOption) {
    final var page_1_Id = PAGES_STEPS.createPage(
        null,
        null,
        true,
        getContentManager());
    final var sharedAncestor = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_Id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var ancestor = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_Id),
        sharedAncestor,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    Widget widget;
    if (isDefaultOption) {
      widget = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_1_Id),
          ancestor,
          desktop,
          true,
          DEFAULT,
          true,
          List.of(RU),
          null,
          null,
          getContentManager());
    } else {
      widget = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_1_Id),
          ancestor,
          desktop,
          false,
          FOR_AB_TEST,
          false,
          List.of(RU),
          null,
          null,
          getContentManager());
    }
    final var page_2_id = PAGES_STEPS.createPage(
        null,
        null,
        true,
        getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        sharedAncestor,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        widget.getDevice(),
        page_1_Id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    Response response;
    if (isDefaultOption) {
      response = OPTION_STEPS.createOptionAssumingFail(
          true,
          List.of(widget.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
    } else {
      response = OPTION_STEPS.createOptionAssumingFail(
          false,
          List.of(widget.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
    }
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(response.getBody().asString())
        .as("Проверка сообщения")
        .contains("В эксперименте не могут участвовать виджеты, общие для нескольких страниц."
            + " Отвяжите следующие виджеты:");
  }

  @Test(description = "Тест создания (не)дефолтного варианта с привязкой к виджету,"
      + " имеющего шаренного потомка", dataProvider = "dataProvider")
  public void assignmentToWidgetWhichHasSharedDescendantNegativeTest(
      @ParameterKey("Дефолтный вариант") final Boolean isDefaultOption) {
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    Widget widget;
    Widget sharedDescendant;
    if (isDefaultOption) {
      widget = DRAFT_STEPS.createWidget(
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
      final var descendant = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_1_id),
          widget,
          desktop,
          true,
          DEFAULT,
          true,
          List.of(RU),
          null,
          null,
          getContentManager());
      sharedDescendant = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_1_id),
          descendant,
          desktop,
          true,
          DEFAULT,
          true,
          List.of(RU),
          null,
          null,
          getContentManager());
    } else {
      widget = DRAFT_STEPS.createWidget(
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
      final var descendant = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_1_id),
          widget,
          desktop,
          false,
          FOR_AB_TEST,
          false,
          List.of(RU),
          null,
          null,
          getContentManager());
      sharedDescendant = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_1_id),
          descendant,
          desktop,
          false,
          FOR_AB_TEST,
          false,
          List.of(RU),
          null,
          null,
          getContentManager());
    }
    final var page_2_id = PAGES_STEPS.createEnabledPage(getContentManager());
    DRAFT_STEPS.shareWidgetToAnotherPage(
        sharedDescendant,
        CREATED_PAGES.get(page_2_id),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        widget.getDevice(),
        page_1_id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    Response response;
    if (isDefaultOption) {
      response = OPTION_STEPS.createOptionAssumingFail(
          true,
          List.of(widget.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
    } else {
      response = OPTION_STEPS.createOptionAssumingFail(
          false,
          List.of(widget.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
    }
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(response.getBody().asString())
        .as("Проверка сообщения")
        .contains("В эксперименте не могут участвовать виджеты, общие для нескольких страниц."
            + " Отвяжите следующие виджеты:", sharedDescendant.getUid());
  }
}
