package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.Status.DISABLED;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class ExpiredExperimentEndDateTimeTest extends BaseTest {

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\tДата окончания эксперимента менее, чем +1 день")
  public void variantAssignedToSharedWidgetTest() throws InterruptedException {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5);
    final var endDateTimeAsString = end.toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    createWidget(createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1 = page.getWidgetList().get(0);
    final var widget2 = page.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), endDateTimeAsString, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
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
        .setCreatedBy(USER.getLogin())
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var deadline = end.toLocalDateTime().minusHours(21);
    while (LocalDateTime.now().isBefore(deadline)) {
      TimeUnit.SECONDS.sleep(1);
    }
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .containsIgnoringCase("Минимально допустимая продолжительность эксперимента "
            + actualExperiment.getUuid() + " составляет: 1 день");
    getExperiment(actualExperiment).checkUpdatedExperiment(expectedExperiment);
  }
}
