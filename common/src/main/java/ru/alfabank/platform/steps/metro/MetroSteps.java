package ru.alfabank.platform.steps.metro;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.metro.Metro;
import ru.alfabank.platform.steps.BaseSteps;

public class MetroSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(MetroSteps.class);

  /**
   * Get nearest metro station name in 2 km.
   *
   * @param lat lat
   * @param lon lon
   * @return metro station
   */
  public String getNearestMetroNameIn2km(final Double lat,
                                         final Double lon) {
    return Arrays.stream(
        getMetro(Map.of(
            "count", 50,
            "lat", lat,
            "lon", lon,
            "radius", 2000))
            .as(Metro[].class))
        .min(Comparator.comparing(Metro::getDistance))
        .orElse(new Metro.Builder().setName(null).build()).getName();
  }

  private Response getMetro(final Map<String, ?> queryParams) {
    LOGGER.info("Выполняю запрос на получение списка станций метро:\n"
        + queryParams.toString());
    final var response = given()
        .spec(getMetroSpec())
        .queryParams(queryParams)
        .get();
    describeResponse(LOGGER, response);
    return response;
  }
}
