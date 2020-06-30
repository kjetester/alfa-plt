package ru.alfabank.platform.experiment.involvements.positive;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.abtests.Experiment;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.experiment.involvements.InvolvementsBaseTest;

public class InvolvementsTest extends InvolvementsBaseTest {

  private int pageId;
  private Experiment runningDesktopExperiment;
  private Experiment runningMobileExperiment;

  /**
   * Before method.
   */
  @BeforeMethod(description = "Выполнение предусловий:\n"
      + "\t1.  Создание страницы"
      + "\t2.  Создание корневого виджета по-умолчанию для десктоп версии"
      + "\t3.  Создание корневого виджета для АБ-теста для десктоп версии"
      + "\t4.  Создание корневого виджета по-умолчанию для мобильной версии"
      + "\t5.  Создание корневого виджета для АБ-теста для мобильной версии"
      + "\t6.  Создание эксперимента для десктоп версии"
      + "\t7.  Создание эксперимента для мобильной версии"
      + "\t8.  Создание варианта по-умолчанию для десктоп версии"
      + "\t9.  Создание 2х вариантов АБ-теста для десктоп версии"
      + "\t10. Создание варианта по-умолчанию для мобильной версии"
      + "\t11. Создание 2х вариантов АБ-теста для мобильной версии"
      + "\t12. Запуск эксперимента десктоп версии"
      + "\t13. Запуск эксперимента мобильной версии",
      firstTimeOnly = true)
  public void beforeMethod() {
    final var date_from = getValidWidgetDateFrom();
    final var date_to = getValidExperimentEndDatePlusWeek();
    pageId = PAGES_STEPS.createPage(date_from, date_to, true, getContentManager());
    final var default_desktop_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        date_from,
        date_to,
        getContentManager());
    final var abTest_desktop_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        date_from,
        date_to,
        getContentManager());
    final var defaultMobileWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
        null,
        mobile,
        true,
        DEFAULT,
        true,
        List.of(RU),
        date_from,
        date_to,
        getContentManager());
    final var abTest_mobile_widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
        null,
        mobile,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        date_from,
        date_to,
        getContentManager());
    final var desktop_experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        pageId,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    final var mobile_experiment = EXPERIMENT_STEPS.createExperiment(
        mobile,
        pageId,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    defaultDesktopOption = OPTION_STEPS.createOption(
        true,
        List.of(default_desktop_widget.getUid()),
        desktop_experiment.getUuid(),
        .33D,
        getContentManager());
    abTestDesktopOption1 = OPTION_STEPS.createOption(
        false,
        null,
        desktop_experiment.getUuid(),
        .33D,
        getContentManager());
    abTestDesktopOption2 = OPTION_STEPS.createOption(
        false,
        List.of(abTest_desktop_widget.getUid()),
        desktop_experiment.getUuid(),
        .34D,
        getContentManager());
    defaultMobileOption = OPTION_STEPS.createOption(
        true,
        List.of(defaultMobileWidget.getUid()),
        mobile_experiment.getUuid(),
        .33D,
        getContentManager());
    abTestMobileOption1 = OPTION_STEPS.createOption(
        false,
        null,
        mobile_experiment.getUuid(),
        .33D,
        getContentManager());
    abTestMobileOption2 = OPTION_STEPS.createOption(
        false,
        List.of(abTest_mobile_widget.getUid()),
        mobile_experiment.getUuid(),
        .34D,
        getContentManager());
    runningDesktopExperiment =
        EXPERIMENT_STEPS.runExperimentAssumingSuccess(desktop_experiment, getContentManager());
    runningMobileExperiment =
        EXPERIMENT_STEPS.runExperimentAssumingSuccess(mobile_experiment, getContentManager());
  }

  @Test(description = "Тест получения признака участия в эксперименте\n"
      + "\t1. Статус эксперимента 'RUNNING'",
      dataProvider = "dataProvider")
  public void involvementsRunningExperimentPositiveTest(
      @ParameterKey("Устройство пользователя") final Device clientDevice,
      @ParameterKey("Гео-метка  пользователя") final List<String> geos) {
    final var response = EXPERIMENT_STEPS.getInvolvements(
        pageId,
        clientDevice,
        geos,
        getContentManager());
    final var softly = new SoftAssertions();
    boolean isInvolved;
    if (clientDevice.equals(desktop)) {
      assertThat(isInvolved = response.jsonPath().getBoolean("involved"))
          .as("Проверка наличия признака вовлеченности")
          .isNotNull();
      if (isInvolved) {
        String optionName;
        softly.assertThat(optionName = response.jsonPath().getString("optionName"))
            .as("Проверка наименования варианта АБ-теста")
            .isIn(
                defaultDesktopOption.getName(),
                abTestDesktopOption1.getName(),
                abTestDesktopOption2.getName());
        if (optionName.equals(abTestDesktopOption1.getName())) {
          abTestDesktopOption1counter++;
        } else if (optionName.equals(abTestDesktopOption2.getName())) {
          abTestDesktopOption2counter++;
        } else {
          defaultDesktopOptionCounter++;
        }
      } else {
        softly.assertThat(response.jsonPath().getString("optionName"))
            .as("Проверка наименования дефолтного варианта")
            .isEqualTo(defaultDesktopOption.getName());
      }
      softly.assertThat(response.jsonPath().getString("uuid"))
          .as("Проверка UUID эксперимента")
          .isEqualTo(runningDesktopExperiment.getUuid());
      softly.assertThat(response.jsonPath().getString("cookieValue"))
          .as("Проверка cookieValue эксперимента")
          .isEqualTo(runningDesktopExperiment.getCookieValue());
      softly.assertThat(response.jsonPath().getString("endDate"))
          .as("Проверка даты завершения эксперимента")
          .isEqualTo(runningDesktopExperiment.getEndDate());
    } else {
      assertThat(isInvolved = response.jsonPath().getBoolean("involved"))
          .as("Приверка наличия признака вовлеченности")
          .isNotNull();
      if (isInvolved) {
        String optionName;
        softly.assertThat(optionName = response.jsonPath().getString("optionName"))
            .as("Проверка наименования варианта АБ-теста")
            .isIn(
                defaultMobileOption.getName(),
                abTestMobileOption1.getName(),
                abTestMobileOption2.getName());
        if (optionName.equals(abTestMobileOption1.getName())) {
          abTestMobileOption1counter++;
        } else if (optionName.equals(abTestMobileOption2.getName())) {
          abTestMobileOption2counter++;
        } else {
          defaultMobileOptionCounter++;
        }
      } else {
        softly.assertThat(response.jsonPath().getString("optionName"))
            .as("Проверка наименования дефолтного варианта")
            .isEqualTo(defaultMobileOption.getName());
      }
      softly.assertThat(response.jsonPath().getString("uuid"))
          .as("Проверка UUID эксперимента")
          .isEqualTo(runningMobileExperiment.getUuid());
      softly.assertThat(response.jsonPath().getString("cookieValue"))
          .as("Проверка cookieValue эксперимента")
          .isEqualTo(runningMobileExperiment.getCookieValue());
      softly.assertThat(response.jsonPath().getString("endDate"))
          .as("Проверка даты завершения эксперимента")
          .isEqualTo(runningMobileExperiment.getEndDate());
    }
    softly.assertAll();
  }
}
