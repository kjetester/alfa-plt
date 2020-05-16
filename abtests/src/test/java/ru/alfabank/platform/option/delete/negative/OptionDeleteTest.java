package ru.alfabank.platform.option.delete.negative;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.apache.log4j.LogManager;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.experiment.involvements.negative.InvolvementsTest;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionDeleteTest extends OptionBaseTest {

  private String experimentEnd = getCurrentDateTime().plusHours(26).toString();
  private Widget defaultWidget;
  private Widget abTestWidget;
  private Experiment experiment;
  private Option createdDefaultOption;
  private Option createdAbTestOption;
  private static final String NAME = randomAlphanumeric(16);
  private static final String DESCRIPTION = randomAlphanumeric(50);

  /**
   * Before Class.
   */
  @BeforeClass
  public void beforeClass() {
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    defaultWidget = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
    abTestWidget = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    experiment = createExperiment(
        defaultWidget.getDevice(), pageId, getRandomProductType(), experimentEnd, .5D, getContentManager());
  }

  /**
   * Data Provider.
   * @return Data
   */
  @DataProvider
  public Object[][] optionDeleteTestDataProvider() {
    return new Object[][]{
        {
            "1. Удаление варианта, ассоциированным с экспериментом со статусом 'RUNNING'",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME + "1")
                .setDescription(DESCRIPTION + "1")
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build(),
            new Option.Builder()
                .setDefault(false)
                .setName(NAME + "2")
                .setDescription(DESCRIPTION + "2")
                .setWidgetUids(List.of(abTestWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build()
        },
        {
            "2. Удаление варианта, ассоциированным с экспериментом со статусом 'CANCELLED'",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME + "2")
                .setDescription(DESCRIPTION + "2")
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build(),
            new Option.Builder()
                .setDefault(false)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setWidgetUids(List.of(abTestWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build()
        },
        {
            "3. Удаление варианта, ассоциированным с экспериментом со статусом 'EXPIRED'",
            new Option.Builder()
                .setDefault(true)
                .setName(NAME + "3")
                .setDescription(DESCRIPTION + "3")
                .setWidgetUids(List.of(defaultWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build(),
            new Option.Builder()
                .setDefault(false)
                .setName(NAME + "3")
                .setDescription(DESCRIPTION + "3")
                .setWidgetUids(List.of(abTestWidget.getUid()))
                .setExperimentUuid(experiment.getUuid())
                .setTrafficRate(.5D).build()
        }
    };
  }

  @Test(description = "Позитивный тест удаления вариантов",
      dataProvider = "optionDeleteTestDataProvider")
  public void optionDeleteTest(
      @ParameterKey("Тест-кейс") final String testCase,
      @ParameterKey("Вариант по-умолчанию") final Option defaultOption,
      @ParameterKey("Вариант для АБ-тестирования") final Option abTestOption) {
    Response response1;
    Response response2;
    switch (Integer.parseInt(testCase.replaceAll("[\\D]", ""))) {
      case 1: {
        createdDefaultOption = createOption(defaultOption, getContentManager());
        createdAbTestOption = createOption(abTestOption, getContentManager());
        runExperimentAssumingSuccess(experiment, getContentManager());
        response1 = deleteOptionAssumingFail(createdDefaultOption, getContentManager());
        response2 = deleteOptionAssumingFail(createdAbTestOption, getContentManager());
        break;
      }
      case 2: {
        stopExperimentAssumingSuccess(experiment, getContentManager());
        response1 = deleteOptionAssumingFail(createdDefaultOption, getContentManager());
        response2 = deleteOptionAssumingFail(createdAbTestOption, getContentManager());
        break;
      }
      case 3: {
        LogManager.getLogger(InvolvementsTest.class)
            .warn("Manual Testing Needed.");
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
    softly.assertThat(getOption(createdDefaultOption, getContentManager()))
        .as("Проверка существования варианта")
        .isNotNull();
    softly.assertThat(getOption(createdAbTestOption, getContentManager()))
        .as("Проверка существования варианта")
        .isNotNull();
    softly.assertAll();
  }
}
