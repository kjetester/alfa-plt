package ru.alfabank.platform.experiment.update.negative;

import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.MG;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.ProductType;
import ru.alfabank.platform.experiment.update.positive.UpdateInactiveExperimentTest;

public class UpdateActiveExperimentTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(UpdateInactiveExperimentTest.class);

  private Experiment experiment;

  /**
   * Before method.
   */
  @BeforeMethod(description = "Создание активного эксперимента "
      + "для негативного теста изменения активного эксперимента")
  public void beforeMethod() {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    createWidget(
        createdPages.get(page.getId()),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null);
    page = createdPages.get(page.getId());
    final var widget = page.getWidgetList().get(0);
    experiment = createExperiment(
        widget.getDevice(),
        page.getId(),
        getRandomProductType(),
        getValidEndDate(),
        0.9);
    createOption(true, emptyList(), experiment.getUuid(), .5D);
    createOption(false, List.of(widget.getUid()), experiment.getUuid(), .5D);
    experiment = runExperimentAssumingSuccess(experiment);
  }

  @Test (
      description = "Негативный тест изменения активного эксперимента",
      dataProvider = "activeWidgetNegativeDataProvider")
  public void updateActiveExperimentTest(
      @ParameterKey ("Test Case") final String testCase,
      @ParameterKey("New Value") final Object newValue) {
    LOGGER.info("Test case: " + testCase);
    final var field2bChanged = StringUtils.substringBetween(testCase, "'");
    Experiment changeSetBody;
    final String cookieValue = experiment.getCookieValue();
    final String description = experiment.getDescription();
    final ProductType productType = experiment.getProductTypeKey();
    final String endDate = experiment.getEndDate();
    final Double trafficRate = experiment.getTrafficRate();
    switch (field2bChanged) {
      case "cookieValue" : {
        changeSetBody = new Experiment.Builder()
            .setCookieValue((String) newValue)
            .setDescription(description)
            .setProductTypeKey(productType)
            .setEndDate(endDate)
            .setTrafficRate(trafficRate)
            .build();
        break;
      }
      case "description" : {
        changeSetBody = new Experiment.Builder()
            .setCookieValue(cookieValue)
            .setDescription((String) newValue)
            .setProductTypeKey(productType)
            .setEndDate(endDate)
            .setTrafficRate(trafficRate)
            .build();
        break;
      }
      case "productTypeKey" : {
        changeSetBody = new Experiment.Builder()
            .setCookieValue(cookieValue)
            .setDescription(description)
            .setProductTypeKey((ProductType) newValue)
            .setEndDate(endDate)
            .setTrafficRate(trafficRate)
            .build();
        break;
      }
      case "endDate" : {
        changeSetBody = new Experiment.Builder()
            .setCookieValue(cookieValue)
            .setDescription(description)
            .setProductTypeKey(productType)
            .setEndDate((String) newValue)
            .setTrafficRate(trafficRate)
            .build();
        break;
      }
      case "trafficRate" : {
        changeSetBody = new Experiment.Builder()
            .setCookieValue(cookieValue)
            .setDescription(description)
            .setProductTypeKey(productType)
            .setEndDate(endDate)
            .setTrafficRate((Double) newValue)
            .build();
        break;
      }
      case "pageId" : {
        changeSetBody = new Experiment.Builder()
            .setPageId((Integer) newValue)
            .setCookieValue(cookieValue)
            .setDescription(description)
            .setProductTypeKey(productType)
            .setEndDate(endDate)
            .setTrafficRate(trafficRate)
            .build();
        break;
      }
      case "device" : {
        changeSetBody = new Experiment.Builder()
            .setDevice((Device) newValue)
            .setCookieValue(cookieValue)
            .setDescription(description)
            .setProductTypeKey(productType)
            .setEndDate(endDate)
            .setTrafficRate(trafficRate)
            .build();
        break;
      }
      default: {
        throw new IllegalArgumentException();
      }
    }
    LOGGER.info("Выполняю запрос изменения эксперимента:\n"
        + describeBusinessObject(changeSetBody));
    var response =
        given()
            .spec(getDeletePatchExperimentSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("uuid", experiment.getUuid())
            .body(changeSetBody)
        .when()
            .patch()
        .then()
            .extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(response.jsonPath().getString("message"))
        .as("Проверка сообщения об ошибке")
        .isEqualTo("Невозможно изменить активный эксперимент " + experiment.getUuid());
  }

  /**
   * Negative data provider.
   * @return test data
   */
  @DataProvider(name = "activeWidgetNegativeDataProvider")
  public Object[][] activeWidgetNegativeDataProvider() {
    return new Object[][]{
        {"Изменение 'cookieValue'", randomAlphanumeric(2)},
        {"Изменение 'description'", randomAlphanumeric(2)},
        {"Изменение 'productTypeKey'", MG},
        {"Изменение 'endDate'", getValidEndDatePlusOneMonth()},
        {"Изменение 'trafficRate'", 0.11D},
        {"Изменение 'pageId'", 1},
        {"Изменение 'device'", mobile}
    };
  }
}
