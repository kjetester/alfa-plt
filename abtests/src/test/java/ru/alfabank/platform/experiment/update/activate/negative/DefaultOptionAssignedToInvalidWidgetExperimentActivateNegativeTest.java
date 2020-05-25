package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
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
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.enums.ExperimentOptionName;

public class DefaultOptionAssignedToInvalidWidgetExperimentActivateNegativeTest extends BaseTest {

  private Integer pageId;
  private Widget defaultWidget;
  private Widget abTestWidget;
  private Experiment experiment;
  private Option defaultOption;
  private Option abTestOption;

  /**
   * Before method.
   */
  @BeforeMethod(description = "Выполнение предусловий:"
      + "\n\t1. На странице нет запущенных экспериментов"
      + "\n\t2. Даты активности страницы не установлены"
      + "\n\t3. Даты активности виджетов не установлены"
      + "\n\t4. Статус эксперимента 'DISABLED'"
      + "\n\t5. Дата окончания эксперимента более текущей даты + 1 день"
      + "\n\t6. У эксперимента есть 2 варианта:"
      + "\n\t\t1. Дефолтный вариант привязан к нешаренному виджету, у которого:"
      + "\n\t\t\t* enable=true"
      + "\n\t\t\t* experimentOptionName=default"
      + "\n\t\t\t* defaultWidget=true"
      + "\n\t\t2. Недефолтный вариант привязан к нешаренному виджету, у которого:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false",
      alwaysRun = true)
  public void beforeMethod() {
    pageId = PAGES_STEPS.createEnabledPage(getContentManager());
    defaultWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(pageId),
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
        CREATED_PAGES.get(pageId),
        null, desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    experiment = EXPERIMENT_STEPS.createExperiment(desktop,
        pageId,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    defaultOption = OPTION_STEPS.createOption(
        true,
        List.of(defaultWidget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    abTestOption = OPTION_STEPS.createOption(
        false,
        List.of(abTestWidget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
  }

  @Test(description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Вариант привязан к виджету с некорренктыми параметрами",
      dataProvider = "dataProvider")
  public void defaultOptionAssignedToInvalidWidgetExperimentActivateNegativeTest(
      @ParameterKey("Test Case")
      final String testCase,
      @ParameterKey("Is Widget for default option enabled") final Boolean isDefaultWidgeEnabled,
      @ParameterKey("Widget ExperimentOptionName for default option")
      final ExperimentOptionName defaultOptionName,
      @ParameterKey("Is Widget for default option default")
      final Boolean defaultWidget1,
      @ParameterKey("Is Widget for NON default option enabled")
      final Boolean isAbTestWidgetEnabled,
      @ParameterKey("Widget ExperimentOptionName for NON default option")
      final ExperimentOptionName abTestOptionName,
      @ParameterKey("Is Widget for NON default option default")
      final Boolean defaultWidget2) {
    if (defaultWidget.isEnabled() != isDefaultWidgeEnabled
        || !defaultWidget.getExperimentOptionName().equals(defaultOptionName)
        || defaultWidget.isDefaultWidget() != defaultWidget1) {
      DRAFT_STEPS.changeWidgetABtestProps(defaultWidget, pageId, isDefaultWidgeEnabled,
          defaultOptionName, defaultWidget1, getContentManager());
    }
    if (abTestWidget.isEnabled() != isAbTestWidgetEnabled
        || !abTestWidget.getExperimentOptionName().equals(abTestOptionName)
        || abTestWidget.isDefaultWidget() != defaultWidget2) {
      DRAFT_STEPS.changeWidgetABtestProps(abTestWidget, pageId, isAbTestWidgetEnabled,
          abTestOptionName, defaultWidget2, getContentManager());
    }
    final var result = EXPERIMENT_STEPS.runExperimentAssumingFail(experiment, getContentManager());
    assertThat(result.getStatusCode()).as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    if ("Дефолтный".equals(StringUtils.substringBefore(testCase, " "))) {
      assertThat(result.asString()).as("Проверка сообщения об ошибке")
          .contains("Для варианта по умолчанию '" + defaultOption.getName()
              + "' виджеты '[" + defaultWidget.getUid()
              + "]' должны быть с дефолтным названием варианта,"
              + " быть включенными и быть виджетами по умолчанию");
    } else {
      assertThat(result.asString()).as("Проверка сообщения об ошибке")
          .contains("Для варианта '" + abTestOption.getName()
              + "' виджеты '[" + abTestWidget.getUid()
              + "]' должны быть помечены как 'forABtest',"
              + " быть выключенными и не должны быть виджетами по умолчанию");
    }
    EXPERIMENT_STEPS.getExistingExperiment(experiment, getContentManager()).equals(experiment);
  }

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] dataProvider() {
    return new Object[][]{
        {
            "Дефолтный вариант привязан к невалидному виджету",
            false,
            FOR_AB_TEST,
            false,
            false,
            FOR_AB_TEST,
            false
        },
        {
            "Дефолтный вариант привязан к невалидному виджету",
            true,
            FOR_AB_TEST,
            false,
            false,
            FOR_AB_TEST,
            false
        },
        {
            "Недефолтный вариант привязан к невалидному виджету",
            true,
            DEFAULT,
            true,
            true,
            DEFAULT,
            true
        },
        {
            "Недефолтный вариант привязан к невалидному виджету",
            true,
            DEFAULT,
            true,
            true,
            FOR_AB_TEST,
            false
        },
        {
            "Недефолтный вариант привязан к невалидному виджету",
            true,
            DEFAULT,
            true,
            false,
            DEFAULT,
            true
        }
    };
  }
}
