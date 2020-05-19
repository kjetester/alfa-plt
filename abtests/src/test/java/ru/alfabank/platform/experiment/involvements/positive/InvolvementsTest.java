package ru.alfabank.platform.experiment.involvements.positive;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.experiment.involvements.InvolvementsBaseTest;

public class InvolvementsTest extends InvolvementsBaseTest {

  private int pageId;
  private Widget defaultWidget;
  private Experiment runningExperiment;

  /**
   * Before method.
   */
  @BeforeMethod
  public void beforeMethod() {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDatePlusWeek();
    pageId = PAGES_STEPS.createPage(start, end, true, getContentManager());
    defaultWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        start,
        end,
        getContentManager());
    final var abTest_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        start,
        end,
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        defaultWidget.getDevice(),
        pageId,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(defaultWidget.getUid()),
        experiment.getUuid(),
        .33D,
        getContentManager());
    option2 = OPTION_STEPS.createOption(
        false,
        null,
        experiment.getUuid(),
        .33D,
        getContentManager());
    option3 = OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget.getUid()),
        experiment.getUuid(),
        .34D,
        getContentManager());
    runningExperiment = EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        experiment,
        getContentManager());
  }

  @Test(description = "Тест получения признака участия в эксперименте\n"
      + "\t Статус эксперимента 'RUNNING'",
      dataProvider = "dataProvider")
  public void involvementsRunningExperimentPositiveTest(
      @ParameterKey("Устройство пользователя") final Device clientDevice,
      @ParameterKey("Гео-метка пользователя") final List<String> geos) {
    final var response = EXPERIMENT_STEPS.getInvolvements(
        pageId,
        clientDevice,
        geos,
        getContentManager());
    final var softly = new SoftAssertions();
    if (clientDevice.equals(defaultWidget.getDevice())) {
      assertThat(response.jsonPath().getBoolean("involved"))
          .as("Приверка наличия признака вовлеченность")
          .isNotNull();
      if (response.jsonPath().getBoolean("involved")) {
        final var optionName = response.jsonPath().getString("optionName");
        softly.assertThat(optionName)
            .as("Проверка наименования варианта АБ-теста")
            .isIn(option2.getName(), option3.getName());
        if (optionName.equals(option2.getName())) {
          option2counter++;
        } else {
          option3counter++;
        }
      } else {
        softly.assertThat(response.jsonPath().getString("optionName"))
            .as("Проверка наименования дефолтного варианта")
            .isEqualTo(defaultWidget.getName());
      }
      softly.assertThat(response.jsonPath().getString("uuid"))
          .as("Проверка UUID эксперимента")
          .isEqualTo(runningExperiment.getUuid());
      softly.assertThat(response.jsonPath().getString("cookieValue"))
          .as("Проверка cookieValue эксперимента")
          .isEqualTo(runningExperiment.getCookieValue());
      softly.assertThat(response.jsonPath().getString("endDate"))
          .as("Проверка даты завершения эксперимента")
          .isEqualTo(runningExperiment.getEndDate());
    } else {
      assertThat(response.getBody().asString())
          .as("Проверка отсутствия переданных данных какого-либо эксперимента")
          .isNullOrEmpty();
    }
    softly.assertAll();
  }
}
