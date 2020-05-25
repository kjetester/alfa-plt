package ru.alfabank.platform.option.delete.negative;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionDeleteTest extends OptionBaseTest {

  private static final Logger LOGGER = LogManager.getLogger(OptionDeleteTest.class);

  private Widget defaultWidget;
  private Widget abTestWidget;
  private Experiment experiment;
  private Option createdDefaultOption;
  private Option createdAbTestOption;

  /**
   * Before Class.
   */
  @BeforeClass
  public void beforeClass() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    defaultWidget = DRAFT_STEPS.createWidget(
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
    abTestWidget = DRAFT_STEPS.createWidget(
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
        defaultWidget.getDevice(),
        page_id,
        getRandomProductType(),
        getValidExperimentEndDatePlusWeek(),
        .5D,
        getContentManager());
  }

  /**
   * Data Provider.
   *
   * @return Data
   */
  @DataProvider
  public Object[][] optionDeleteTestDataProvider() {
    return new Object[][]{
        {
            "1. Удаление варианта, ассоциированным с экспериментом со статусом 'RUNNING'",
            new Option.Builder()
                .setDefault(true)
                .setName(randomAlphanumeric(11))
                .setDescription(randomAlphanumeric(11))
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build(),
            new Option.Builder()
                .setDefault(false)
                .setName(randomAlphanumeric(11))
                .setDescription(randomAlphanumeric(11))
                .setWidgetUids(List.of(abTestWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build()
        },
        {
            "2. Удаление варианта, ассоциированным с экспериментом со статусом 'CANCELLED'",
            new Option.Builder()
                .setDefault(true)
                .setName(randomAlphanumeric(11))
                .setDescription(randomAlphanumeric(11))
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build(),
            new Option.Builder()
                .setDefault(false)
                .setName(randomAlphanumeric(11))
                .setDescription(randomAlphanumeric(11))
                .setWidgetUids(List.of(abTestWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build()
        },
        {
            "3. Удаление варианта, ассоциированным с экспериментом со статусом 'EXPIRED'",
            new Option.Builder()
                .setDefault(true)
                .setName(randomAlphanumeric(11))
                .setDescription(randomAlphanumeric(11))
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build(),
            new Option.Builder()
                .setDefault(false)
                .setName(randomAlphanumeric(11))
                .setDescription(randomAlphanumeric(11))
                .setWidgetUids(List.of(abTestWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build()
        }
    };
  }

  @Test(description = "Позитивный тест удаления вариантов",
      dataProvider = "optionDeleteTestDataProvider")
  public void optionDeleteNegativeTest(
      @ParameterKey("Тест-кейс") final String testCase,
      @ParameterKey("Вариант по-умолчанию") final Option defaultOption,
      @ParameterKey("Вариант для АБ-тестирования") final Option abTestOption) {
    Response response1;
    Response response2;
    switch (Integer.parseInt(testCase.replaceAll("[\\D]", ""))) {
      case 1: {
        createdDefaultOption = OPTION_STEPS.createOption(
            defaultOption,
            getContentManager());
        createdAbTestOption = OPTION_STEPS.createOption(
            abTestOption,
            getContentManager());
        EXPERIMENT_STEPS.runExperimentAssumingSuccess(
            experiment,
            getContentManager());
        response1 = OPTION_STEPS.deleteOptionAssumingFail(
            createdDefaultOption,
            getContentManager());
        response2 = OPTION_STEPS.deleteOptionAssumingFail(
            createdAbTestOption,
            getContentManager());
        break;
      }
      case 2: {
        EXPERIMENT_STEPS.stopExperimentAssumingSuccess(
            experiment,
            getContentManager());
        response1 = OPTION_STEPS.deleteOptionAssumingFail(
            createdDefaultOption,
            getContentManager());
        response2 = OPTION_STEPS.deleteOptionAssumingFail(
            createdAbTestOption,
            getContentManager());
        break;
      }
      case 3: {
        LOGGER.warn("Manual Testing Needed");
        throw new SkipException("Manual Testing Needed");
      }
      default: {
        throw new IllegalArgumentException("Неучтенный тест-кейс");
      }
    }
    final var softly = new SoftAssertions();
    softly.assertThat(response1.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    softly.assertThat(response1.asString())
        .as("Проверка невозможности удаления варианта")
        .contains("Невозможно удалить вариант '" + createdDefaultOption.getUuid());
    softly.assertThat(response2.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    softly.assertThat(response2.asString())
        .as("Невозможно удалить вариант")
        .contains("Невозможно удалить вариант '" + createdAbTestOption.getUuid());
    softly.assertThat(OPTION_STEPS.getOption(
        createdDefaultOption,
        getContentManager()))
        .as("Проверка существования варианта")
        .isNotNull();
    softly.assertThat(OPTION_STEPS.getOption(
        createdAbTestOption,
        getContentManager()))
        .as("Проверка существования варианта")
        .isNotNull();
    softly.assertAll();
  }
}
