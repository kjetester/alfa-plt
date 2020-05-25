package ru.alfabank.platform.option.update.negative;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionUpdateTest extends OptionBaseTest {

  private Widget defaultWidget;
  private Widget defaultWidget1;
  private Widget widget1;
  private Widget defaultWidget2;
  private Widget widget2;
  private Widget abTestWidget;
  private Widget abTestWidget1;
  private Widget widget4;
  private Widget abTestWidget2;
  private Widget widget5;
  private Widget abTestWidget3;
  private Experiment experiment1;
  private Experiment experiment2;
  private Option createdOption;
  private static final String NAME = randomAlphanumeric(16);
  private static final String MAX_VIOLATION_NAME = randomAlphanumeric(33);
  private static final String DESCRIPTION = randomAlphanumeric(500);
  private static final String MAX_VIOLATION_DESCRIPTION = randomAlphanumeric(1001);
  private static final String STATUS_CODE_CHECK = "Проверка статус-кода";
  private static final String MESSAGE_CHECK = "Проверка сообщения";

  /**
   * Before Class.
   */
  @BeforeClass
  public void beforeClass() {
    final var experiment_end = getValidExperimentEndDatePlusWeek();
    final var page_1_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var page_2_id = PAGES_STEPS.createEnabledPage(getContentManager());
    defaultWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    defaultWidget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    widget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        defaultWidget1,
        desktop,
        false,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    defaultWidget2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    widget2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        defaultWidget2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    abTestWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    abTestWidget1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    widget4 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        abTestWidget1,
        desktop,
        false,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    abTestWidget2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    widget5 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        abTestWidget2,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var widget6 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    abTestWidget3 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_1_id),
        widget6,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    experiment1 = EXPERIMENT_STEPS.createExperiment(
        defaultWidget.getDevice(),
        page_1_id,
        getRandomProductType(),
        experiment_end,
        .5D,
        getContentManager());
    experiment2 = EXPERIMENT_STEPS.createExperiment(
        defaultWidget.getDevice(),
        page_2_id,
        getRandomProductType(),
        experiment_end,
        .5D,
        getContentManager());
  }

  @Test(description = "Негативный тест апдейта вариантов",
      dataProvider = "optionUpdateTestDataProvider",
      groups = "1")
  public void optionUpdateNegativeTest(
      @ParameterKey("Тест-кейс") final String testCase,
      @ParameterKey("Существующий вариант") final Option existingOption,
      @ParameterKey("Изменить на") final Option optionModification) {
    createdOption = OPTION_STEPS.createOption(existingOption, getContentManager());
    final var response = OPTION_STEPS.modifyOptionAssumingFail(
        createdOption,
        optionModification,
        getContentManager());
    final var softly = new SoftAssertions();
    switch (Integer.parseInt(testCase.replaceAll("[\\D]", ""))) {
      case 1:
      case 2: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Длина названия варианта должна быть в интервале от 1 до 32 символов");
        break;
      }
      case 3:
      case 4: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Длина описания варианта должна быть в интервале от 1 до 1000 символов");
        break;
      }
      case 5: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Доля траффика должна быть меньше единицы");
        break;
      }
      case 6: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Доля траффика должна быть больше нуля");
        break;
      }
      case 7: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Для варианта по умолчанию '" + createdOption.getName() + "' виджеты '"
                + optionModification.getWidgetUids()
                + "' должны быть с дефолтным названием варианта, быть включенными и быть виджетами"
                + " по умолчанию");
        break;
      }
      case 8: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Для варианта по умолчанию '" + createdOption.getName() + "' виджеты '["
                + widget1.getUid()
                + "]' должны быть с дефолтным названием варианта, быть включенными и быть виджетами"
                + " по умолчанию");
        break;
      }
      case 9: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Для варианта по умолчанию '" + createdOption.getName() + "' виджеты '["
                + widget2.getUid()
                + "]' должны быть с дефолтным названием варианта, быть включенными и быть виджетами"
                + " по умолчанию");
        break;
      }
      case 10: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Для варианта '" + createdOption.getName() + "' виджеты '"
                + optionModification.getWidgetUids()
                + "' должны быть помечены как 'forABtest', быть выключенными и не должны быть "
                + "виджетами по умолчанию");
        break;
      }
      case 11: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Для варианта '" + createdOption.getName() + "' виджеты '["
                + widget4.getUid()
                + "]' должны быть помечены как 'forABtest', быть выключенными и не должны быть "
                + "виджетами по умолчанию");
        break;
      }
      case 12: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody()
            .asString()).as(MESSAGE_CHECK)
            .contains("Для варианта '" + createdOption.getName() + "' виджеты '["
                + widget5.getUid()
                + "]' должны быть помечены как 'forABtest', быть выключенными и не должны быть "
                + "виджетами по умолчанию");
        break;
      }
      case 13: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
        softly.assertThat(response.getBody()
            .asString()).as(MESSAGE_CHECK)
            .contains(
                optionModification.getWidgetUids().get(0),
                "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
        break;
      }
      case 14: {
        assertThat(response.getStatusCode()).as(STATUS_CODE_CHECK).isEqualTo(SC_OK);
        break;
      }
      default: {
        throw new IllegalArgumentException("Неучтенный тест-кейс");
      }
    }
    OPTION_STEPS.getOption(createdOption, getContentManager())
        .equals(new Option.Builder()
            .using(createdOption)
            .build());
    softly.assertAll();
  }


  /**
   * Data Provider.
   *
   * @return Data
   */
  @DataProvider
  public Object[][] optionUpdateTestDataProvider() {
    return new Object[][]{
        {
            "1. Изменение наименование варианта < MIN",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName("")
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "2. Изменение наименование варианта > MAX",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(MAX_VIOLATION_NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "3. Изменение описания варианта < MIN",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription("")
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "4. Изменение описания варианта > MAX",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(MAX_VIOLATION_DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "5. Изменение доли траффика варианта > max",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(1D)
                .build()
        },
        {
            "6. Изменение доли траффика варианта < min",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.99D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.00D)
                .build()
        },
        {
            "7. Ассоциация варианта по-умолчанию с виджетом для АБ-тестирования",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "8. Ассоциация варианта по-умолчанию с дефолтным виджетом,"
                + " ребёнок которого - дефолтный неактивный виджет",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget1.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "9. Ассоциация варианта по-умолчанию с дефолтным виджетом,"
                + " ребёнок которого - неактивный виджет для АБ-тестов",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget2.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "10. Ассоциация варианта для АБ-тестирования с виджетом по-умолчанию",
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "11. Ассоциация варианта для АБ-тестирования с виджетом для АБ-тестирования,"
                + " ребёнок которого - неактивный дефолтный виджет",
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget1.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "12. Ассоциация варианта для АБ-тестирования с виджетом для АБ-тестирования,"
                + " ребёнок которого - активный дефолтный виджет",
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget2.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "13. Ассоциация варианта для АБ-тестирования с виджетом для АБ-тестирования,"
                + " предок которого - неактивный виджет для АБ-тестов",
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget3.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build()
        },
        {
            "14. Ассоциация варианта для АБ-тестирования с другим экспериментом",
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.01D)
                .build(),
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(emptyList())
                .setExperimentUuid(experiment2.getUuid())
                .setTrafficRate(.01D)
                .build()
        }
    };
  }

  /**
   * After method.
   */
  @AfterMethod(onlyForGroups = "1")
  public void afterMethod() {
    if (createdOption != null) {
      OPTION_STEPS.deleteOption(createdOption, getContentManager());
      createdOption = null;
    }
  }
}
