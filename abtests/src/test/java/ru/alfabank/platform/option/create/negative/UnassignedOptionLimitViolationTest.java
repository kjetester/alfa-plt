package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class UnassignedOptionLimitViolationTest extends OptionBaseTest {

  @Test (description = "Тест создания более одного непривязанного к виджету варианта",
      dataProvider = "dataProvider")
  public void oneDefaultOptionLimitTest(
      @ParameterKey ("Дефолтный вариант") final Boolean isDefaultOption) {
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var experiment = createExperiment(
        desktop, pageId, getRandomProductType(), experimentEnd, .5D);
    final Response result;
    if (isDefaultOption) {
      createOption(false, List.of(), experiment.getUuid(), .5D);
      result = createOptionAssumingFail(true, List.of(), experiment.getUuid(), .5D);
    } else {
      createOption(true, List.of(), experiment.getUuid(), .5D);
      result = createOptionAssumingFail(false, List.of(), experiment.getUuid(), .5D);
    }
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Только один вариант эксперимента '" + experiment.getUuid()
            + "' может иметь пустой список связанных виджетов");
  }
}