package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.enums.User;

public class PageVisibleLimitedTest extends BaseTest {

  private static final String ERROR_MESSAGE =
      "Страница '%d' должна быть видима во время проведения эксперимента '%s'";

  @Test (description = "Тест активации эксперимента с негативными условиями:"
      + "\n\t1. Страница невидима или частично видима", dataProvider = "dataProvider")
  public void pageVisibleLimitedTest(@ParameterKey("Дата начала") final String start,
                                     @ParameterKey("Дата окончания") final String end,
                                     @ParameterKey("Видимость") final Boolean isEnabled) {
    setUser(User.CONTENT_MANAGER);
    final var experimentStart = getCurrentDateTime().plusSeconds(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(start, end, isEnabled);
    final var pageId = page.getId();
    createWidget(createdPages.get(pageId), null, desktop, true, DEFAULT, true, null, null);
    createWidget(createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1 = page.getWidgetList().get(0);
    final var widget2 = page.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEnd, trafficRate);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate);
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
        .setCreatedBy(getUser().getLogin())
        .setActivationDate(null)
        .setStatus(DISABLED)
        .setCreationDate(experimentStart)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(String.format(ERROR_MESSAGE, pageId, actualExperiment.getUuid()));
    getExperiment(actualExperiment).checkUpdatedExperiment(expectedExperiment);
  }

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  public Object[][] dataProvider() {
    return new Object[][]{
        {
            getCurrentDateTime().plusHours(6).toString(),
            getCurrentDateTime().plusHours(12).toString(),
            true
        },
        {
            getCurrentDateTime().plusHours(6).toString(),
            null,
            true
        },
        {
            null,
            getCurrentDateTime().plusHours(12).toString(),
            true
        },
        {
            null,
            null,
            false
        }
    };
  }
}
