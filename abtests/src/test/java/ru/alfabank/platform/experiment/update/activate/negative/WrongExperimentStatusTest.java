package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.ProductType.getRandomProductType;

import java.util.List;
import org.apache.log4j.LogManager;
import org.testng.SkipException;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.experiment.involvements.negative.InvolvementsTest;

public class WrongExperimentStatusTest extends BaseTest {

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'RUNNING'")
  public void experimentRunningTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    createWidget(createdPages.get(pageId), null, desktop,true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop,false, FOR_AB_TEST, false, null, null);
    final var widget1 = page.getWidgetList().get(0);
    final var widget2 = page.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEndDate, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
    // TEST //
    actualExperiment = runExperimentAssumingSuccess(actualExperiment);
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно изменить активный эксперимент " + actualExperiment.getUuid());
    getExperiment(actualExperiment).checkActivatedExperiment(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(actualExperiment.getEnabled())
        .setCreatedBy(actualExperiment.getCreatedBy())
        .setActivationDate(getCurrentDateTime().toString())
        .setActivatedBy(USER.getLogin())
        .setStatus(actualExperiment.getStatus())
        .setCreationDate(actualExperiment.getCreationDate())
        .build());
  }

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'CANCELED'")
  public void experimentCanceledTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    createWidget(createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1 = page.getWidgetList().get(0);
    final var widget2 = page.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    // TEST //
    var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEndDate, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
    actualExperiment = runExperimentAssumingSuccess(actualExperiment);
    actualExperiment = stopExperimentAssumingSuccess(actualExperiment);
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно активировать эксперимент '" + actualExperiment.getUuid()
            + "' со статусом 'CANCELLED'");
    getExperiment(actualExperiment).checkActivatedExperiment(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(actualExperiment.getEnabled())
        .setCreationDate(actualExperiment.getCreationDate())
        .setCreatedBy(actualExperiment.getCreatedBy())
        .setActivationDate(actualExperiment.getActivationDate())
        .setActivatedBy(actualExperiment.getActivatedBy())
        .setDeactivationDate(getCurrentDateTime().toString())
        .setDeactivatedBy(USER.getLogin())
        .setStatus(actualExperiment.getStatus())
        .build());
  }

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'EXPIRED'")
  public void experimentExpiredTest() {
    LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
