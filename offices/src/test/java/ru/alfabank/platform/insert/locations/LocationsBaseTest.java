package ru.alfabank.platform.insert.locations;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location;

public class LocationsBaseTest extends BaseTest {

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] locationsPositiveTestDataProvider() {
    return new Object[][]{
        {
            "smoke",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(BASE_LOCATION)
                        ).build()
                )
            )
        },
        {
            "Max количество полей с max заполнением",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .setFiasId("8866e114-2bea-406a-86cb-bbe5f1506063")
                                .setKladrId("78000000000071000")
                                .setLat(59.921593)
                                .setLon(30.355319)
                                .setPostcode(randomNumeric(15))
                                .setFederalDistrict(randomAlphanumeric(255))
                                .setSubjectOfFederation(randomAlphanumeric(300))
                                .setCity("Санкт-Петербург")
                                .setStreet(randomAlphanumeric(100))
                                .setHouse(randomAlphanumeric(100))
                                .setBlock(randomAlphanumeric(100))
                                .setBuilding(randomAlphanumeric(100))
                                .setLiter(randomAlphanumeric(100))
                                .setRoom(randomAlphanumeric(100))
                                .setPlaceComment(randomAlphanumeric(300))
                                .build()
                            )
                        ).build()
                )
            )
        },
        {
            "> 1 metro stations",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .setFiasId("ed66d85b-434a-481e-86bc-579901da4f8b")
                                .setKladrId("7700000000071640057")
                                .setLat(55.7472054)
                                .setLon(37.6229693)
                                .setPostcode("115035")
                                .setFederalDistrict("Центральный")
                                .setSubjectOfFederation("г Москва")
                                .setCity("Москва")
                                .setStreet("Софийская наб")
                                .build()
                            )
                        ).build()
                )
            )
        },
        {
            "no metro stations",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .setFiasId("4043b5bd-b5f2-4062-a6a7-7d8b5f0da21b")
                                .setKladrId("5900000500000280001")
                                .setLat(50.58858)
                                .setLon(137.04987)
                                .setPostcode("681007")
                                .setFederalDistrict("Дальневосточный")
                                .setSubjectOfFederation("Хабаровский край")
                                .setCity("Комсомольск-на-Амуре")
                                .setStreet("1-я Дальневосточная")
                                .build()
                            )
                        ).build()
                )
            )
        },
        {
            "Merge двух адресов с уникальными данными",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .setFiasId("a4cf8692-14d4-4218-a07a-49807d3cd7ad")
                                .setKladrId("5900000500000280001")
                                .setLat(58.8418156)
                                .setLon(57.5491844)
                                .setPostcode("618250")
                                .setFederalDistrict("Приволжский")
                                .setSubjectOfFederation("Пермский край")
                                .setCity("Губаха")
                                .setStreet("пр-кт Ленина")
                                .setHouse("1")
                                .setBlock("3")
                                .setBuilding("5")
                                .setLiter("А")
                                .setRoom("223")
                                .setPlaceComment(randomAlphanumeric(10))
                                .build(),
                            new Location.Builder()
                                .setFiasId("a4cf8692-14d4-4218-a07a-49807d3cd7ad")
                                .setKladrId("5900000500000280001")
                                .setLat(58.8418156)
                                .setLon(57.5491844)
                                .setPostcode("618250")
                                .setFederalDistrict("Приволжский")
                                .setSubjectOfFederation("Пермский край")
                                .setCity("Губаха")
                                .setStreet("пр-кт Андропова")
                                .setHouse("2")
                                .setBlock("4")
                                .setBuilding("7")
                                .setLiter("Б")
                                .setRoom("332")
                                .setPlaceComment(randomAlphanumeric(10))
                                .build()
                            )
                        ).build()
                )
            )
        },
        {
            "Merge двух адресов с повторяющимися данными",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .setFiasId("2c248779-9d42-475b-addb-063bfdad8dac")
                                .setKladrId("7700000000071640057")
                                .setLat(59.049840)
                                .setLon(57.646558)
                                .setPostcode("618351")
                                .setFederalDistrict("Приволжский")
                                .setSubjectOfFederation("Пермский край")
                                .setCity("Кизел")
                                .setStreet("ул Борцов")
                                .setHouse("3")
                                .setBlock("1")
                                .setBuilding("4")
                                .setLiter("Б")
                                .setRoom("Н12")
                                .setPlaceComment("randomAlphanumeric(10)")
                                .build(),
                            new Location.Builder()
                                .setFiasId("8a3f8666-9121-419b-9bf7-55b5d3381349")
                                .setKladrId("7700000000071640057")
                                .setLat(59.049840)
                                .setLon(57.646558)
                                .setPostcode("618350")
                                .setFederalDistrict("Приволжский")
                                .setSubjectOfFederation("Пермский край")
                                .setCity("Кизел")
                                .setStreet("ул Борцов")
                                .setHouse("3")
                                .setBlock("1")
                                .setBuilding("4")
                                .setLiter("Б")
                                .setRoom("Н12")
                                .setPlaceComment("randomAlphanumeric(10)")
                                .build()
                            )
                        ).build()
                )
            )
        }
    };
  }

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] locationsNegativeTestDataProvider() {
    return new Object[][]{
        {
            "locations.fiasId == format",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setFiasId(randomAlphanumeric(36))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("UUID has to be represented by standard 36-char representation")
        },
        {
            "locations.fiasId.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setFiasId("")
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("fiasId", "must not be null")
        },
        {
            "locations.fiasId == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setFiasId(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("fiasId", "must not be null")
        },
        {
            "locations.kladrId non digits",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setKladrId(randomAlphanumeric(19))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("kladrId", "должно содержать только цифры")
        },
        {
            "locations.kladrId.length > 19",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setKladrId(randomNumeric(20))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("kladrId", "size must be between 1 and 19")
        },
        {
            "locations.kladrId.length < 1",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setKladrId("")
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("kladrId", "must not be blank")
        },
        {
            "locations.kladrId == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setKladrId(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("kladrId", "must not be blank")
        },
        {
            "locations.lat == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setLat(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("point.lat", "must not be null")
        },
        {
            "locations.lon == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setLon(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("point.lon", "must not be null")
        },
        {
            "locations.postcode == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setPostcode(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("postcode", "must not be blank")
        },
        {
            "locations.postcode.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setPostcode("")
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("postcode", "must not be blank")
        },
        {
            "locations.postcode.length < 6",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setPostcode(randomNumeric(5))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("postcode", "size must be between 6 and 15")
        },
        {
            "locations.postcode.length > 15",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setPostcode(randomNumeric(16))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("postcode", "size must be between 6 and 15")
        },
        {
            "locations.postcode == non digits",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setPostcode(randomAlphabetic(6))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("postcode", "должно содержать только цифры")
        },
        {
            "locations.federalDistrict.length > 255",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setFederalDistrict(randomAlphanumeric(256))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("federalDistrict", "size must be between 1 and 255")
        },
        {
            "locations.federalDistrict == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setFederalDistrict(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("federalDistrict", "must not be blank")
        },
        {
            "locations.federalDistrict.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setFederalDistrict("")
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("federalDistrict", "must not be blank")
        },
        {
            "locations.subjectOfFederation.length > 300",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setSubjectOfFederation(randomAlphanumeric(301))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("subjectOfFederation", "size must be between 1 and 300")
        },
        {
            "locations.subjectOfFederation.length < 1",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setSubjectOfFederation("")
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("subjectOfFederation", "must not be blank")
        },
        {
            "locations.subjectOfFederation == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setSubjectOfFederation(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("subjectOfFederation", "must not be blank")
        },
        {
            "locations.city.length > 300",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setCity(randomAlphanumeric(301))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("city", "size must be between 1 and 100")
        },
        {
            "locations.city == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setCity(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("city", "must not be blank")
        },
        {
            "locations.city.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setCity("")
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("city", "must not be blank")
        },
        {
            "locations.street == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setStreet(null)
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("street", "must not be blank")
        },
        {
            "locations.street.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setStreet("")
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("street", "must not be blank")
        },
        {
            "locations.street.length > 100",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setStreet(randomAlphanumeric(101))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("street", "size must be between 1 and 100")
        },
        {
            "locations.house.length > 100",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setHouse(randomAlphanumeric(101))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("house", "size must be between 0 and 100")
        },
        {
            "locations.block.length > 100",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setBlock(randomAlphanumeric(101))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("block", "size must be between 0 and 100")
        },
        {
            "locations.building.length > 100",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setBuilding(randomAlphanumeric(101))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("building", "size must be between 0 and 100")
        },
        {
            "locations.liter.length > 100",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setLiter(randomAlphanumeric(101))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("liter", "size must be between 0 and 100")
        },
        {
            "locations.room.length > 100",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setRoom(randomAlphanumeric(101))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("room", "size must be between 0 and 100")
        },
        {
            "locations.placeComment.length > 300",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of(
                            new Location.Builder()
                                .using(BASE_LOCATION)
                                .setPlaceComment(randomAlphanumeric(301))
                                .build()
                            )
                        ).build()
                )
            ),
            List.of("placeComment", "size must be between 0 and 300")
        },
    };
  }
}
