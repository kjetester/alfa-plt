package ru.alfabank.platform.experiment.update.activate.negative;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class PageVisibleLimitedExperimentActivateNegativeTest extends BaseTest {

  private static final String ERROR_MESSAGE =
      "Страница '%s' должна быть видима во время проведения эксперимента '%s'";

  @Test(description = "Тест активации эксперимента с негативными условиями:"
      + "\n\t1. Страница невидима или частично видима", dataProvider = "dataProvider")
  public void pageVisibleLimitedExperimentActivateNegativeTest(
      @ParameterKey("Дата начала") final String dateFrom,
      @ParameterKey("Дата окончания") final String dateTo,
      @ParameterKey("Видимость") final Boolean isEnabled) {
    final var page_id =
        PAGES_STEPS.createPage(dateFrom, dateTo, isEnabled, null, getContentManager());
    final var default_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var abTest_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        null,
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(experiment, getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString()).as("Проверка сообщения об ошибке")
        .contains(String.format(ERROR_MESSAGE, page_id, experiment.getUuid()));
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
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
            getCurrentDateTime().plus(6, HOURS).toString(),
            getCurrentDateTime().plus(12, HOURS).toString(),
            true
        },
        {
            getCurrentDateTime().plus(6, HOURS).toString(),
            null,
            true
        },
        {
            null,
            getCurrentDateTime().plus(12, HOURS).toString(),
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
