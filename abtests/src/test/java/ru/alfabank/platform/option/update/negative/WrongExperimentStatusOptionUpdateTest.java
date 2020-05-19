package ru.alfabank.platform.option.update.negative;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.experiment.involvements.negative.InvolvementsTest;
import ru.alfabank.platform.option.OptionBaseTest;

public class WrongExperimentStatusOptionUpdateTest extends OptionBaseTest {

  private Experiment experiment1;
  private static Option createdDefaultOption;
  private static Option createdAbTestOption;

  /**
   * Before Class.
   */
  @BeforeClass
  public void beforeClass() {
    final var experiment_end = getValidEndDatePlusWeek();
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var defaultWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var abTestWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    experiment1 = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_1_id,
        getRandomProductType(),
        experiment_end,
        .5D,
        getContentManager());
    createdDefaultOption = OPTION_STEPS.createOption(
        new Option.Builder()
            .setDefault(true)
            .setName(randomAlphanumeric(10))
            .setDescription(randomAlphanumeric(10))
            .setWidgetUids(List.of(defaultWidget.getUid()))
            .setExperimentUuid(experiment1.getUuid())
            .setTrafficRate(.5D)
            .build(),
        getContentManager());
    createdAbTestOption = OPTION_STEPS.createOption(
        new Option.Builder()
            .setDefault(false)
            .setName(randomAlphanumeric(10))
            .setDescription(randomAlphanumeric(10))
            .setWidgetUids(List.of(abTestWidget.getUid()))
            .setExperimentUuid(experiment1.getUuid())
            .setTrafficRate(.5D)
            .build(),
        getContentManager());
  }

  @Test(description = "Негативный тест апдейта вариантов - статус эксперимента",
      dataProvider = "experimentStatusOptionUpdateTestDataProvider",
      groups = "1")
  public void wrongExperimentStatusOptionUpdateNegativeTest(
      @ParameterKey("Тест-кейс") final String testCase,
      @ParameterKey("Вариант по-умолчанию") final Option defaultOptionChangeSet,
      @ParameterKey("Вариант АБ-теста") final Option abTestOptionChangeSet) {
    final var softly = new SoftAssertions();
    switch (StringUtils.substringBetween(testCase, "'")) {
      case "RUNNING": {
        EXPERIMENT_STEPS.runExperimentAssumingSuccess(experiment1, getContentManager());
        final var defaultOptionModificationResponse = OPTION_STEPS.modifyOptionAssumingFail(
            createdDefaultOption,
            defaultOptionChangeSet,
            getContentManager());
        final var abTestOptionModificationResponse = OPTION_STEPS.modifyOptionAssumingFail(
            createdAbTestOption,
            abTestOptionChangeSet,
            getContentManager());
        softly.assertThat(defaultOptionModificationResponse.asString())
            .contains("Невозможно изменить вариант '" + createdDefaultOption.getUuid());
        softly.assertThat(abTestOptionModificationResponse.asString())
            .contains("Невозможно изменить вариант '" + createdAbTestOption.getUuid());
        break;
      }
      case "CANCELLED": {
        EXPERIMENT_STEPS.stopExperimentAssumingSuccess(experiment1, getContentManager());
        final var defaultOptionModificationResponse = OPTION_STEPS.modifyOptionAssumingFail(
            createdDefaultOption,
            defaultOptionChangeSet,
            getContentManager());
        final var abTestOptionModificationResponse = OPTION_STEPS.modifyOptionAssumingFail(
            createdAbTestOption,
            abTestOptionChangeSet,
            getContentManager());
        softly.assertThat(defaultOptionModificationResponse.asString())
            .contains("Невозможно изменить вариант '" + createdDefaultOption.getUuid());
        softly.assertThat(abTestOptionModificationResponse.asString())
            .contains("Невозможно изменить вариант '" + createdAbTestOption.getUuid());
        break;
      }
      case "EXPIRED": {
        LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
        throw new SkipException("Manual Testing Needed");
      }
      default: {
        throw new IllegalArgumentException("Неучтенный тест-кейс");
      }
    }
    OPTION_STEPS.getOption(createdDefaultOption, getContentManager())
        .equals(new Option.Builder().using(createdDefaultOption).build());
    OPTION_STEPS.getOption(createdAbTestOption, getContentManager())
        .equals(new Option.Builder().using(createdAbTestOption).build());
    softly.assertAll();
  }

  /**
   * Data Provider.
   *
   * @return Data
   */
  @DataProvider
  public Object[][] experimentStatusOptionUpdateTestDataProvider() {
    return new Object[][]{
        {
            "1. Изменение варианта для эксперимента со статусом 'RUNNING'",
            new Option.Builder().setName(randomAlphanumeric(15)).build(),
            new Option.Builder().setName(randomAlphanumeric(15)).build()
        },
        {
            "2. Изменение варианта для эксперимента со статусом 'CANCELLED'",
            new Option.Builder().setName(randomAlphanumeric(15)).build(),
            new Option.Builder().setName(randomAlphanumeric(15)).build()
        },
        {
            "3. Изменение варианта для эксперимента со статусом 'EXPIRED'",
            null,
            null
        }
    };
  }
}
