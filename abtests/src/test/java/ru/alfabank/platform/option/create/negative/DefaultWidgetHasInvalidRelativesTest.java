package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.enums.User;
import ru.alfabank.platform.option.OptionBaseTest;

public class DefaultWidgetHasInvalidRelativesTest extends OptionBaseTest {

  @Test (description = "Тест создания дефлотного варианта с привязкой к виджету:"
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
  public void defaultWidgetHasInvalidRelativesTest() {
    setUser(User.CONTENT_MANAGER);
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null);
    final var widget1_1 = createWidget(
        page, widget1, desktop, false, DEFAULT, true, null, null);
    final var widget1_2 = createWidget(
        page, widget1, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var actualExperiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEnd, .5D);
    final var result = createOptionAssumingFail(
        true, List.of(widget1.getUid()), actualExperiment.getUuid(), .5D);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для варианта по умолчанию '", widget1_1.getUid(), widget1_2.getUid(),
            "должны быть с дефолтным названием варианта, быть включенными и быть виджетами по "
                + "умолчанию");
  }
}