package ru.alfabank.platform.geogroups.creation;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.GeoGroupType.MUTABLE;

import java.time.Instant;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.geofacade.GeoGroup;
import ru.alfabank.platform.businessobjects.geofacade.GeoGroupWithCities;

public class GeoGroupCreationTest extends BaseTest {

  /**
   * Data provider.
   *
   * @return positive test data
   */
  @DataProvider
  public static Object[][] positiveDataProvider() {
    return new Object[][]{
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            List.of("Москва")
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            null,
            List.of("Москва")
        },
        {
            randomAlphanumeric(200),
            randomAlphanumeric(200),
            randomAlphanumeric(1000),
            List.of("Москва", "Пермь", "Владимир", "Нижний Новгород",
                "Екатеринбурн", "Иркутск", "Киров", "Кизел")
        }
    };
  }

  @Test(dataProvider = "positiveDataProvider")
  private void geoGroupCreationPositiveTest(final String code,
                                            final String name,
                                            final String description,
                                            final List<String> cityList) {
    final var fias_code_list = CITIES_STEPS.getCityFiasCodeList(cityList);
    final var creation_request_body = new GeoGroup.Builder()
        .setCode(code)
        .setName(name)
        .setDescription(description)
        .setCities(fias_code_list)
        .build();
    final var response = GEO_STEPS.createGeoGroup(creation_request_body);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    final var actualGeo = response.as(GeoGroup.class);
    final var expectedGeo = new GeoGroup.Builder()
        .using(actualGeo)
        .setCode(creation_request_body.getCode())
        .setName(creation_request_body.getName())
        .setDescription(creation_request_body.getDescription())
        .setChangedDate(Instant.now().toString())
        .setType(MUTABLE.getType())
        .build();
    GEO_STEPS.getExistingGeoGroup(actualGeo).equals(expectedGeo);
    final var city_list_with_meta = CITIES_STEPS.getCityListWithMetaByName(cityList);
    final var expectedGeoWithCities = new GeoGroupWithCities.Builder()
        .using(expectedGeo)
        .setCities(city_list_with_meta)
        .build();
    GEO_STEPS.getExistingGeoGroupWithCities(actualGeo).equals(expectedGeoWithCities);
  }
}
