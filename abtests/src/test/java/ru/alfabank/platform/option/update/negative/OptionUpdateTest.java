package ru.alfabank.platform.option.update.negative;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.apache.log4j.LogManager;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.enums.User;
import ru.alfabank.platform.experiment.involvements.negative.InvolvementsTest;
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
    setUser(User.CONTENT_MANAGER);
    final var experimentEnd = getCurrentDateTime().plusDays(5).toString();
    var page1 = createPage(null, null, true);
    final var page2 = createPage(null, null, true);
    final var pageId1 = page1.getId();
    final var pageId2 = page2.getId();
    defaultWidget = createWidget(
        page1,
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null);
    page1 = createdPages.get(pageId1);
    defaultWidget1 = createWidget(
        page1,
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null);
    widget1 = createWidget(page1,
        defaultWidget1,
        desktop,
        false,
        DEFAULT,
        true,
        null,
        null);
    page1 = createdPages.get(pageId1);
    defaultWidget2 = createWidget(
        page1,
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null);
    widget2 = createWidget(
        page1,
        defaultWidget2,
        desktop,
 false,
        FOR_AB_TEST,
        false,
        null,
        null);
    page1 = createdPages.get(pageId1);
    abTestWidget = createWidget(
        page1,
        null,
        desktop,
  false,
        FOR_AB_TEST,
        false,
        null,
        null);
    abTestWidget1 = createWidget(
        page1,
        null,
        desktop,
  false,
        FOR_AB_TEST,
        false,
        null,
        null);
    widget4 = createWidget(
        page1,
        abTestWidget1,
        desktop,
        false,
        DEFAULT,
        true,
        null,
        null);
    page1 = createdPages.get(pageId1);
    abTestWidget2 = createWidget(
        page1,
        null,
        desktop,
    false,
        FOR_AB_TEST,
        false,
        null,
        null);
    widget5 = createWidget(
        page1,
        abTestWidget2,
        desktop,
    true,
        DEFAULT,
        true,
        null,
        null);
    page1 = createdPages.get(pageId1);
    final var widget6 = createWidget(
        page1,
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null);
    abTestWidget3 = createWidget(
        page1,
        widget6,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null);
    experiment1 = createExperiment(
        defaultWidget.getDevice(),
        pageId1,
        getRandomProductType(),
        experimentEnd,
        .5D);
    experiment2 = createExperiment(
        defaultWidget.getDevice(),
        pageId2,
        getRandomProductType(),
        experimentEnd,
        .5D);
  }

  /**
   * Data Provider.
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

  @Test(description = "Негативный тест апдейта вариантов",
      dataProvider = "optionUpdateTestDataProvider",
      groups = "1")
  public void optionUpdateTest(@ParameterKey("Тест-кейс") final String testCase,
                               @ParameterKey("Существующий вариант") final Option existingOption,
                               @ParameterKey("Изменить на") final Option optionModification) {
    createdOption = createOption(existingOption);
    final var response = modifyOptionAssumingFail(createdOption, optionModification);
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
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Длина описания варианта должна быть в интервале от 1 до 1000 символов");
        break;
      }
      case 5: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Доля траффика должна быть меньше единицы");
        break;
      }
      case 6: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
        softly.assertThat(response.getBody().asString())
            .as(MESSAGE_CHECK)
            .contains("Доля траффика должна быть больше нуля");
        break;
      }
      case 7: {
        softly.assertThat(response.getStatusCode())
            .as(STATUS_CODE_CHECK)
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
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
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
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
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
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
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
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
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
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
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
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
            .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
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
    getOption(createdOption)
        .equals(new Option.Builder()
            .using(createdOption)
            .build());
    softly.assertAll();
  }

  /**
   * Data Provider.
   * @return Data
   */
  @DataProvider
  public Object[][] experimentStatusOptionUpdateTestDataProvider() {
    return new Object[][]{
        {
            "1. Изменение варианта для эксперимента со статусом RUNNING",
            new Option.Builder()
            .setDefault(false)
            .setName(NAME + "1")
            .setDescription(DESCRIPTION + "1")
            .setWidgetUids(List.of(abTestWidget.getUid()))
            .setExperimentUuid(experiment1.getUuid())
            .setTrafficRate(.5D)
            .build(),
            new Option.Builder()
            .setDefault(true)
            .setName(NAME + "2")
            .setDescription(DESCRIPTION + "2")
            .setWidgetUids(List.of(defaultWidget.getUid()))
            .setExperimentUuid(experiment1.getUuid())
            .setTrafficRate(.5D)
            .build()
        },
        {
            "2. Изменение варианта для эксперимента со статусом CANCELLED",
            new Option.Builder()
                .setDefault(false)
                .setName(NAME + "1")
                .setDescription(DESCRIPTION + "1")
                .setWidgetUids(List.of(abTestWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.5D)
                .build(),
            new Option.Builder()
                .setDefault(true)
                .setName(NAME + "2")
                .setDescription(DESCRIPTION + "2")
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment1.getUuid())
                .setTrafficRate(.5D)
                .build()
        },
        {
            "3. Изменение варианта для эксперимента со статусом EXPIRED",
            null,
            null
        }
    };
  }

  @Test(description = "Негативный тест апдейта вариантов - статус эксперимента",
      dataProvider = "experimentStatusOptionUpdateTestDataProvider",
      groups = "1")
  public void wrongExperimentStatusOptionUpdateTest(
      @ParameterKey("Тест-кейс") final String testCase,
      @ParameterKey("Вариант АБ-теста") final Option abTestOption,
      @ParameterKey("Вариант по-умолчанию") final Option defaultOption) {
    Option createdAbTestOption = createOption(abTestOption);
    Option abTestOptionModification = new Option.Builder()
        .setDefault(false)
        .setName(NAME + "1")
        .setDescription(DESCRIPTION)
        .setWidgetUids(List.of(abTestWidget.getUid()))
        .setExperimentUuid(experiment1.getUuid())
        .setTrafficRate(.5D)
        .build();
    Option createdDefaultOption = createOption(defaultOption);
    Option defaultOptionModification = new Option.Builder()
        .setDefault(false)
        .setName(NAME + "2")
        .setDescription(DESCRIPTION)
        .setWidgetUids(List.of(abTestWidget.getUid()))
        .setExperimentUuid(experiment1.getUuid())
        .setTrafficRate(.5D)
        .build();
    final var softly = new SoftAssertions();
    switch (Integer.parseInt(testCase.replaceAll("[\\D]", ""))) {
      case 1: {
        runExperimentAssumingSuccess(experiment1);
        final var response1 =
            modifyOptionAssumingFail(createdAbTestOption, abTestOptionModification);
        final var response2 =
            modifyOptionAssumingFail(createdDefaultOption, defaultOptionModification);
        softly.assertThat(response1.asString())
            .contains("Невозможно изменить вариант '" + createdAbTestOption.getUuid());
        softly.assertThat(response2.asString())
            .contains("Невозможно изменить вариант '" + createdDefaultOption.getUuid());
        break;
      }
      case 2: {
        stopExperimentAssumingSuccess(experiment1);
        final var response1 =
            modifyOptionAssumingFail(createdAbTestOption, abTestOptionModification);
        final var response2 =
            modifyOptionAssumingFail(createdDefaultOption, defaultOptionModification);
        softly.assertThat(response1.asString())
            .contains("Невозможно изменить вариант '" + createdAbTestOption.getUuid());
        softly.assertThat(response2.asString())
            .contains("Невозможно изменить вариант '" + createdDefaultOption.getUuid());
        break;
      }
      case 3: {
        LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
        throw new SkipException("Manual Testing Needed");
      }
      default: {
        throw new IllegalArgumentException("Неучтенный тест-кейс");
      }
    }
    getOption(createdAbTestOption)
        .equals(new Option.Builder()
            .using(createdAbTestOption)
            .build());
    getOption(createdDefaultOption)
        .equals(new Option.Builder()
            .using(createdDefaultOption)
            .build());
    softly.assertAll();
  }

  /**
   * After method.
   */
  @AfterMethod(onlyForGroups = "1")
  public void afterMethod() {
    if (createdOption != null) {
      deleteOption(createdOption);
      createdOption = null;
    }
  }
}
