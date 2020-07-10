package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class AssignmentToAlreadyAssignedWidgetTest extends OptionBaseTest {

  @Test(description = "Тест создания варианта с ассоциацией с виджетом, который уже ассоциирован "
      + "с другим вариантом")
  public void assignmentToAlreadyAssignedWidgetNegativeTest() {
    final var pageId =
        PAGES_STEPS.createPage(
            null,
            null,
            true,
            null,
            getContentManager());
    final var widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
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
        widget.getDevice(),
        pageId,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    final var response = OPTION_STEPS.createOptionAssumingFail(
        false,
        List.of(widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(response.getBody().asString())
        .as("Проверка сообщения")
        .contains("Обнаружены виджеты, использущиеся в нескольких вариантах");
  }
}
