package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class ExpiredExperimentEndDateTimeTest extends BaseTest {

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\tДата окончания эксперимента менее, чем +1 день")
  public void variantAssignedToSharedWidgetExperimentUpdateNegativeTest() throws InterruptedException {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDate();
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget_2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var device = widget_1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        end,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_1.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget_2.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
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
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var deadline = getDeadLine();
    while (Instant.now().isBefore(deadline)) {
      TimeUnit.SECONDS.sleep(1);
    }
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .containsIgnoringCase("Минимально допустимая продолжительность эксперимента "
            + actualExperiment.getUuid() + " составляет: 1 день");
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }
}
