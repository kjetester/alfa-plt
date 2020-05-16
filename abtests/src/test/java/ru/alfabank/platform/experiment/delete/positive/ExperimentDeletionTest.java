package ru.alfabank.platform.experiment.delete.positive;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.apache.log4j.LogManager;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.experiment.involvements.negative.InvolvementsTest;

public class ExperimentDeletionTest extends BaseTest {

  @Test (description = "Позитивный тест удаления эксперимента со статусом 'DISABLED'")
  public void experimentDisabledTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
    page = createdPages.get(pageId);
    final var widget2 = createWidget(page, null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var experiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEndDate, trafficRate, getContentManager());
    final var defaultOption = createOption(
        true, List.of(widget1.getUid()), experiment.getUuid(), trafficRate, getContentManager());
    final var nonDefaultOption = createOption(
        false, List.of(widget2.getUid()), experiment.getUuid(), trafficRate, getContentManager());
    // TEST //
    assertThat(deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NO_CONTENT);
    final var softly = new SoftAssertions();
    softly.assertThat(getAbsentExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(getAbsentOption(defaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(getAbsentOption(nonDefaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertAll();
  }

  @Test (description = "Позитивный тест удаления эксперимента со статусом 'CANCELLED'")
  public void experimentCancelledTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
    page = createdPages.get(pageId);
    final var widget2 = createWidget(page, null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var experiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEndDate, trafficRate, getContentManager());
    final var defaultOption = createOption(
        true, List.of(widget1.getUid()), experiment.getUuid(), trafficRate, getContentManager());
    final var nonDefaultOption = createOption(
        false, List.of(widget2.getUid()), experiment.getUuid(), trafficRate, getContentManager());
    runExperimentAssumingSuccess(experiment, getContentManager());
    stopExperimentAssumingSuccess(experiment, getContentManager());
    // TEST //
    assertThat(deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NO_CONTENT);
    final var softly = new SoftAssertions();
    softly.assertThat(getAbsentExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(getAbsentOption(defaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(getAbsentOption(nonDefaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertAll();
  }

  @Test (description = "Позитивный тест удаления эксперимента со статусом 'EXPIRED'")
  public void experimentExpiredTest() {
    LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
