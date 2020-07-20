package ru.alfabank.platform.steps.geofacade;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.alfabank.platform.businessobjects.cities.Cities;
import ru.alfabank.platform.businessobjects.cities.Cities.City;
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
   * @return list of FIASes
   */
  public List<City> getCityListWithMetaByName(final List<String> citiesNamesList) {
    return getCitiesList(Map.of("name", citiesNamesList)).as(Cities.class).getCities();
  }

  /**
   * Get cities list bu cities names.
   *
   * @param queryParams query params
   * @return response
   */
  @NotNull
  public Response getCitiesList(final Map<String, ?> queryParams) {
    LOGGER.info("Выполняю запрос на получение мета по городам:\n"
        + queryParams.toString());
    final var response = given()
        .spec(getCitySpec())
        .queryParams(queryParams)
        .get();
    describeResponse(LOGGER, response);
    assertThat(response.statusCode()).isEqualTo(SC_OK);
    return response;
  }
}
