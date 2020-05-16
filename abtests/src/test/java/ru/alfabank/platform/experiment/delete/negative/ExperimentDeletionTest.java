package ru.alfabank.platform.experiment.delete.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class ExperimentDeletionTest extends BaseTest {

  @Test (description = "Негативный тест удаления эксперимента со статусом 'RUNNING'")
  public void experimentRunningTest() {
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
    // TEST //
    assertThat(deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    final var softly = new SoftAssertions();
    softly.assertThat(getAbsentExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_OK);
    softly.assertThat(getAbsentOption(defaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_OK);
    softly.assertThat(getAbsentOption(nonDefaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_OK);
    softly.assertAll();
  }
}
