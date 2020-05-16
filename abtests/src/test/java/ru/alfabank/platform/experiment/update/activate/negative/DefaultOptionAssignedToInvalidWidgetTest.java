package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.enums.ExperimentOptionName;

public class DefaultOptionAssignedToInvalidWidgetTest extends BaseTest {

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\t1. Вариант привязан к виджету с некорренктыми параметрами",
      dataProvider = "dataProvider")
  public void defaultOptionAssignedToInvalidWidgetTest(
      @ParameterKey("Test Case")
      final String testCase,
      @ParameterKey("Is Widget for default option enabled")
      final Boolean enabledWidget1,
      @ParameterKey("Widget ExperimentOptionName for default option")
      final ExperimentOptionName expOptName1,
      @ParameterKey("Is Widget for default option default")
      final Boolean defaultWidget1,
      @ParameterKey("Is Widget for NON default option enabled")
      final Boolean enabledWidget2,
      @ParameterKey("Widget ExperimentOptionName for NON default option")
      final ExperimentOptionName expOptName2,
      @ParameterKey("Is Widget for NON default option default")
      final Boolean defaultWidget2) {
    final var experimentEndDate = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var widget1 = createWidget(
        createdPages.get(pageId),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget2 = createWidget(
        createdPages.get(pageId),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment = createExperiment(
        device,
        pageId,
        getRandomProductType(),
        experimentEndDate,
        trafficRate,
        getContentManager());
    final var defaultOption = createOption(true,
        List.of(widget1.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    final var nonDefaultOption = createOption(false,
        List.of(widget2.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    if (widget1.isEnabled() != enabledWidget1
        || !widget1.getExperimentOptionName().equals(expOptName1)
        || widget1.isDefaultWidget() != defaultWidget1) {
      changeWidgetABtestProps(widget1, pageId, enabledWidget1, expOptName1, defaultWidget1, getContentManager());
    }
    if (widget2.isEnabled() != enabledWidget2
        || !widget2.getExperimentOptionName().equals(expOptName2)
        || widget2.isDefaultWidget() != defaultWidget2) {
      changeWidgetABtestProps(widget2, pageId, enabledWidget2, expOptName2, defaultWidget2, getContentManager());
    }
    final var experimentStart = getCurrentDateTime().plusSeconds(10).toString();
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(false)
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(experimentStart)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    if ("Дефолтный".equals(StringUtils.substringBefore(testCase, " "))) {
      assertThat(result.asString())
          .as("Проверка сообщения об ошибке")
          .contains("Для варианта по умолчанию '" + defaultOption.getName()
              + "' виджеты '[" + widget1.getUid()
              + "]' должны быть с дефолтным названием варианта,"
              + " быть включенными и быть виджетами по умолчанию");
    } else {
      assertThat(result.asString())
          .as("Проверка сообщения об ошибке")
          .contains("Для варианта '" + nonDefaultOption.getName()
              + "' виджеты '[" + widget2.getUid()
              + "]' должны быть помечены как 'forABtest',"
              + " быть выключенными и не должны быть виджетами по умолчанию");
    }
    getExperiment(actualExperiment, getContentManager()).checkUpdatedExperiment(expectedExperiment);
  }

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  public static Object[][] dataProvider() {
    return new Object[][]{
        {"Дефолтный вариант привязан к невалидному виджету",
            false, FOR_AB_TEST, false,
            false, FOR_AB_TEST, false},
        {"Дефолтный вариант привязан к невалидному виджету",
            true, FOR_AB_TEST, false,
            false, FOR_AB_TEST, false},
        {"Недефолтный вариант привязан к невалидному виджету",
            true, DEFAULT, true,
            true, DEFAULT, true},
        {"Недефолтный вариант привязан к невалидному виджету",
            true, DEFAULT, true,
            true, FOR_AB_TEST, false},
        {"Недефолтный вариант привязан к невалидному виджету",
            true, DEFAULT, true,
            false, DEFAULT, true}
    };
  }
}
