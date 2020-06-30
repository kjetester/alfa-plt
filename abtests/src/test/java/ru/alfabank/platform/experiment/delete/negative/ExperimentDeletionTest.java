package ru.alfabank.platform.experiment.delete.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.abtests.Experiment;

public class ExperimentDeletionTest extends BaseTest {

  private Experiment experiment;
  private Experiment expectedExperiment;

  /**
   * Before method.
   */
  @BeforeMethod(description = "Выполнение предусловий:\n"
      + "\t1. Создание страницы"
      + "\t2. Создание корневого виджета по-умолчанию"
      + "\t3. Создание корневого виджета для АБ-теста"
      + "\t4. Создание эксперимента"
      + "\t5. Создание варианта по-умолчанию"
      + "\t6. Создание варианта АБ-теста"
      + "\t7. Запуск эксперимента")
  public void beforeMethod() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
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
    experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        getRandomProductType(),
        getValidExperimentEndDatePlusWeek(),
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
    expectedExperiment =
        EXPERIMENT_STEPS.runExperimentAssumingSuccess(experiment, getContentManager());
  }

  @Test(description = "Негативный тест удаления эксперимента со статусом 'RUNNING'")
  public void runningExperimentDeletionNegativeTest() {
    assertThat(EXPERIMENT_STEPS.deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager())
        .equals(expectedExperiment);
  }
}
