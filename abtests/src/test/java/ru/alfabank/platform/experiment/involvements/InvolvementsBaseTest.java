package ru.alfabank.platform.experiment.involvements;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.Device.mobile;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;

import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Device;
import ru.alfabank.platform.businessobjects.Option;

public class InvolvementsBaseTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(InvolvementsBaseTest.class);

  protected Option option2;
  protected Option option3;
  protected int option2counter;
  protected int option3counter;

  /**
   * Получение флага.
   * @param pageId pageId
   * @param device device
   * @param geoGroups geos
   * @return response
   */
  protected Response getInvolvements(Integer pageId, Device device, List<String> geoGroups) {
    LOGGER.info(String.format(
        "Выполняю запрос на получение флага для страницы '%d', девайса '%s' и гео-групп '%s'",
        pageId, device, Arrays.toString(geoGroups.toArray())));
    final var response =
        given()
            .spec(involvementsSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .queryParam("pageId", pageId)
            .queryParam("device", device)
            .queryParam("geoGroups", geoGroups)
            .when().get()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    return response;
  }

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  protected static Object[][] dataProvider() {
    return new Object[][]{
        {desktop, List.of("ru")},
        {desktop, List.of("ru", "msk")}
    };
  }

  /**
   * After class.
   */
  @AfterClass(description = "Посчет количества выпадений каждого из вариантов")
  public void afterClass() {
    LOGGER.info(String.format("Количество выпадений вариата '%s' - '%d'",
        option2, option2counter));
    LOGGER.info(String.format("Количество выпадений вариата '%s' - '%d'",
        option3, option3counter));
  }
}
