package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.contentstore.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class AssignmentToWrongWidgetTypeTest extends OptionBaseTest {

  @Test(description = "Тест создания варианта с привязкой к виджету с некорректным значением "
      + "enabled", dataProvider = "dataProvider")
  public void assignmentWidgetEnabledValueNegativeTest(
      @ParameterKey("Дефолтный вариант") final Boolean isDefaultOption) {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    Widget widget1;
    Widget widget2 = null;
    if (isDefaultOption) {
      widget1 = DRAFT_STEPS.createWidget(
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
    } else {
      widget1 = DRAFT_STEPS.createWidget(
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
      widget2 = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_id),
          null,
          desktop,
          false,
          DEFAULT,
          true,
          List.of(RU),
          null,
          null,
          getContentManager());
    }
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        widget1.getDevice(),
        page_id,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    Response response1;
    Response response2 = null;
    if (isDefaultOption) {
      response1 = OPTION_STEPS.createOptionAssumingFail(
          true,
          List.of(widget1.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
    } else {
      response1 = OPTION_STEPS.createOptionAssumingFail(
          false,
          List.of(widget1.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
      response2 = OPTION_STEPS.createOptionAssumingFail(
          false,
          List.of(widget2.getUid()),
          experiment.getUuid(),
          .5D,
          getContentManager());
    }
    final var softly = new SoftAssertions();
    softly.assertThat(response1.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    if (isDefaultOption) {
      softly.assertThat(response1.getBody().asString())
          .as("Проверка сообщения")
          .contains(widget1.getUid(),
              " должны быть с дефолтным названием варианта, быть включенными и быть виджетами по "
                  + "умолчанию");
    } else {
      softly.assertThat(response1.getBody().asString())
          .as("Проверка сообщения")
          .contains(widget1.getUid(),
              "должны быть помечены как 'forABtest', быть выключенными и не должны быть виджетами "
                  + "по умолчанию");
      softly.assertThat(response2.getStatusCode())
          .as("Проверка статус-кода")
          .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
      softly.assertThat(response2.getBody().asString())
          .as("Проверка сообщения")
          .contains(widget2.getUid(),
              "должны быть помечены как 'forABtest', быть выключенными и не должны быть виджетами "
                  + "по умолчанию");
    }
    softly.assertAll();
  }
}
