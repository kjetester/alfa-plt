package ru.alfabank.platform.experiment.update.deactivate.negative;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.CANCELLED;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.apache.log4j.LogManager;
import org.testng.SkipException;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.experiment.involvements.negative.InvolvementsTest;

public class ExperimentDeactivationTest extends BaseTest {

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'DISABLED'")
  public void experimentDisabledTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
    page = createdPages.get(pageId);
    final var widget2 = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEndDate, trafficRate, getContentManager());
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    // TEST //
    final var result = stopExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно деактивировать эксперимент '" + actualExperiment.getUuid()
            + "' со статусом 'DISABLED'");
    getExperiment(actualExperiment, getContentManager()).equals(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setCreationDate(actualExperiment.getCreationDate())
        .setCreatedBy(actualExperiment.getCreatedBy())
        .setEnabled(false)
        .setStatus(DISABLED)
        .build());
  }

  @Test (description = "Тест деактивации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'CANCELED'")
  public void experimentCanceledTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
    page = createdPages.get(pageId);
    final var widget2 = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEndDate, trafficRate, getContentManager());
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    actualExperiment = runExperimentAssumingSuccess(actualExperiment, getContentManager());
    actualExperiment = stopExperimentAssumingSuccess(actualExperiment, getContentManager());
    // TEST //
    final var result = stopExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно деактивировать эксперимент '" + actualExperiment.getUuid()
            + "' со статусом 'CANCELLED'");
    getExperiment(actualExperiment, getContentManager()).equals(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setCreationDate(actualExperiment.getCreationDate())
        .setCreatedBy(actualExperiment.getCreatedBy())
        .setActivationDate(actualExperiment.getActivationDate())
        .setActivatedBy(actualExperiment.getActivatedBy())
        .setDeactivationDate(getCurrentDateTime().toString())
        .setDeactivatedBy(getContentManager().getLogin())
        .setEnabled(false)
        .setStatus(CANCELLED)
        .build());
  }

  @Test (description = "Тест деактивации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'EXPIRED'")
  public void experimentExpiredTest() {
    LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
