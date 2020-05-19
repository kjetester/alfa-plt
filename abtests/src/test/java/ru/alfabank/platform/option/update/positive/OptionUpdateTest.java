package ru.alfabank.platform.option.update.positive;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionUpdateTest extends OptionBaseTest {

  private Widget defaultWidget1;
  private Widget defaultWidget2;
  private Widget abTestWidget1;
  private Widget abTestWidget2;
  private Experiment experiment;
  private Option createdOption;
  private static final String SHORT_NAME = randomAlphanumeric(1);
  private static final String LONG_NAME = randomAlphanumeric(32);
  private static final String SHORT_DESCRIPTION = randomAlphanumeric(1);
  private static final String LONG_DESCRIPTION = randomAlphanumeric(1000);

  /**
   * Before Class.
   */
  @BeforeClass
  public void beforeClass() {
    final var experimentEnd = getValidEndDatePlusWeek();
    final var page_id = PAGES_STEPS.createPage(null, null, true, getContentManager());
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
    final var widget_1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_1,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    defaultWidget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_1_1,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        defaultWidget1,
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
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget_2_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    defaultWidget2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_2_1,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        defaultWidget2,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget_3 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    abTestWidget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_3,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        abTestWidget1,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget_4 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    abTestWidget2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget_4,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        abTestWidget2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    experiment = EXPERIMENT_STEPS.createExperiment(
        widget_1.getDevice(),
        page_id,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());

  }

  @Test(description = "Позитивный тест апдейта вариантов",
      dataProvider = "optionUpdateTestDataProvider")
  public void optionUpdatePositiveTest(
      @ParameterKey("Тест-кейс") final String testCase,
      @ParameterKey("Существующий вариант") final Option existing,
      @ParameterKey("Изменить на") final Option modification) {
    createdOption = OPTION_STEPS.createOption(existing, getContentManager());
    final var modifiedOption = OPTION_STEPS.modifyOption(
        createdOption,
        modification,
        getContentManager());
    if (testCase.contains("Изменение ассоцииации варианта с экспериментом ")) {
      OPTION_STEPS.getOption(modifiedOption, getContentManager())
          .equals(new Option.Builder()
              .using(modification)
              .setUuid(modifiedOption.getUuid())
              .setExperimentUuid(modification.getExperimentUuid())
              .build());
    } else {
      OPTION_STEPS.getOption(modifiedOption, getContentManager())
          .equals(new Option.Builder()
              .using(modification)
              .setUuid(modifiedOption.getUuid())
              .build());
    }
  }

  /**
   * Data Provider.
   *
   * @return Data
   */
  @DataProvider
  public Object[][] optionUpdateTestDataProvider() {
    return new
        Object[][]{
        {

            "Изменение наименование варианта с min до max",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D).build(),
            new Option.Builder()
                .setDefault(true)
                .setName(LONG_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение наименование варианта с max до min",
            new Option.Builder()
                .setDefault(true)
                .setName(LONG_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D).build(),
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение описания варианта с min до max",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(LONG_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение описания варианта с max до min",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(LONG_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение флага вариатна по умолчанию с 'true' на 'false'",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение флага вариатна по умолчанию с 'false' на 'true'",
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(emptyList()).setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),

            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(emptyList()).setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение флага вариатна по умолчанию с 'false' на 'true' и ассоциации с виджетом",
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение флага вариатна по умолчанию с 'true' на 'false' и ассоциации с виджетом",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Ассоциация варианта по-умолчанию с виджетом",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(emptyList()).setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),

            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение ассоциации варианта по-умолчанию с виджетом",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget2.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Удаление ассоциации варианта по-умолчанию с виджетом",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Ассоциация варианта для АБ-тестов с виджетом",
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение ассоциации варианта для АБ-тестов с виджетом",
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget2.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Удаление ассоциации варианта для АБ-тестов с виджетом",
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "Изменение доли траффика варианта с min до max",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.99D)
                .build()
        },
        {
            "Изменение доли траффика варианта с max до min",
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.99D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(SHORT_NAME)
                .setDescription(SHORT_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.01D)
                .build()
        }
    };
  }

  /**
   * After Method.
   */
  @AfterMethod
  public void afterMethod() {
    if (createdOption != null) {
      OPTION_STEPS.deleteOption(createdOption, getContentManager());
      createdOption = null;
    }
  }
}
