package ru.alfabank.platform.steps.geofacade;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.helpers.DaDaTaHelper.getCityOrSettlementFiasIdByLocationCoordinates;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.TestNGException;
import ru.alfabank.platform.businessobjects.cities.Cities;
import ru.alfabank.platform.businessobjects.cities.Cities.City;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location;
import ru.alfabank.platform.helpers.DaDaTaHelper;
import ru.alfabank.platform.steps.BaseSteps;

public class CitiesSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(Cities.class);

  /**
   * Get FIAS codes based on cities list.
   *
   * @param cityList list of cities
   * @return list of FIASes
   */
  public List<String> getCityFiasCodeList(final List<String> cityList) {
    return getCitiesList(Map.of("name", cityList)).jsonPath().getList("cities.fiasId");
  }

  /**
   * Get full meta based on cities list.
   *
   * @param citiesNamesList list of cities
   * @return list of cities
   */
  public List<City> getCityListWithMetaByName(final List<String> citiesNamesList) {
    return getCitiesList(Map.of("name", citiesNamesList)).as(Cities.class).getCities();
  }

  /**
   * Get cities list by cities names.
   *
   * @param queryParams query params
   * @return response
   */
  public Response getCitiesList(final Map<String, ?> queryParams) {
    LOGGER.info("Выполняю запрос в '/cities' на получение мета по городам:\n"
        + queryParams.toString());
    final var response = given()
        .spec(getCitySpec())
        .queryParams(queryParams)
        .get();
    describeResponse(LOGGER, response);
    assertThat(response.statusCode()).isEqualTo(SC_OK);
    return response;
  }

  /**
   * Get aJson ID by FIAS ID received from DaDaTa.
   *
   * @param location location
   * @return aJsonId
   */
  public Integer getAJsonId(final Location location) {
    final var daDaTaSuggestion = getCityOrSettlementFiasIdByLocationCoordinates(location, 10)
        .getData();
    final String fiasId;
    if (daDaTaSuggestion.getCityFiasId() != null) {
      fiasId = daDaTaSuggestion.getCityFiasId();
    } else if (daDaTaSuggestion.getSettlementFiasId() != null) {
      fiasId = daDaTaSuggestion.getSettlementFiasId();
    } else {
      fiasId = daDaTaSuggestion.getAreaFiasId();
    }
    var citiesList = getCitiesList(Map.of("fiasId", fiasId));
    if (citiesList.jsonPath().getList("cities").size() == 0) {
      citiesList = getCitiesList(Map.of("fiasId", daDaTaSuggestion.getAreaFiasId()));
    }
    return citiesList.as(Cities.class).getCities().stream().map(City::getAjsonId).findFirst()
        .orElseThrow(() -> new TestNGException("Сервис /cities не вернул ни одного города"));
  }
}
