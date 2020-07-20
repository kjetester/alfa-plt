package ru.alfabank.platform;

import java.util.List;
import java.util.Map;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.businessobjects.cities.Cities;
import ru.alfabank.platform.businessobjects.cities.Cities.City;
import ru.alfabank.platform.steps.geofacade.CitiesSteps;

public class BaseTest {

  protected static final CitiesSteps CITIES_STEPS = new CitiesSteps();

  /**
   * Positive test data provider.
   *
   * @return Positive test data
   */
  @DataProvider
  public static Object[][] positiveDataProvider() {
    return new Object[][]{
        {
            Map.of("ajsonId", List.of(21)),
            new Cities(List.of(
                new City.Builder()
                    .setFiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")
                    .setFiasName("г Москва")
                    .setAjsonId(21)
                    .setCityNameRus("Москва")
                    .setPath("moscow")
                    .setCityNameNorm("москва")
                    .setGenitiveName("Москвы")
                    .setRegionId(77)
                    .setLatitude(55.7536)
                    .setLongitude(37.6092)
                    .setParentCityId(0)
                    .setCityDepartment("MOZQ")
                    .build()))
        },
        {
            Map.of("ajsonId", List.of(21, 56, 89)),
            new Cities(List.of(
                new City.Builder()
                    .setFiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")
                    .setFiasName("г Москва")
                    .setAjsonId(21)
                    .setCityNameRus("Москва")
                    .setPath("moscow")
                    .setCityNameNorm("москва")
                    .setGenitiveName("Москвы")
                    .setRegionId(77)
                    .setLatitude(55.7536)
                    .setLongitude(37.6092)
                    .setParentCityId(0)
                    .setCityDepartment("MOZQ")
                    .build(),
                new City.Builder()
                    .setFiasId("a309e4ce-2f36-4106-b1ca-53e0f48a6d95")
                    .setFiasName("г Пермь")
                    .setAjsonId(56)
                    .setCityNameRus("Пермь")
                    .setPath("perm")
                    .setCityNameNorm("пермь")
                    .setGenitiveName("Перми")
                    .setRegionId(59)
                    .setLatitude(57.9972)
                    .setLongitude(56.2353)
                    .setParentCityId(0)
                    .setCityDepartment("NZPR")
                    .build(),
                new City.Builder()
                    .setFiasId("f66a00e6-179e-4de9-8ecb-78b0277c9f10")
                    .setFiasName("г Владимир")
                    .setAjsonId(89)
                    .setCityNameRus("Владимир")
                    .setPath("vladimir")
                    .setCityNameNorm("владимир")
                    .setGenitiveName("Владимира")
                    .setRegionId(33)
                    .setLatitude(56.1277)
                    .setLongitude(40.4076)
                    .setParentCityId(0)
                    .setCityDepartment("MOUX")
                    .build()))
        },
        {
            Map.of("fiasId", List.of("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")),
            new Cities(List.of(
                new City.Builder()
                    .setFiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")
                    .setFiasName("г Москва")
                    .setAjsonId(21)
                    .setCityNameRus("Москва")
                    .setPath("moscow")
                    .setCityNameNorm("москва")
                    .setGenitiveName("Москвы")
                    .setRegionId(77)
                    .setLatitude(55.7536)
                    .setLongitude(37.6092)
                    .setParentCityId(0)
                    .setCityDepartment("MOZQ")
                    .build()))},
        {
            Map.of("fiasId", List.of(
                "0c5b2444-70a0-4932-980c-b4dc0d3f02b5",
                "a309e4ce-2f36-4106-b1ca-53e0f48a6d95",
                "f66a00e6-179e-4de9-8ecb-78b0277c9f10")),
            new Cities(List.of(
                new City.Builder()
                    .setFiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")
                    .setFiasName("г Москва")
                    .setAjsonId(21)
                    .setCityNameRus("Москва")
                    .setPath("moscow")
                    .setCityNameNorm("москва")
                    .setGenitiveName("Москвы")
                    .setRegionId(77)
                    .setLatitude(55.7536)
                    .setLongitude(37.6092)
                    .setParentCityId(0)
                    .setCityDepartment("MOZQ")
                    .build(),
                new City.Builder()
                    .setFiasId("a309e4ce-2f36-4106-b1ca-53e0f48a6d95")
                    .setFiasName("г Пермь")
                    .setAjsonId(56)
                    .setCityNameRus("Пермь")
                    .setPath("perm")
                    .setCityNameNorm("пермь")
                    .setGenitiveName("Перми")
                    .setRegionId(59)
                    .setLatitude(57.9972)
                    .setLongitude(56.2353)
                    .setParentCityId(0)
                    .setCityDepartment("NZPR")
                    .build(),
                new City.Builder()
                    .setFiasId("f66a00e6-179e-4de9-8ecb-78b0277c9f10")
                    .setFiasName("г Владимир")
                    .setAjsonId(89)
                    .setCityNameRus("Владимир")
                    .setPath("vladimir")
                    .setCityNameNorm("владимир")
                    .setGenitiveName("Владимира")
                    .setRegionId(33)
                    .setLatitude(56.1277)
                    .setLongitude(40.4076)
                    .setParentCityId(0)
                    .setCityDepartment("MOUX")
                    .build()))},
        {
            Map.of("name", List.of("Москва")),
            new Cities(List.of(
                new City.Builder()
                    .setFiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")
                    .setFiasName("г Москва")
                    .setAjsonId(21)
                    .setCityNameRus("Москва")
                    .setPath("moscow")
                    .setCityNameNorm("москва")
                    .setGenitiveName("Москвы")
                    .setRegionId(77)
                    .setLatitude(55.7536)
                    .setLongitude(37.6092)
                    .setParentCityId(0)
                    .setCityDepartment("MOZQ")
                    .build()))},
        {
            Map.of("name", List.of("Москва", "Пермь", "Владимир")),
            new Cities(List.of(
                new City.Builder()
                    .setFiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")
                    .setFiasName("г Москва")
                    .setAjsonId(21)
                    .setCityNameRus("Москва")
                    .setPath("moscow")
                    .setCityNameNorm("москва")
                    .setGenitiveName("Москвы")
                    .setRegionId(77)
                    .setLatitude(55.7536)
                    .setLongitude(37.6092)
                    .setParentCityId(0)
                    .setCityDepartment("MOZQ")
                    .build(),
                new City.Builder()
                    .setFiasId("a309e4ce-2f36-4106-b1ca-53e0f48a6d95")
                    .setFiasName("г Пермь")
                    .setAjsonId(56)
                    .setCityNameRus("Пермь")
                    .setPath("perm")
                    .setCityNameNorm("пермь")
                    .setGenitiveName("Перми")
                    .setRegionId(59)
                    .setLatitude(57.9972)
                    .setLongitude(56.2353)
                    .setParentCityId(0)
                    .setCityDepartment("NZPR")
                    .build(),
                new City.Builder()
                    .setFiasId("f66a00e6-179e-4de9-8ecb-78b0277c9f10")
                    .setFiasName("г Владимир")
                    .setAjsonId(89)
                    .setCityNameRus("Владимир")
                    .setPath("vladimir")
                    .setCityNameNorm("владимир")
                    .setGenitiveName("Владимира")
                    .setRegionId(33)
                    .setLatitude(56.1277)
                    .setLongitude(40.4076)
                    .setParentCityId(0)
                    .setCityDepartment("MOUX")
                    .build()))
        },
        {
            Map.of("path", List.of("moscow")),
            new Cities(List.of(
                new City.Builder()
                    .setFiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")
                    .setFiasName("г Москва")
                    .setAjsonId(21)
                    .setCityNameRus("Москва")
                    .setPath("moscow")
                    .setCityNameNorm("москва")
                    .setGenitiveName("Москвы")
                    .setRegionId(77)
                    .setLatitude(55.7536)
                    .setLongitude(37.6092)
                    .setParentCityId(0)
                    .setCityDepartment("MOZQ")
                    .build()))},
        {
            Map.of("path", List.of("moscow", "perm", "vladimir")),
            new Cities(List.of(
                new City.Builder()
                    .setFiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")
                    .setFiasName("г Москва")
                    .setAjsonId(21)
                    .setCityNameRus("Москва")
                    .setPath("moscow")
                    .setCityNameNorm("москва")
                    .setGenitiveName("Москвы")
                    .setRegionId(77)
                    .setLatitude(55.7536)
                    .setLongitude(37.6092)
                    .setParentCityId(0)
                    .setCityDepartment("MOZQ")
                    .build(),
                new City.Builder()
                    .setFiasId("a309e4ce-2f36-4106-b1ca-53e0f48a6d95")
                    .setFiasName("г Пермь")
                    .setAjsonId(56)
                    .setCityNameRus("Пермь")
                    .setPath("perm")
                    .setCityNameNorm("пермь")
                    .setGenitiveName("Перми")
                    .setRegionId(59)
                    .setLatitude(57.9972)
                    .setLongitude(56.2353)
                    .setParentCityId(0)
                    .setCityDepartment("NZPR")
                    .build(),
                new City.Builder()
                    .setFiasId("f66a00e6-179e-4de9-8ecb-78b0277c9f10")
                    .setFiasName("г Владимир")
                    .setAjsonId(89)
                    .setCityNameRus("Владимир")
                    .setPath("vladimir")
                    .setCityNameNorm("владимир")
                    .setGenitiveName("Владимира")
                    .setRegionId(33)
                    .setLatitude(56.1277)
                    .setLongitude(40.4076)
                    .setParentCityId(0)
                    .setCityDepartment("MOUX")
                    .build()))
        }
    };
  }

  /**
   * Negative test data provider.
   *
   * @return Negative test data
   */
  @DataProvider
  public static Object[][] negativeDataProvider() {
    return new Object[][]{
        {Map.of("ajsonId", List.of())},
        {Map.of("ajsonId", List.of(0))},
        {Map.of("fiasId", List.of())},
        {Map.of("fiasId", List.of(""))},
        {Map.of("fiasId", List.of("0c5b2444-1111-4932-980c-b4dc0d3f02b5"))},
        {Map.of("name", List.of())},
        {Map.of("name", List.of(""))},
        {Map.of("name", List.of("3юкайка"))},
        {Map.of("path", List.of())},
        {Map.of("path", List.of(""))},
        {Map.of("path", List.of("QWERTY"))},
        {Map.of(
            "ajsonId", List.of(21),
            "fiasId", List.of("0c5b2444-70a0-4932-980c-b4dc0d3f02b5"),
            "name", List.of("Москва"),
            "path", List.of("MOZQ"))},
        {Map.of(
            "ajsonId", List.of(21, 56, 89),
            "fiasId", List.of(
                "0c5b2444-70a0-4932-980c-b4dc0d3f02b5",
                "a309e4ce-2f36-4106-b1ca-53e0f48a6d95",
                "f66a00e6-179e-4de9-8ecb-78b0277c9f10"),
            "name", List.of("Москва", "Пермь", "Тамбов"),
            "path", List.of("MOZQ", "NZPR", "MOUX"))}
    };
  }
}
