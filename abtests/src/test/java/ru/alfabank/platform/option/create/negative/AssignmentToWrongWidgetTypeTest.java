package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class AssignmentToWrongWidgetTypeTest extends OptionBaseTest {

  @Test (description = "Тест создания варианта с привязкой к виджету с некорректным значением "
      + "enabled", dataProvider = "dataProvider")
  public void assignmentWidgetEnabledValueTest(
      @ParameterKey ("Дефолтный вариант") final Boolean isDefaultOption) {
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    page = createdPages.get(pageId);
    Widget widget1;
    Widget widget2 = null;
    if (isDefaultOption) {
      widget1 = createWidget(page, null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    } else {
      widget1 = createWidget(page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
      widget2 = createWidget(page, null, desktop, false, DEFAULT, true, null, null, getContentManager());
    }
    final var experiment = createExperiment(
            widget1.getDevice(), pageId, getRandomProductType(), experimentEnd, .5D, getContentManager());
    Response response1;
    Response response2 = null;
    if (isDefaultOption) {
      response1 = createOptionAssumingFail(
              isDefaultOption, List.of(widget1.getUid()), experiment.getUuid(), .5D, getContentManager());
    } else {
      response1 = createOptionAssumingFail(
              isDefaultOption, List.of(widget1.getUid()), experiment.getUuid(), .5D, getContentManager());
      response2 = createOptionAssumingFail(
              isDefaultOption, List.of(widget2.getUid()), experiment.getUuid(), .5D, getContentManager());
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
