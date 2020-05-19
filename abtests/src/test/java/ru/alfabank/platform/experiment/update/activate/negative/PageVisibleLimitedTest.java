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

import com.epam.reportportal.annotations.ParameterKey;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class PageVisibleLimitedTest extends BaseTest {

  private static final String ERROR_MESSAGE =
      "Страница '%d' должна быть видима во время проведения эксперимента '%s'";

  @Test(description = "Тест активации эксперимента с негативными условиями:"
      + "\n\t1. Страница невидима или частично видима", dataProvider = "dataProvider")
  public void pageVisibleLimitedExperimentUpdateNegativeTest(
      @ParameterKey("Дата начала") final String start,
      @ParameterKey("Дата окончания") final String end,
      @ParameterKey("Видимость") final Boolean isEnabled) {
    final var experiment_start = getValidEndDatePlus10Seconds();
    final var experiment_end = getValidEndDate();
    final var page_id = PAGES_STEPS.createPage(
        start,
        end,
        isEnabled,
        getContentManager());
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
        experiment_end,
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
        .setActivationDate(null)
        .setStatus(DISABLED)
        .setCreationDate(experiment_start)
        .build();
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(
        actualExperiment,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(String.format(ERROR_MESSAGE, page_id, actualExperiment.getUuid()));
    EXPERIMENT_STEPS.getExistingExperiment(
        actualExperiment,
        getContentManager())
        .checkUpdatedExperiment(expectedExperiment);
  }

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public Object[][] dataProvider() {
    return new Object[][]{
        {
            getCurrentDateTime().plus(6, ChronoUnit.HOURS).toString(),
            getCurrentDateTime().plus(12, ChronoUnit.HOURS).toString(),
            true
        },
        {
            getCurrentDateTime().plus(6, ChronoUnit.HOURS).toString(),
            null,
            true
        },
        {
            null,
            getCurrentDateTime().plus(12, ChronoUnit.HOURS).toString(),
            true
        },
        {
            null,
            null,
            false
        }
    };
  }
}
