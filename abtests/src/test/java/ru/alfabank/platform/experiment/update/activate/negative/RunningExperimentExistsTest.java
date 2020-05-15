package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.enums.User;

public class RunningExperimentExistsTest extends BaseTest {

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\tЕсть запущенный эксперимент")
  public void runningExperimentExistsTest() {
    setUser(User.CONTENT_MANAGER);
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    Integer pageId = page.getId();
    createWidget(createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null);
    createWidget(createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null);
    page = createdPages.get(pageId);
    final var widget1 = page.getWidgetList().get(0);
    final var widget2 = page.getWidgetList().get(1);
    final var widget3 = page.getWidgetList().get(2);
    final var widget4 = page.getWidgetList().get(3);
    var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var runningExperiment =
        createExperiment(device, pageId, getRandomProductType(), end, trafficRate);
    createOption(true, List.of(widget1.getUid()), runningExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), runningExperiment.getUuid(), trafficRate);
    runExperimentAssumingSuccess(runningExperiment);
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), end, trafficRate);
    createOption(true, List.of(widget3.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget4.getUid()), actualExperiment.getUuid(), trafficRate);
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
        .setCreatedBy(getUser().getLogin())
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .containsIgnoringCase("Для страницы '" + pageId + "' и типа устройства '"
            + expectedExperiment.getDevice() + "' уже существует активный эксперимент");
    getExperiment(actualExperiment).checkUpdatedExperiment(expectedExperiment);
  }
}
