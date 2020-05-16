package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

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
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var defaultWidget = createWidget(
        createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null, getContentManager());
    final var abTestWidget = createWidget(
        createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = defaultWidget.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEnd, trafficRate, getContentManager());
    createOption(true, List.of(defaultWidget.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(abTestWidget.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    changeWidgetActiveDates(defaultWidget, pageId, start, end, getContentManager());
    changeWidgetActiveDates(abTestWidget, pageId, start, end, getContentManager());
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
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            defaultWidget.getUid(),
            abTestWidget.getUid(),
            "должны быть активны в момент начала и завершения эксперимента");
    getExperiment(actualExperiment, getContentManager()).checkUpdatedExperiment(expectedExperiment);
  }

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата начала отображения виджета больше даты начала эксперимента")
  public void widgetStartDateCrossingExperimentActiveDatesTest() {
    final var widgetStartDate = getCurrentDateTime().plusHours(6).toString();
    final var experimentEndDate = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var defaultWidget = createWidget(
        createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null, getContentManager());
    final var abTestWidget = createWidget(
        createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = defaultWidget.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEndDate, trafficRate, getContentManager());
    createOption(true, List.of(defaultWidget.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(abTestWidget.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    changeWidgetActiveDates(defaultWidget, pageId, widgetStartDate, null, getContentManager());
    changeWidgetActiveDates(abTestWidget, pageId, widgetStartDate, null, getContentManager());
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
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("должны", "быть выключенными");
    getExperiment(actualExperiment, getContentManager()).checkUpdatedExperiment(expectedExperiment);
  }

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Дата окончания отображения виджета меньше даты окончания эксперимента")
  public void widgetEndDateCrossingExperimentActiveDatesTest() {
    final var widgetStart = getCurrentDateTime().plusHours(12).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var defaultWidget = createWidget(
        createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null, getContentManager());
    final var abTestWidget = createWidget(
        createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = defaultWidget.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEnd, trafficRate, getContentManager());
    createOption(true, List.of(defaultWidget.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(abTestWidget.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    changeWidgetActiveDates(defaultWidget, pageId, null, widgetStart, getContentManager());
    changeWidgetActiveDates(abTestWidget, pageId, null, widgetStart, getContentManager());
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
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("должны", "быть выключенными");
    getExperiment(actualExperiment, getContentManager()).checkUpdatedExperiment(expectedExperiment);
  }
}
