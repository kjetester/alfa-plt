package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.Status.DISABLED;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class WidgetActiveDatesCrossingExperimentActiveDatesTest extends BaseTest {

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата начала отображения виджета больше даты начала эксперимента"
      + "\n\t2. Дата окончания отображения виджета меньше даты окончания эксперимента")
  public void widgetActiveDatesCrossingExperimentActiveDatesTest() {
    final var start = getCurrentDateTime().plusHours(6).toString();
    final var end = getCurrentDateTime().plusHours(12).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    createWidget(createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1 = page.getWidgetList().get(0);
    final var widget2 = page.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEnd, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
    changeWidgetActiveDates(widget1, pageId, start, end);
    changeWidgetActiveDates(widget2, pageId,start, end);
    final var experimentStart = getCurrentDateTime().plusSeconds(10).toString();
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
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            widget1.getUid(),
            widget2.getUid(),
            "должны быть активны в момент начала и завершения эксперимента");
    getExperiment(actualExperiment).checkUpdatedExperiment(expectedExperiment);
  }

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата начала отображения виджета больше даты начала эксперимента")
  public void widgetStartDateCrossingExperimentActiveDatesTest() {
    final var start = getCurrentDateTime().plusHours(6).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    createWidget(createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1 = page.getWidgetList().get(0);
    final var widget2 = page.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEnd, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
    changeWidgetActiveDates(widget1, pageId, start, null);
    changeWidgetActiveDates(widget2, pageId,start, null);
    final var experimentStart = getCurrentDateTime().plusSeconds(10).toString();
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
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            widget1.getUid(),
            widget2.getUid(),
            "должны быть активны в момент начала и завершения эксперимента");
    getExperiment(actualExperiment).checkUpdatedExperiment(expectedExperiment);
  }

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата окончания отображения виджета меньше даты окончания эксперимента")
  public void widgetEndDateCrossingExperimentActiveDatesTest() {
    final var widgetStart = getCurrentDateTime().plusHours(12).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    createWidget(createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1 = page.getWidgetList().get(0);
    final var widget2 = page.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEnd, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
    changeWidgetActiveDates(widget1, pageId, null, widgetStart);
    changeWidgetActiveDates(widget2, pageId, null, widgetStart);
    final var experimentStart = getCurrentDateTime().plusSeconds(10).toString();
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
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            widget1.getUid(),
            widget2.getUid(),
            "должны быть активны в момент начала и завершения эксперимента");
    getExperiment(actualExperiment).checkUpdatedExperiment(expectedExperiment);
  }
}
