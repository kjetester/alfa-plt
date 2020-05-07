package ru.alfabank.platform.experiment.delete.positive;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.ProductType.getRandomProductType;

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
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, null, null);
    page = createdPages.get(pageId);
    final var widget2 = createWidget(page, null, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var experiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEndDate, trafficRate);
    final var defaultOption = createOption(
        true, List.of(widget1.getUid()), experiment.getUuid(), trafficRate);
    final var nonDefaultOption = createOption(
        false, List.of(widget2.getUid()), experiment.getUuid(),
        trafficRate);
    // TEST //
    assertThat(deleteExperiment(experiment).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NO_CONTENT);
    final var softly = new SoftAssertions();
    softly.assertThat(getAbsentExperiment(experiment).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(getAbsentOption(defaultOption).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(getAbsentOption(nonDefaultOption).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertAll();
  }

  @Test (description = "Позитивный тест удаления эксперимента со статусом 'CANCELLED'")
  public void experimentCancelledTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, null, null);
    page = createdPages.get(pageId);
    final var widget2 = createWidget(page, null, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var experiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEndDate, trafficRate);
    final var defaultOption = createOption(
        true, List.of(widget1.getUid()), experiment.getUuid(), trafficRate);
    final var nonDefaultOption = createOption(
        false, List.of(widget2.getUid()), experiment.getUuid(), trafficRate);
    runExperimentAssumingSuccess(experiment);
    stopExperimentAssumingSuccess(experiment);
    // TEST //
    assertThat(deleteExperiment(experiment).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NO_CONTENT);
    final var softly = new SoftAssertions();
    softly.assertThat(getAbsentExperiment(experiment).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(getAbsentOption(defaultOption).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(getAbsentOption(nonDefaultOption).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertAll();
  }

  @Test (description = "Позитивный тест удаления эксперимента со статусом 'EXPIRED'")
  public void experimentExpiredTest() {
    LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
