package ru.alfabank.platform.steps.geofacade;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;

import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.geofacade.GeoGroup;
import ru.alfabank.platform.businessobjects.geofacade.GeoGroupWithCities;
import ru.alfabank.platform.steps.BaseSteps;

public class GeoSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(GeoSteps.class);

  /**
   * Create Geo Group.
   *
   * @param body geo group
   * @return response
   */
  public Response createGeoGroup(final GeoGroup body) {
    LOGGER.info("Выполняю запрос на создание гео-группы:\n" + describeBusinessObject(body));
    final var response = given()
        .spec(getGeoFacadeGeoGroupsSpec())
        .body(body)
        .post();
    describeResponse(LOGGER, response);
    if (response.getStatusCode() == SC_OK) {
      final var geoGroup = response.as(GeoGroup.class);
      CREATED_GEO_GROUPS.put(geoGroup.getId(), geoGroup);
    }
    return response;
  }

  /**
   * Get existing Geo Group.
   *
   * @param geoGroup Geo Group
   * @return Geo Group
   */
  public GeoGroup getExistingGeoGroup(final GeoGroup geoGroup) {
    LOGGER.info("Выполняю запрос на чтение гео-группы:\n" + describeBusinessObject(geoGroup));
    final var response = given()
        .spec(getGeoFacadeGeoGroupsIdSpec())
        .pathParam("id", geoGroup.getId())
        .get();
    describeResponse(LOGGER, response);
    return response.as(GeoGroup.class);
  }

  /**
   * Get existing Geo Group with its cities.
   *
   * @param geoGroup Geo Group
   * @return Geo Group
   */
  public GeoGroupWithCities getExistingGeoGroupWithCities(final GeoGroup geoGroup) {
    LOGGER.info("Выполняю запрос на чтение гео-группы со споском городов:\n"
        + describeBusinessObject(geoGroup));
    final var response = given()
        .spec(getGeoFacadeGeoGroupsIdCitiesSpec())
        .pathParam("id", geoGroup.getId())
        .get();
    describeResponse(LOGGER, response);
    return response.as(GeoGroupWithCities.class);
  }

  /**
   * Delete all created geo-groups.
   */
  public void deleteAllCreatedGeoGroups() {
    final var geoGroupsCount = CREATED_GEO_GROUPS.size();
    if (geoGroupsCount > 0) {
      LOGGER.info(String.format("Гео-групп к удалению: %d", geoGroupsCount));
      CREATED_GEO_GROUPS.entrySet().parallelStream().forEach((entry) -> {
        LOGGER.info(String.format("Начинаю процесс удаления гео-группы '%s'",
            entry.getValue().getCode()));
        final var response = given()
            .spec(getGeoFacadeGeoGroupsIdSpec())
            .pathParam("id", entry.getValue().getId())
            .delete();
        if (response.getStatusCode() == SC_OK) {
          LOGGER.info(String.format("Гео-группа '%s' удалена", entry.getValue().getCode()));
        } else {
          LOGGER.warn(String.format("Гео-группа не была удалена:\n'%s'\n%s",
              response.getStatusCode(),
              response.prettyPrint()));
        }
      });
    }
  }
}
