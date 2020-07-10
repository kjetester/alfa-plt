package ru.alfabank.platform.experiment.update.activate.positive;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.abtests.Experiment;

public class ExperimentActivationTwoTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(ExperimentActivationTwoTest.class);
  private Experiment actualExperiment;
  private Experiment expectedExperiment;

  /**
   * Before method.
   */
  @BeforeMethod(description = """
      Выполнение предусловий:
      \t1. На странице нет запущенных экспериментов
      \t2. Даты активности страницы НЕ установлены
      \t3. Даты активности виджетов НЕ установлены
      \t4. Статус эксперимента 'DISABLED'
      \t5. Дата окончания эксперимента более текущей даты + 1 день
      \t6. У эксперимента есть 3 варианта:
      \t\t1. Дефолтный вариант привязан к нешаренному виджету, у которого:
      \t\t\t* enable=true
      \t\t\t* experimentOptionName=default
      \t\t\t* defaultWidget=true
      \t\t2. Недефолтный вариант привязан к нешаренному виджету, у которого:
      \t\t\t* enable=false
      \t\t\t* experimentOptionName=forABtest
      \t\t\t* defaultWidget=false
      \t\t3. Недефолтный вариант не привязан виджету""")
  public void beforeMethod() {
    LOGGER.error("ERROR");
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id), null, desktop, true, DEFAULT,
            true, List.of(RU), null, null, getContentManager());
    final var abTest_widget =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id), null, desktop, false, FOR_AB_TEST,
            false, List.of(RU), null, null, getContentManager());
    actualExperiment =
        EXPERIMENT_STEPS.createExperiment(default_widget.getDevice(), page_id,
            null, getValidExperimentEndDate(), .5D, getContentManager());
    OPTION_STEPS.createOption(true, List.of(default_widget.getUid()), actualExperiment.getUuid(),
        .33D, getContentManager());
    OPTION_STEPS.createOption(false, List.of(abTest_widget.getUid()), actualExperiment.getUuid(),
        .33D, getContentManager());
    OPTION_STEPS.createOption(false, null, actualExperiment.getUuid(), .34D, getContentManager());
    expectedExperiment = new Experiment.Builder()
        .using(actualExperiment)
        .setEnabled(true)
        .setCreatedBy(getContentManager().getLogin())
        .setActivatedBy(getContentManager().getLogin())
        .setActivationDate(getCurrentDateTime().toString())
        .setStatus(RUNNING)
        .setCreationDate(getCurrentDateTime().toString())
        .build();
  }

  @Test()
  public void experimentActivationPositiveTwoTest() {
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(actualExperiment, getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager())
        .equals(expectedExperiment);
  }
}