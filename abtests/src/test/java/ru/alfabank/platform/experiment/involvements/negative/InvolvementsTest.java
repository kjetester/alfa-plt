package ru.alfabank.platform.experiment.involvements.negative;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.apache.log4j.LogManager;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.experiment.involvements.InvolvementsBaseTest;

public class InvolvementsTest extends InvolvementsBaseTest {

  private int pageId;

  /**
   * Before method.
   */
  @BeforeMethod(onlyForGroups = "involvementsDisabledExperimentTest")
  public void beforeMethodInvolvementsDisabledExperimentTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDatePlusWeek();
    pageId = PAGES_STEPS.createPage(
        start,
        end,
        true,
        getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        start,
        end,
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        widget_1.getDevice(),
        pageId,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_1.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        null,
        experiment.getUuid(),
        .5D,
        getContentManager());
  }

  @Test(description = "Тест получения признака участия в эксперименте\n"
      + "\t Статус эксперимента 'DISABLED'",
      dataProvider = "dataProvider",
      groups = "involvementsDisabledExperimentTest")
  public void involvementsDisabledExperimentNegativeTest(
      @ParameterKey("Устройство пользователя") final Device clientDevice,
      @ParameterKey("Гео-метка пользователя") final List<String> geos) {
    assertThat(EXPERIMENT_STEPS.getInvolvements(
        pageId,
        clientDevice,
        geos,
        getContentManager())
        .getBody().asString())
        .as("Проверка отсутствия переданных данных какого-либо эксперимента")
        .isNullOrEmpty();
  }

  /**
   * Before method.
   */
  @BeforeMethod(onlyForGroups = "involvementsCancelledExperimentTest")
  public void beforeMethodInvolvementsCancelledExperimentTest() {
    final var start = getValidEndDatePlus10Seconds();
    final var end = getValidEndDatePlusWeek();
    final var pageId = PAGES_STEPS.createPage(
        start,
        end,
        true,
        getContentManager());
    final var widget_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        start,
        end,
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        widget_1.getDevice(),
        pageId,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget_1.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        null,
        experiment.getUuid(),
        .5D,
        getContentManager());
    final var runningExperiment = EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        experiment,
        getContentManager());
    EXPERIMENT_STEPS.stopExperimentAssumingSuccess(
        runningExperiment,
        getContentManager());
  }

  @Test(description = "Тест получения признака участия в эксперименте\n"
      + "\t Статус эксперимента 'CANCELLED'",
      dataProvider = "dataProvider",
      groups = "involvementsCancelledExperimentTest")
  public void involvementsCancelledExperimentNegativeTest(
      @ParameterKey("Устройство пользователя") final Device clientDevice,
      @ParameterKey("Гео-метка пользователя") final List<String> geos) {
    assertThat(EXPERIMENT_STEPS.getInvolvements(
        pageId,
        clientDevice,
        geos,
        getContentManager())
        .getBody().asString())
        .as("Проверка отсутствия переданных данных какого-либо эксперимента")
        .isNullOrEmpty();
  }

  @Test(description = "Тест получения признака участия в эксперименте\n"
      + "\t Статус эксперимента 'EXPIRED'")
  public void involvementsExpiredExperimentNegativeTest() {
    LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
