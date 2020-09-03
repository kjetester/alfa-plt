package ru.alfabank.platform.steps.geofacade;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import io.restassured.response.Response;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.contentstore.Property;
import ru.alfabank.platform.businessobjects.geofacade.GeoGroup;
import ru.alfabank.platform.businessobjects.geofacade.GeoGroupWithCities;
import ru.alfabank.platform.steps.BaseSteps;

public class GeoSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(GeoSteps.class);

  /**
   * Create Geo Group assuming success.
   *
   * @param geoGroup geo group
   * @return response
   */
  public GeoGroup createGeoGroupAssumingSuccess(final GeoGroup geoGroup) {
    final var response = createGeoGroup(geoGroup);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    return response.as(GeoGroup.class);
  }

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
        .auth().oauth2(getContentManager().getJwt().getAccessToken())
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
    final var response = getGeoGroup(geoGroup);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    return response.as(GeoGroup.class);
  }

  /**
   * Check if geo group is absent.
   * @param geoGroup geo group
   */
  public void getAbsentGeoGroup(final GeoGroup geoGroup) {
    final var response = getGeoGroup(geoGroup);
    assertThat(response.getStatusCode()).isEqualTo(SC_NOT_FOUND);
    assertThat(response.getBody().asString())
        .contains("GeoGroup wasn't found for id=" + geoGroup.getId());
  }

  private Response getGeoGroup(final GeoGroup geoGroup) {
    LOGGER.info("Выполняю запрос на чтение гео-группы:\n" + describeBusinessObject(geoGroup));
    final var response = given()
        .spec(getGeoFacadeGeoGroupsIdSpec())
        .auth().oauth2(getContentManager().getJwt().getAccessToken())
        .pathParam("id", geoGroup.getId())
        .get();
    describeResponse(LOGGER, response);
    return response;
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
        final var response = deleteGeoGroup(entry.getValue());
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

  /**
   * Delete geo group assuming success.
   * @param geoGroup geo group
   */
  public void deleteGeoGroupAssumingSuccess(final GeoGroup geoGroup) {
    final var response = deleteGeoGroup(geoGroup);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
  }

  /**
   * Delete a geo group assuming failure due to its usage on a page.
   * @param geoGroup geo group
   * @param pageId page ID
   */
  public void deleteGeoGroupAssumingFailureDueToItsUsage(final GeoGroup geoGroup,
                                                         final int pageId) {
    final var response = deleteGeoGroup(geoGroup);
    assertThat(response.getStatusCode()).isGreaterThan(SC_BAD_REQUEST);

    final var page = CREATED_PAGES.get(pageId);
    final var message = List.of(
        "Невозможно удалить геогруппу, используемую на страницах",
        String.format("id\": %s", page.getId()),
        String.format("uri\": \"%s", page.getUri()),
        String.format("title\": \"%s", page.getTitle()));
    assertThat(response.getBody().print()).contains(message);
  }

  private Response deleteGeoGroup(final GeoGroup geoGroup) {
    LOGGER.info("Выполняю запрос на удаление гео-группы:\n"
        + describeBusinessObject(geoGroup));
    final var response = given()
        .spec(getGeoFacadeGeoGroupsIdSpec())
        .auth().oauth2(getContentManager().getJwt().getAccessToken())
        .pathParam("id", geoGroup.getId())
        .delete();
    describeResponse(LOGGER, response);
    return response;
  }
}
