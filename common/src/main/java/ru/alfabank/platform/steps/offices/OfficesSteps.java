package ru.alfabank.platform.steps.offices;

import static io.restassured.RestAssured.given;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Comparator.*;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.helpers.DataBaseHelper.getConnection;

import io.restassured.response.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.TestNGException;
import ru.alfabank.platform.businessobjects.cities.Cities.City;
import ru.alfabank.platform.businessobjects.offices.CbCodeName;
import ru.alfabank.platform.businessobjects.offices.Kind.Code;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Operation;
import ru.alfabank.platform.businessobjects.offices.ServiceCodeName;
import ru.alfabank.platform.steps.BaseSteps;
import ru.alfabank.platform.steps.geofacade.CitiesSteps;
import ru.alfabank.platform.steps.metro.MetroSteps;

public class OfficesSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(OfficesSteps.class);

  public void cleanUpDataBase() {
    final var query1 = """
        DELETE
          FROM alfabank_ru_old.DepartmentToDepartmentFeature
          WHERE DepartmentID > 1637;""";
    final var query2 = """
        DELETE
          FROM alfabank_ru_old.Department
          WHERE DepartmentID > 1637;""";
    try {
      getConnection().prepareStatement(query1).executeUpdate();
      getConnection().prepareStatement(query2).executeUpdate();
    } catch (SQLException e) {
      LOGGER.error(e);
      throw new TestNGException(e.toString());
    }
  }

  public void sendMessageToInQueueAssumingSuccess(final Offices offices) {
    sendMessageToInQueue(offices);
    ensureErrMessageIsAbsent();
  }

  private void ensureErrMessageIsAbsent() {
    LOGGER.info("Проверяю отсутствие сообщения в DLQ");
    assertThat(checkErrQueueMessage().getStatusCode())
        .as("Проверка отсутствия сообщения в DLQ")
        .isEqualTo(SC_NO_CONTENT);
  }

  public void sendMessageToInQueue(final Offices offices) {
    LOGGER.info("Отправляю сообщение в очередь:\n" + describeBusinessObject(offices));
    given().spec(getOfficesQueueInSpec()).body(offices)
        .when().post().then().statusCode(SC_OK);
  }

  public void ensureErrMessageExistsAndCheckItsContent(final List<String> expectedErrorMessagesList) {
    LOGGER.info("Проверяю наличие сообщения в DLQ и его контент");
    final var response = checkErrQueueMessage();
    assertThat(response.getStatusCode()).as("Проверка наличия сообщения в DLQ").isEqualTo(SC_OK);
    final var errMsg = response.getBody().jsonPath().getString("errorMessages");
    assertThat(errMsg).contains(expectedErrorMessagesList);
  }

  private Response checkErrQueueMessage() {
    try {
      TimeUnit.SECONDS.sleep(15);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage());
      throw new TestNGException(e.toString());
    }
    final var response = given().spec(getOfficesQueueErrSpec()).get();
    describeResponse(LOGGER, response);
    return response;
  }

  public void clearErrMessageQueue() {
    while (given().spec(getOfficesQueueErrSpec()).get().getStatusCode() != SC_NO_CONTENT) {
      assert deleteErrQueueMessage().statusCode() == SC_OK;
    }
  }

  private Response deleteErrQueueMessage() {
    final var response = given().spec(getOfficesQueueErrSpec()).delete();
    describeResponse(LOGGER, response);
    return response;
  }

  public void ensureIfOfficesWereNotSaved(final Offices offices) {
    if (offices.getOffices() != null) {
      for (Office office : offices.getOffices()) {
        if (office != null) {
          checkOfficeExistenceInDataBase(office, false);
        }
      }
    }
  }

  public void checkIfOfficesWereSaved(Offices offices) {
    for (Office office : offices.getOffices()) {
      if (office != null) {
        LOGGER.info("Проверка сохранения офиса в БД");
        checkOfficeExistenceInDataBase(office, true);
      }
    }
  }

  private void checkOfficeExistenceInDataBase(final Office expectedOffice,
                                              final boolean shouldBeSaved) {
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage());
      throw new TestNGException(e.toString());
    }
    final var query = String.format("""
        SELECT COUNT(*)
          FROM alfabank_ru_old.Department
         WHERE MnemonicCode = '%s'
           AND pid = '%s';""",
        expectedOffice.getMnemonic(),
        expectedOffice.getPid()
    );
    try {
      final ResultSet rs;
      rs = getConnection().prepareStatement(query).executeQuery();
      rs.next();
      final var count = rs.getInt(1);
      if (shouldBeSaved && count == 0) {
        deleteErrQueueMessage();
      }
      assertThat(count).isEqualTo(shouldBeSaved ? 1 : 0);
    } catch (SQLException e) {
      LOGGER.error(e.toString());
      throw new TestNGException(e.toString());
    }
  }

  public void checkOfficeMapping(final Offices offices) {
    for (Office expectedOffice : offices.getOffices()) {
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        LOGGER.error(e);
        throw new TestNGException(e.toString());
      }
      final var query = String.format("""
            SELECT * 
              FROM alfabank_ru_old.Department d
              WHERE d.MnemonicCode = '%s';""",
          expectedOffice.getMnemonic());
      try {
        final ResultSet rs;
        LOGGER.info(String.format("Выполняю запрос в БД: '%s'", query));
        rs = getConnection().prepareStatement(query).executeQuery();
        final var actualOfficesList = new ArrayList<Office>();
        while (rs.next()) {
          final var officeFromDataBase = new Office.Builder()
              .setPid(rs.getString("pid"))
              .setMnemonic(rs.getString("MnemonicCode"))
              .setPathUrl(rs.getString("Path"))
              .setTitle(rs.getString("Title"))
              .setDescription(rs.getString("Description"))
              .setClose(rs.getInt("Enabled") == 0)
              .setShortNameCB(rs.getString("IBTitle"))
              .setOpenDate(rs.getString("DateStart"))
              .setPhoneCB(rs.getString("Phone"))
              .setStatusCB(CbCodeName.findValueByTypeId(rs.getInt("TypeID")))
              .setBranchID(rs.getInt("BranchID"))
              .setUseInCosmo(rs.getInt("useInCosmo"))
              .setIsOnReconstruction(rs.getInt("isOnReconstruction"))
              .setParking(rs.getInt("parking"))
              .setCityId(rs.getInt("CityID"))
              .setMetroId(rs.getInt("MetroID"))
              .build();
          actualOfficesList.add(officeFromDataBase);
        }
        assertThat(actualOfficesList.size())
            .as("Проверка, что искомый офис сохранен и только в одном экземпляре")
            .isEqualTo(1);
        actualOfficesList.forEach(office -> office.equals(expectedOffice));
      } catch (SQLException e) {
        LOGGER.error(e.toString());
        throw new TestNGException(e.toString());
      }
    }
  }

  public void checkKindsMapping(final Offices offices,
                                final List<Code> expectedCodesList) {
    LOGGER.info("Проверяю маппинг связей kinds в БД");
    for (Office office : offices.getOffices()) {
      try {
        LOGGER.info("Ожидание обработки сообщения - 5 сек.");
        TimeUnit.SECONDS.sleep(5);
        final var query = String.format("""
                SELECT dept.DepartmentID,
                       feat.code
                  FROM alfabank_ru_old.Department dept
                  JOIN alfabank_ru_old.DepartmentToDepartmentFeature d2df
                    ON dept.DepartmentID = d2df.DepartmentID
                  JOIN alfabank_ru_old.DepartmentFeature feat
                    ON d2df.DepartmentFeatureID = feat.DepartmentFeatureID
                 WHERE dept.pid = '%s'
                   AND dept.MnemonicCode = '%s';""",
            office.getPid(), office.getMnemonic());
        LOGGER.info(String.format("Выполняю запрос в БД:\n'%s'\n", query));
        final ResultSet rs = getConnection().prepareStatement(query).executeQuery();
        final var actualCodesList = new ArrayList<Code>();
        while (rs.next()) {
          final var code = rs.getString("code");
          LOGGER.info(String.format("Записываю code '%s'", code));
          actualCodesList.add(Code.findValue(code));
        }
        LOGGER.info(String.format(
            "Проверяю соответствие actual '%s' и expected '%s'",
            actualCodesList,
            expectedCodesList));
        actualCodesList.sort(naturalOrder());
        final var expected = new ArrayList<>(expectedCodesList);
        expected.sort(naturalOrder());
        assertThat(actualCodesList).isEqualTo(expected);
      } catch (SQLException | InterruptedException e) {
        LOGGER.error(e.toString());
        throw new TestNGException(e.toString());
      }
    }
  }

  public void checkServicesMapping(final Offices offices) {
    LOGGER.info("Проверяю маппинг связей services в БД");
    for (Office office : offices.getOffices()) {
      try {
        LOGGER.info("Ожидание обработки сообщения - 5 сек.");
        TimeUnit.SECONDS.sleep(5);
        final var query = String.format("""
                SELECT dept.DepartmentID,
                       feat.code,
                       feat.Description
                  FROM alfabank_ru_old.Department dept
                  JOIN alfabank_ru_old.DepartmentToDepartmentFeature d2df
                    ON dept.DepartmentID = d2df.DepartmentID
                  JOIN alfabank_ru_old.DepartmentFeature feat
                    ON d2df.DepartmentFeatureID = feat.DepartmentFeatureID
                 WHERE dept.pid = '%s'
                   AND dept.MnemonicCode = '%s';""",
            office.getPid(), office.getMnemonic());
        LOGGER.info(String.format("Выполняю запрос в БД:\n'%s'\n", query));
        final ResultSet rs = getConnection().prepareStatement(query).executeQuery();
        final var actualServicesListByCodes = new ArrayList<ServiceCodeName>();
        while (rs.next()) {
          final var code = rs.getString("code");
          LOGGER.info(String.format("Записываю code '%s'", code));
          if (!code.equals("sme")) {
            actualServicesListByCodes.add(ServiceCodeName.findValueByCode(code));
          }
        }
        actualServicesListByCodes.sort(naturalOrder());
        final var expectedServicesListByCodes = office.getServices().stream()
            .filter(service -> !service.getCode().equalsIgnoreCase("err"))
            .map(service -> ServiceCodeName.findValueByCode(service.getCode()))
            .sorted(naturalOrder())
            .collect(Collectors.toCollection(ArrayList::new));
        LOGGER.info(String.format(
            "Проверяю соответствие actual '%s' и expected '%s'",
            actualServicesListByCodes,
            expectedServicesListByCodes));
        assertThat(actualServicesListByCodes).isEqualTo(expectedServicesListByCodes);
      } catch (SQLException | InterruptedException e) {
        LOGGER.error(e.toString());
        throw new TestNGException(e.toString());
      }
    }
  }

  public void checkListOfOperationsMapping(final Offices offices) {
    LOGGER.info("Проверяю маппинг связей ListOfOperations в БД");
    for (Office office : offices.getOffices()) {
      try {
        LOGGER.info("Ожидание обработки сообщения - 5 сек.");
        TimeUnit.SECONDS.sleep(5);
        final var query = String.format("""
                SELECT dept.DepartmentID,
                       feat.abbr,
                       feat.title,
                       feat.title_html
                  FROM alfabank_ru_old.Department dept
                  JOIN alfabank_ru_old.DepartmentToDepartmentIBFeature d2dIbF
                    ON dept.DepartmentID = d2dIbF.department_id
                  JOIN alfabank_ru_old.DepartmentIBFeature feat
                    ON d2dIbF.feature_id = feat.id
                 WHERE dept.pid = '%s'
                   AND dept.MnemonicCode = '%s';""",
            office.getPid(), office.getMnemonic());
        LOGGER.info(String.format("Выполняю запрос в БД:\n'%s'\n", query));
        final ResultSet rs = getConnection().prepareStatement(query).executeQuery();
        final var actualCodesList = new ArrayList<String>();
        final var actualNamesList = new ArrayList<String>();
        final var actualNamesHtmlList = new ArrayList<String>();
        while (rs.next()) {
          actualCodesList.add(rs.getString("abbr"));
          actualNamesList.add(rs.getString("title"));
          actualNamesHtmlList.add(rs.getString("title_html"));
        }
        actualCodesList.sort(naturalOrder());
        final List<String> expectedCodesList;
        final List<String> expectedNamesList;
        if (office.getListOfOperations() != null) {
          expectedCodesList = office.getListOfOperations().stream()
              .filter(operation -> !operation.getCode().contains("random"))
              .map(Operation::getCode).sorted(naturalOrder()).collect(Collectors.toList());
          expectedNamesList = office.getListOfOperations().stream()
              .filter(operation -> !operation.getName().contains("random"))
              .map(Operation::getName).sorted(naturalOrder()).collect(Collectors.toList());
        } else {
          expectedCodesList = List.of();
          expectedNamesList = List.of();
        }
        LOGGER.info(String.format(
            "Проверяю соответствие actualCodesList '%s' и expectedCodesList '%s'",
            actualCodesList, expectedCodesList));
        assertThat(actualCodesList).isEqualTo(expectedCodesList);
        actualNamesList.sort(naturalOrder());
        LOGGER.info(String.format(
            "Проверяю соответствие actualNamesList '%s' и expectedNamesList '%s'",
            actualNamesList, expectedNamesList));
        assertThat(actualNamesList).isEqualTo(expectedNamesList);
        LOGGER.info(String.format(
            "Проверяю соответствие actualNamesHtmlList '%s' и expectedNamesList '%s'",
            actualNamesHtmlList, expectedNamesList));
        actualNamesHtmlList.sort(naturalOrder());
        assertThat(actualNamesHtmlList).isEqualTo(expectedNamesList);
      } catch (SQLException | InterruptedException e) {
        LOGGER.error(e.toString());
        throw new TestNGException(e.toString());
      }
    }
  }

  public void checkLocationMapping(final Offices offices) {
    for (Office office : offices.getOffices()) {
      assert office.getLocations().size() > 0;
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        LOGGER.error(e);
        throw new TestNGException(e.toString());
      }
      final var query = String.format("""
              SELECT m.metroName,
                     d.CityID,d.Latitude,
                     d.Longitude,
                     d.postcode,
                     d.Region,
                     d.Address,
                     d.pathDescription
                FROM alfabank_ru_old.Department d
                JOIN alfabank_ru_old.metro m on m.metroID = d.MetroID
               WHERE d.MnemonicCode = '%s'
                 AND d.pid = '%s';""",
          office.getMnemonic(), office.getPid());
      try {
        final ResultSet rs;
        LOGGER.info(String.format("Выполняю запрос в БД: '%s'", query));
        rs = getConnection().prepareStatement(query).executeQuery();
        rs.next();
        final var actualLocation = new Office.Location.Builder()
            .setLat(rs.getDouble("Latitude"))
            .setLon(rs.getDouble("Longitude"))
            .setPostcode(rs.getString("postcode"))
            .setFederalDistrict(rs.getString("Region"))
            .setAddress(rs.getString("Address"))
            .setPlaceComment(rs.getString("pathDescription"))
            .build();
        final var location = office.getLocations().stream()
            .max(comparing(Location::hashCode)).orElseThrow();
        final var expectedLocation = new Location.Builder()
            .setLat(location.getLat())
            .setLon(location.getLon())
            .setPostcode(location.getPostcode())
            .setFederalDistrict(location.getFederalDistrict())
            .setPlaceComment(location.getPlaceComment())
            .setAddress(composeAddress(office.getLocations()))
            .build();
        List<Integer> listOfCitiesIds = new CitiesSteps()
            .getCityListWithMetaByName(List.of(location.getCity()))
            .stream().map(City::getAjsonId).collect(Collectors.toList());
        final var metroName = new MetroSteps()
            .getNearestMetroNameIn2km(location.getLat(), location.getLon());
        assertThat(metroName)
            .as("Проверка маппинга ID станции метро")
            .isEqualTo(rs.getString("metroName"));
        assertThat(listOfCitiesIds)
            .as("Проверка маппинга ID города")
            .contains(rs.getInt("CityID"));
        actualLocation.equals(expectedLocation);
      } catch (SQLException e) {
        LOGGER.error(e.toString());
        e.printStackTrace();
        throw new TestNGException(Arrays.toString(e.getStackTrace()));
      }
    }
  }

  private String composeAddress(final List<Location> locationList) {
    Iterator<Location> iterator = locationList.listIterator();
    final var address = new StringBuilder();
    while (iterator.hasNext()) {
      Location location = iterator.next();
      if (location.getStreet() != null
          && !location.getStreet().isEmpty()
          && !address.toString().contains(location.getStreet())) {
        address.append(location.getStreet());
      }
      if (location.getHouse() != null
          && !location.getHouse().isEmpty()
          && !address.toString().contains(", д. " + location.getHouse())) {
        address.append(", д. ").append(location.getHouse());
      }
      if (location.getLiter() != null
          && !location.getLiter().isEmpty()
          && !address.toString().contains(location.getLiter())) {
        address.append(location.getLiter());
      }
      if (location.getBlock() != null
          && !location.getBlock().isEmpty()
          && !address.toString().contains(", к. " + location.getBlock())) {
        address.append(", к. ").append(location.getBlock());
      }
      if (location.getBuilding() != null
          && !location.getBuilding().isEmpty()
          && !address.toString().contains(", стр. " + location.getBuilding())) {
        address.append(", стр. ").append(location.getBuilding());
      }
      if (location.getRoom() != null
          && !location.getRoom().isEmpty()
          && !address.toString().contains(", пом. " + location.getRoom())) {
        address.append(", пом. ").append(location.getRoom());
      }
      if (iterator.hasNext()) {
        address.append("/");
      }
    }
    return address.toString();
  }

  public void checkChangeDateTimeMapping(final Offices offices) {
    LOGGER.info("Проверяю маппинг связей ChangeDateTime в БД");
    for (Office office : offices.getOffices()) {
      try {
        LOGGER.info("Ожидание обработки сообщения - 5 сек.");
        TimeUnit.SECONDS.sleep(5);
        final var query = String.format("""
                SELECT dept.changed
                  FROM alfabank_ru_old.Department dept
                 WHERE dept.pid = '%s'
                   AND dept.MnemonicCode = '%s';""",
            office.getPid(), office.getMnemonic());
        LOGGER.info(String.format("Выполняю запрос в БД:\n'%s'\n", query));
        final ResultSet rs = getConnection().prepareStatement(query).executeQuery();
        String actual = "";
        while (rs.next()) {
          actual = rs.getString("changed");
        }
        final var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        assertThat(LocalDateTime.parse(actual, dateTimeFormatter).atZone(ZoneId.of("Z")).toInstant())
            .isCloseTo(Instant.parse(office.getMetaInfo().getChangeDatetime()), within(1, SECONDS));
      } catch (SQLException | InterruptedException e) {
        LOGGER.error(e.toString());
        throw new TestNGException(e.toString());
      }
    }
  }
}
