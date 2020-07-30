package ru.alfabank.platform.helpers;

import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.steps.BaseSteps.describeResponse;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.TestException;
import org.testng.TestNGException;
import ru.alfabank.platform.businessobjects.dadata.Suggestions;
import ru.alfabank.platform.businessobjects.dadata.Suggestions.Suggestion;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location;

public class DaDaTaHelper {

  private static final Logger LOGGER = LogManager.getLogger(DaDaTaHelper.class);
  //  private static final String TOKEN = "Token c700cdb63b3bb969dfad44120d06ba725e629373";
  private static final String TOKEN = "Token 63d47b5c4f5277c67b0e02879a9f807da28b8d0c";

  /**
   * Get suggestion from DaDaTa.
   *
   * @param location location
   * @return fiasId
   */
  public String getCityOrSettlementFiasIdByLocationFiasId(final Location location) {
    LOGGER.info("Выполняю запрос в ДаДаТа '/fias' на получение мета по локации с fiasId:\n"
        + location.getFiasId());
    final var spec = new RequestSpecBuilder()
        .setBaseUri("https://suggestions.dadata.ru/suggestions/api/4_1/rs/findById")
        .setContentType(ContentType.JSON).setAccept(ContentType.JSON)
        .addHeader("Authorization", TOKEN)
        .setBody(String.format("{\"query\":\"%s\"}", location.getFiasId()))
        .build();
    Response response = given().spec(spec).basePath("fias").post();
    var suggestion = response.getBody().jsonPath();
    if (suggestion.getList("suggestions").isEmpty()) {
      LOGGER.warn("В /fias нет данных, полез в /address");
      response = given().spec(spec).basePath("address").post();
      suggestion = given().spec(spec).basePath("address").post().getBody().jsonPath();
    }
    describeResponse(LOGGER, response);
    final var cityFiasId = suggestion.getString("suggestions.data.city_fias_id")
        .replaceAll("\\[", "").replaceAll("]", "");
    if (!cityFiasId.equals("null")) {
      return cityFiasId;
    } else {
      LOGGER.warn("'city_fias_id' отсутствует, ищу 'settlement_fias_id'");
      final var settlementFiasId = suggestion.getString("suggestions.data.settlement_fias_id")
          .replaceAll("\\[", "").replaceAll("]", "");
      if (!settlementFiasId.equals("null")) {
        return settlementFiasId;
      } else {
        throw new TestException("'settlement_fias_id' тоже отсутствует");
      }
    }
  }

  /**
   * Get suggestion from DaDaTa.
   *
   * @param location location
   * @return fiasId
   */
  public static Suggestion getCityOrSettlementFiasIdByLocationCoordinates(final Location location,
                                                                   final Integer radius) {
    LOGGER.info(String.format("""
        Выполняю запрос в ДаДаТа '/geolocate/address' на получение мета по координатам:
        lat=%s
        lon=%s
        radius=%s""",
        location.getLat(), location.getLon(), radius));
    final var spec = new RequestSpecBuilder()
        .setBaseUri("https://suggestions.dadata.ru/suggestions/api/4_1/rs")
        .setBasePath("geolocate/address")
        .setContentType(ContentType.JSON).setAccept(ContentType.JSON)
        .addHeader("Authorization", TOKEN)
        .addQueryParams(Map.of(
            "lat", location.getLat(),
            "lon", location.getLon(),
            "radius_meters", radius))
        .build();
    Response response = given().spec(spec).get();
    describeResponse(LOGGER, response);
    final var suggestionsList = response.getBody().as(Suggestions.class).getSuggestions();
    return suggestionsList.stream().filter(s ->
        s.getData().getSettlementFiasId() != null
            || s.getData().getCityFiasId() != null).findFirst().orElseGet(() -> radius <= 200
        ? getCityOrSettlementFiasIdByLocationCoordinates(location, radius + 50)
        : null);
  }
}
