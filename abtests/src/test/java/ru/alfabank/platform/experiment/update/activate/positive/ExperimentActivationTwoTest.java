package ru.alfabank.platform.experiment.update.activate.positive;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class ExperimentActivationTwoTest extends BaseTest {

  @Test(description = "Тест запуска эксперимента с условиями:"
      + "\n\t1. На странице нет запущенных экспериментов"
      + "\n\t2. Даты активности страницы НЕ установлены"
      + "\n\t3. Даты активности виджетов НЕ установлены"
      + "\n\t4. Статус эксперимента 'DISABLED'"
      + "\n\t5. Дата окончания эксперимента более текущей даты + 1 день"
      + "\n\t6. У эксперимента есть 3 варианта:"
      + "\n\t\t1. Дефолтный вариант привязан к нешаренному виджету, у которого:"
      + "\n\t\t\t* enable=true"
      + "\n\t\t\t* experimentOptionName=default"
      + "\n\t\t\t* defaultWidget=true"
      + "\n\t\t2. Недефолтный вариант привязан к нешаренному виджету, у которого:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false"
      + "\n\t\t3. Недефолтный вариант не привязан виджету")
  public void experimentActivationPositiveTwoTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDatePlus10Minutes();
    final var experiment_end = getValidEndDate();
    final var page_id = PAGES_STEPS.createPage(start, end, true, getContentManager());
    final var default_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        start,
        end,
        getContentManager());
    final var abTest_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        start,
        end,
        getContentManager());
    final var actual_experiment = EXPERIMENT_STEPS.createExperiment(
        default_widget.getDevice(),
        page_id,
        getRandomProductType(),
        experiment_end,
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget.getUid()),
        actual_experiment.getUuid(),
        .33D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        actual_experiment.getUuid(),
        .33D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        null,
        actual_experiment.getUuid(),
        .34D,
        getContentManager());
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actual_experiment.getUuid())
        .setCookieValue(actual_experiment.getCookieValue())
        .setDescription(actual_experiment.getDescription())
        .setPageId(actual_experiment.getPageId())
        .setProductTypeKey(actual_experiment.getProductTypeKey())
        .setEndDate(actual_experiment.getEndDate())
        .setTrafficRate(actual_experiment.getTrafficRate())
        .setDevice(actual_experiment.getDevice())
        .setEnabled(true)
        .setCreatedBy(getContentManager().getLogin())
        .setActivatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(RUNNING)
        .setCreationDate(start)
        .build();
    // TEST //
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(actual_experiment, getContentManager())
        .checkActivatedExperiment(expectedExperiment);
  }
}