package ru.alfabank.platform.option.create.negative;

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
import ru.alfabank.platform.option.OptionBaseTest;

public class DefaultWidgetHasInvalidRelativesTest extends OptionBaseTest {

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
  public void defaultWidgetHasInvalidRelativesNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget1 = DRAFT_STEPS.createWidget(
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
    final var widget1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget1,
        desktop,
        false,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var widget1_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget1,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var device = widget1.getDevice();
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    final var result = OPTION_STEPS.createOptionAssumingFail(
        true,
        List.of(widget1.getUid()),
        actualExperiment.getUuid(),
        .5D,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для варианта по умолчанию '", widget1_1.getUid(), widget1_2.getUid(),
            "должны быть с дефолтным названием варианта, быть включенными и быть виджетами по "
                + "умолчанию");
  }
}