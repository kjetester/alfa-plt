package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.ProductType.getRandomProductType;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class DefaultOptionLimitViolationTest extends OptionBaseTest {

  @Test (description = "Тест создания более одного дефолтного варианта одного эксперимента")
  public void defaultOptionLimitViolationTest() {
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, null, null);
    final var widget2 = createWidget(page, null, desktop, true, DEFAULT, true, null, null);
    page = createdPages.get(pageId);
    final var device = widget1.getDevice();
    final var experiment = createExperiment(
            device, pageId, getRandomProductType(), experimentEnd, .5D);
    createOption(true, List.of(widget1.getUid()), experiment.getUuid(), .5D);
    final var result = createOptionAssumingFail(
            true, List.of(widget2.getUid()), experiment.getUuid(), .5D);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для эксперимента '" + experiment.getUuid()
                + "' уже существует дефолтный вариант");
  }
}
