package ru.alfabank.platform.experiment.update.deactivate.negative;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.Status.CANCELLED;
import static ru.alfabank.platform.businessobjects.Status.DISABLED;

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
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page,
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null);
    page = createdPages.get(pageId);
    final var widget2 = createWidget(
        page,
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEndDate, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
    // TEST //
    actualExperiment = stopDisabledExperiment(actualExperiment);
    getExperiment(actualExperiment).equals(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(false)
        .setCreationDate(actualExperiment.getCreationDate())
        .setCreatedBy(actualExperiment.getCreatedBy())
        .setStatus(DISABLED)
        .build());
  }

  @Test (description = "Тест деактивации эксперимента с негативным условием:"
      + "\n\t1. Эксперимент имеет статус 'CANCELED'")
  public void experimentCanceledTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page,
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null);
    page = createdPages.get(pageId);
    final var widget2 = createWidget(
        page,
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEndDate, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
    actualExperiment = runExperimentAssumingSuccess(actualExperiment);
    actualExperiment = stopExperimentAssumingSuccess(actualExperiment);
    // TEST //
    final var result = stopExperimentAssumingFail(actualExperiment);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Невозможно обновить эксперимент '" + actualExperiment.getUuid()
            + "' со статусом 'CANCELLED'");
    getExperiment(actualExperiment).equals(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(false)
        .setCreationDate(actualExperiment.getCreationDate())
        .setCreatedBy(actualExperiment.getCreatedBy())
        .setActivationDate(actualExperiment.getActivationDate())
        .setActivatedBy(actualExperiment.getActivatedBy())
        .setDeactivationDate(getCurrentDateTime().toString())
        .setDeactivatedBy(USER.getLogin())
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
