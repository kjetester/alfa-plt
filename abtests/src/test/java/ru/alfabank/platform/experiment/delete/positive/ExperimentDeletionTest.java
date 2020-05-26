package ru.alfabank.platform.experiment.delete.positive;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;

public class ExperimentDeletionTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(ExperimentDeletionTest.class);
  private Experiment experiment;
  private Option defaultOption;
  private Option abTestOption;

  /**
   * Before method.
   */
  @BeforeMethod(description = "Выполнение предусловий:\n"
      + "\t1. Создание страницы"
      + "\t2. Создание корневого виджета по-умолчанию"
      + "\t3. Создание корневого виджета для АБ-теста"
      + "\t4. Создание эксперимента"
      + "\t5. Создание варианта по-умолчанию"
      + "\t6. Создание варианта АБ-теста",
      onlyForGroups = "disabledExperimentDeletionPositiveTest")
  public void beforeDisabledExperimentDeletionPositiveTestMethod() {
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
    defaultOption = OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    abTestOption = OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
  }

  @Test(description = "Позитивный тест удаления эксперимента со статусом 'DISABLED'",
      groups = "disabledExperimentDeletionPositiveTest")
  public void disabledExperimentDeletionPositiveTest() {
    assertThat(EXPERIMENT_STEPS.deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NO_CONTENT);
    final var softly = new SoftAssertions();
    softly.assertThat(
        EXPERIMENT_STEPS.getAbsentExperimentByUuid(
            experiment.getUuid(), getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(defaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(abTestOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertAll();
  }

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
      + "\t7. Запуск эксперимента"
      + "\t8. Остановка эксперимента",
      onlyForGroups = "cancelledExperimentDeletionPositiveTest")
  public void beforeCancelledExperimentDeletionPositiveTestMethod() {
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
    defaultOption = OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    abTestOption = OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(experiment, getContentManager());
    EXPERIMENT_STEPS.stopExperimentAssumingSuccess(experiment, getContentManager());
  }

  @Test(description = "Позитивный тест удаления эксперимента со статусом 'CANCELLED'",
      groups = "cancelledExperimentDeletionPositiveTest")
  public void cancelledExperimentDeletionPositiveTest() {
    assertThat(
        EXPERIMENT_STEPS.deleteExperiment(experiment, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NO_CONTENT);
    final var softly = new SoftAssertions();
    softly.assertThat(
        EXPERIMENT_STEPS.getAbsentExperimentByUuid(
            experiment.getUuid(), getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(defaultOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertThat(
        OPTION_STEPS.getAbsentOption(abTestOption, getContentManager()).getStatusCode())
        .as("Проверка статус-кода").isEqualTo(SC_NOT_FOUND);
    softly.assertAll();
  }

  @Test(description = "Тест деактивации эксперимента с негативным условием:\n"
                    + "\t1. Эксперимент имеет статус 'EXPIRED'")
  public void expiredExperimentDeletionNegativeTest() {
    LOGGER.warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
