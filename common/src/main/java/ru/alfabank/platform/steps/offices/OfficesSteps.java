package ru.alfabank.platform.steps.offices;

import static io.restassured.RestAssured.given;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Comparator.naturalOrder;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.OKVKU;
import static ru.alfabank.platform.helpers.DataBaseHelper.getConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.TestNGException;
import ru.alfabank.platform.businessobjects.offices.CbCodeName;
import ru.alfabank.platform.businessobjects.offices.FileImportResponse;
import ru.alfabank.platform.businessobjects.offices.Kind.Code;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location.Builder;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Operation;
import ru.alfabank.platform.businessobjects.offices.ServiceCodeName;
import ru.alfabank.platform.steps.BaseSteps;
import ru.alfabank.platform.steps.geofacade.CitiesSteps;
import ru.alfabank.platform.steps.metro.MetroSteps;

public class OfficesSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(OfficesSteps.class);
  private static final File INIT_FILE = new File("src/test/resources/uws.json");

  public static List<Office> expectedOffices;

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
    ensureErrMessagesAreAbsent();
  }

  public void sendMessageToInQueue(final Offices offices) {
    LOGGER.info("Отправляю сообщение в очередь:\n" + describeBusinessObject(offices));
    describeResponse(LOGGER, given().spec(getOfficesQueueInSpec()).body(offices).post());
  }

  public void ensureErrMessageExistsAndCheckItsContent(final List<String> expectedErrorMessagesList) {
    LOGGER.info("Проверяю наличие сообщения в DLQ и его контент");
    final var response = checkErrQueueMessage(15);
    assertThat(response.getStatusCode()).as("Проверка наличия сообщения в DLQ").isEqualTo(SC_OK);
    final var errMsg = response.getBody().jsonPath().getString("errorMessages");
    assertThat(errMsg).contains(expectedErrorMessagesList);
  }

  public void cleanUpErrMessageQueue() {
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
          LOGGER.info(String.format("Проверка отсутствия офиса с 'pid=%s' и 'mnemonic=%s' в БД",
              office.getPid(), office.getMnemonic()));
          checkOfficeExistenceInDataBase(office, false);
        }
      }
    }
  }

  public void checkIfOfficesWereSaved(final Offices offices) {
    for (Office office : offices.getOffices()) {
      if (office != null) {
        LOGGER.info(String.format("Проверка нилачия офиса с 'pid=%s' и 'mnemonic=%s' в БД",
            office.getPid(), office.getMnemonic()));
        checkOfficeExistenceInDataBase(office, true);
      }
    }
  }

  public Office getOfficeFromDataBase(final Office office) {
    final var officesList = getOfficesListFromDataBase(office.getPid(), office.getMnemonic());
    assert officesList.size() == 1;
    return officesList.get(0);
  }

  public void checkOfficesMapping(final Offices expectedOfficesList) {
    for (Office expectedOffice : expectedOfficesList.getOffices()) {
      checkOfficeMapping(expectedOffice);
    }
  }

  public void checkOfficeMapping(Office office) {
    if (office.getStatusCB() != OKVKU) {
      final var actualOfficesList = getOfficesListFromDataBase(
          office.getPid(),
          office.getMnemonic());
      assertThat(actualOfficesList.size())
          .as("Проверка, что искомый офис сохранен и только в одном экземпляре")
          .isEqualTo(1);
      actualOfficesList.forEach(actualOffice -> actualOffice.equals(office));
    }
  }

  public void checkOfficeWasNotChanged(final Office office) {
    checkOfficesMapping(new Offices(null, List.of(office)));
  }

  public ArrayList<Code> getOfficeKindsCodesFromDataBase(final Office office) {
    return getKindCodesFromDataBase(office.getPid(), office.getMnemonic());
  }

  public void checkOfficesKindsMapping(final Offices offices) {
    LOGGER.info("Проверяю маппинг связей 'kinds' в БД");
    for (Office office : offices.getOffices()) {
      checkOfficeKindsMapping(office);
    }
  }

  public void checkOfficeKindsMapping(Office office) {
    final var actualKindCodesList = getKindCodesFromDataBase(office.getPid(), office.getMnemonic());
    final var expectedKindCodesSet = new HashSet<Code>();
    office.getKinds().forEach(kind -> {
      switch (kind) {
        case RETAIL_STANDARD_KIND -> expectedKindCodesSet.add(Code.RETAIL);
        case RETAIL_VIP_KIND -> {
          expectedKindCodesSet.add(Code.RETAIL);
          expectedKindCodesSet.add(Code.VIP);
        }
        case VIP_KIND -> {
          expectedKindCodesSet.add(Code.RETAIL);
          expectedKindCodesSet.add(Code.VIP);
          expectedKindCodesSet.add(Code.VIPMNGR);
          expectedKindCodesSet.add(Code.VIP_CLIENT);
        }
        case RETAIL_CIK_KIND -> {
          expectedKindCodesSet.add(Code.RETAIL);
          expectedKindCodesSet.add(Code.MORTGAGE);
        }
        case MMB_KIND -> expectedKindCodesSet.add(Code.SME);
        case SB_KIND, CIB_KIND -> expectedKindCodesSet.add(Code.CORPORATE);
        case RETAIL_ACLUB_KIND -> {
          expectedKindCodesSet.add(Code.VIP);
          expectedKindCodesSet.add(Code.ACLUB);
        }
      }
    });
    final var expectedCodesList = new ArrayList<>(expectedKindCodesSet);
    expectedCodesList.sort(naturalOrder());
    LOGGER.info(String.format(
        "Проверяю соответствие actual '%s' и expectedCodesList '%s'",
        actualKindCodesList,
        expectedKindCodesSet));
    assertThat(actualKindCodesList).isEqualTo(expectedCodesList);
  }

  public void checkKindsWereNotChanged(final Office office, final ArrayList<Code> expectedCodesList) {
    LOGGER.info("Проверяю маппинг связей kinds в БД");
    final var actual = getKindCodesFromDataBase(office.getPid(), office.getMnemonic());
    final var expected = new ArrayList<>(expectedCodesList);
    expected.sort(naturalOrder());
    LOGGER.info(String.format(
        "Проверяю соответствие actual '%s' и expected '%s'",
        actual,
        expectedCodesList));
    assertThat(actual).isEqualTo(expected);
  }

  public void checkOfficesServicesMapping(final Offices offices) {
    LOGGER.info("Проверяю маппинг связей 'services' в БД");
    for (Office office : offices.getOffices()) {
      checkOfficeServicesMapping(office);
    }
  }

  public void checkOfficeServicesMapping(Office office) {
    final ArrayList<ServiceCodeName> actualServiceCodesList = getServiceCodesListFromDataBase(office);
    final ArrayList<ServiceCodeName> expectedServiceCodesList = getServiceCodeNames(office);
    LOGGER.info(String.format(
        "Проверяю соответствие actual '%s' и expected '%s'",
        actualServiceCodesList, expectedServiceCodesList));
    assertThat(actualServiceCodesList).isEqualTo(expectedServiceCodesList);
  }

  public void checkServiceCodesListWasNotChanged(final Office office,
                                                 final ArrayList<ServiceCodeName> expectedServiceCodesList) {
    LOGGER.info("Проверяю маппинг связей services в БД");
    final ArrayList<ServiceCodeName> actualServiceCodesList = getServiceCodesListFromDataBase(office);
    LOGGER.info(String.format(
        "Проверяю соответствие actual '%s' и expected '%s'",
        actualServiceCodesList, expectedServiceCodesList));
    assertThat(actualServiceCodesList).isEqualTo(expectedServiceCodesList);
  }

  public ArrayList<ServiceCodeName> getServiceCodesListFromDataBase(final Office office) {
    return getCodesListFromDataBase(String.format("""
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
        office.getPid(), office.getMnemonic()));
  }

  private ArrayList<ServiceCodeName> getCodesListFromDataBase(final String query) {
    final var actualServiceCodesList = new ArrayList<ServiceCodeName>();
    try {
      LOGGER.debug("Ожидание обработки сообщения - 0 сек.");
      TimeUnit.SECONDS.sleep(0);
      LOGGER.info(String.format("Выполняю запрос в БД:\n'%s'\n", query));
      final ResultSet rs = getConnection().prepareStatement(query).executeQuery();
      while (rs.next()) {
        final var code = rs.getString("code");
        if (!code.equals("sme")) {
          LOGGER.info(String.format("Записываю code '%s'", code));
          actualServiceCodesList.add(ServiceCodeName.findValueByCode(code));
        } else {
          LOGGER.info(String.format("Code '%s' отброшен", code));
        }
      }
      actualServiceCodesList.sort(naturalOrder());
    } catch (SQLException | InterruptedException e) {
      LOGGER.error(e.toString());
      throw new TestNGException(e.toString());
    }
    return actualServiceCodesList;
  }

  public void checkOfficesListOfOperationsMapping(final Offices offices) {
    LOGGER.info("Проверяю маппинг связей ListOfOperations в БД");
    for (var office : offices.getOffices()) {
      checkOfficeListOfOperationsMapping(office);
    }
  }

  public void checkOfficeListOfOperationsMapping(Office office) {
    try {
      LOGGER.debug("Ожидание обработки сообщения - 0 сек.");
      TimeUnit.SECONDS.sleep(0);
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

  public void checkOfficesLocationMapping(final Offices offices) {
    for (Office office : offices.getOffices()) {
      checkOfficeLocationMapping(office);
    }
  }

  public void checkOfficeLocationMapping(Office office) {
    assert office.getLocations().size() > 0;
    final var mapOfActual = getMetroNameCityIdLocationFromDataBase(office);
    final var location = office.getLocations().stream()
        .findFirst().orElseThrow();
    final String expectedMetroName;
    final var isMsk = location.getLat() > 54.891836D && location.getLat() < 56.440495D
        && location.getLon() > 35.982771D && location.getLon() < 39.004186D;
    final var isSpb = location.getLat() > 59.801376D && location.getLat() < 60.119807D
        && location.getLon() > 30.025165D && location.getLon() < 30.563364D;
    if (isMsk || isSpb) {
      expectedMetroName = new MetroSteps()
        .getNearestMetroNameIn2km(location.getLat(), location.getLon());
    } else {
      expectedMetroName = null;
    }
    final var expectedCityId = new CitiesSteps()
        .getAJsonId(location);
    final var expectedLocation = new Builder()
        .setLat(location.getLat())
        .setLon(location.getLon())
        .setPostcode(location.getPostcode())
        .setFederalDistrict(location.getFederalDistrict())
        .setPlaceComment(location.getPlaceComment())
        .setAddress(composeAddress(office.getLocations()))
        .build();
    final Map<String, Object> mapOfExpected;
    if (expectedMetroName != null) {
      mapOfExpected = Map.of(
          "MetroName", expectedMetroName,
          "CityId", expectedCityId,
          "Location", expectedLocation);
    } else {
      mapOfExpected = Map.of(
          "CityId", expectedCityId,
          "Location", expectedLocation);
    }
    checkMetroNameCityIdLocation(mapOfActual, mapOfExpected);
  }

  public Map<String, Object> getMetroNameCityIdLocationFromDataBase(final Office office) {
    try {
      LOGGER.debug("Ожидание обработки сообщения - 0 сек.");
      TimeUnit.SECONDS.sleep(0);
      final var query = String.format("""
                 SELECT m.metroName,
                        d.CityID,d.Latitude,
                        d.Longitude,
                        d.postcode,
                        d.Region,
                        d.Address,
                        d.pathDescription
                   FROM alfabank_ru_old.Department d
              LEFT JOIN alfabank_ru_old.metro m on m.metroID = d.MetroID
                  WHERE d.MnemonicCode = '%s'
                    AND d.pid = '%s';""",
          office.getMnemonic(), office.getPid());
      final ResultSet rs;
      LOGGER.info(String.format("Выполняю запрос в БД: '%s'", query));
      rs = getConnection().prepareStatement(query).executeQuery();
      rs.next();
      final var metroName = rs.getString("metroName");
      final var actualLocation = new Location.Builder()
          .setLat(rs.getDouble("Latitude"))
          .setLon(rs.getDouble("Longitude"))
          .setPostcode(rs.getString("postcode"))
          .setFederalDistrict(rs.getString("Region"))
          .setAddress(rs.getString("Address"))
          .setPlaceComment(rs.getString("pathDescription"))
          .build();
      if (metroName != null) {
        return Map.of(
            "MetroName", metroName,
            "CityId", rs.getInt("CityID"),
            "Location", actualLocation);
      } else {
        return Map.of(
            "CityId", rs.getInt("CityID"),
            "Location", actualLocation);
      }
    } catch (SQLException | InterruptedException e) {
      LOGGER.error(e.toString());
      e.printStackTrace();
      throw new TestNGException(Arrays.toString(e.getStackTrace()));
    }
  }

  public void checkLocationWasNotChanged(final Office office, final Map<String, Object> mapOfExpected) {
    final Map<String, Object> mapOfActual = getMetroNameCityIdLocationFromDataBase(office);
    checkMetroNameCityIdLocation(mapOfExpected, mapOfActual);
  }

  public void checkOfficesChangeDateTimeMapping(final Offices offices) {
    LOGGER.info("Проверяю маппинг связей ChangeDateTime в БД");
    for (Office office : offices.getOffices()) {
      checkOfficeChangeDateTimeMapping(office);
    }
  }

  public void checkOfficeChangeDateTimeMapping(Office office) {
    final var expectedTimestamp = Instant.parse(office.getMetaInfo().getChangeDatetime());
    assertThat(getChangedDateTimeFromDataBase(office))
        .isCloseTo(expectedTimestamp, within(1, SECONDS));
  }

  public Instant getChangedDateTimeFromDataBase(final Office office) {
    try {
      LOGGER.debug("Ожидание обработки сообщения - 0 сек.");
      TimeUnit.SECONDS.sleep(0);
      final var query = String.format("""
              SELECT dept.changed
                FROM alfabank_ru_old.Department dept
               WHERE dept.pid = '%s'
                 AND dept.MnemonicCode = '%s';""",
          office.getPid(), office.getMnemonic());
      LOGGER.info(String.format("Выполняю запрос в БД:\n'%s'\n", query));
      final ResultSet rs = getConnection().prepareStatement(query).executeQuery();
      rs.next();
      final var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      return LocalDateTime.parse(rs.getString("changed"), dateTimeFormatter)
          .atZone(ZoneId.of("Z")).toInstant();
    } catch (SQLException | InterruptedException e) {
      LOGGER.error(e.toString());
      throw new TestNGException(e.toString());
    }
  }

  public void checkChangeDateTimeWasNotChanged(final Office office,
                                               final Instant expectedTimestamp) {
    LOGGER.info("Проверяю маппинг связей ChangeDateTime в БД");
    assertThat(getChangedDateTimeFromDataBase(office))
        .isCloseTo(expectedTimestamp, within(1, SECONDS));
  }

  private Response checkErrQueueMessage(int delay) {
    try {
      TimeUnit.SECONDS.sleep(delay);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage());
      throw new TestNGException(e.toString());
    }
    final var response = given().spec(getOfficesQueueErrSpec()).get();
    describeResponse(LOGGER, response);
    return response;
  }

  private String composeAddress(final List<Location> locationList) {
    Iterator<Location> iterator = locationList.listIterator();
    final var address = new StringBuilder();
    while (iterator.hasNext()) {
      Location location = iterator.next();
      if (location.getStreet() != null
          && !location.getStreet().isEmpty()
          && !address.toString().contains(location.getStreet())) {
        address.append(location.getStreet().trim());
      }
      if (location.getHouse() != null
          && !location.getHouse().isEmpty()
          && !address.toString().contains(", д. " + location.getHouse())) {
        address.append(", д. ").append(location.getHouse().trim());
      }
      if (location.getLiter() != null
          && !location.getLiter().isEmpty()
          && !address.toString().contains(location.getLiter().trim())) {
        address.append(location.getLiter());
      }
      if (location.getBlock() != null
          && !location.getBlock().isEmpty()
          && !address.toString().contains(", к. " + location.getBlock())) {
        address.append(", к. ").append(location.getBlock().trim());
      }
      if (location.getBuilding() != null
          && !location.getBuilding().isEmpty()
          && !address.toString().contains(", стр. " + location.getBuilding())) {
        address.append(", стр. ").append(location.getBuilding().trim());
      }
      if (location.getRoom() != null
          && !location.getRoom().isEmpty()
          && !address.toString().contains(", пом. " + location.getRoom())) {
        address.append(", пом. ").append(location.getRoom().trim());
      }
      if (iterator.hasNext()) {
        address.append("/");
      }
    }
    return address.toString().replaceAll("/$", "");
  }

  private void ensureErrMessagesAreAbsent() {
    LOGGER.info("Проверяю отсутствие сообщения в DLQ");
    assertThat(checkErrQueueMessage(15).getStatusCode()).isEqualTo(SC_NO_CONTENT);
  }

  private void ensureErrMessagesAreAbsent(int delay) {
    LOGGER.info("Проверяю отсутствие сообщения в DLQ");
    assertThat(checkErrQueueMessage(delay).getStatusCode())
        .isEqualTo(SC_NO_CONTENT);
  }

  private ArrayList<Code> getKindCodesFromDataBase(final String pid, String mnemonic) {
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
          pid, mnemonic);
      LOGGER.info(String.format("Выполняю запрос в БД:\n'%s'\n", query));
      final ResultSet rs = getConnection().prepareStatement(query).executeQuery();
      final var actualCodesList = new ArrayList<Code>();
      while (rs.next()) {
        final var code = rs.getString("code");
        LOGGER.debug(String.format("Записываю code '%s'", code));
        actualCodesList.add(Code.findValue(code));
      }
      actualCodesList.sort(naturalOrder());
      return actualCodesList;
    } catch (SQLException | InterruptedException e) {
      LOGGER.error(e.toString());
      throw new TestNGException(e.toString());
    }
  }

  private ArrayList<Office> getOfficesListFromDataBase(final String pid,
                                                       final String mnemonic) {
    try {
      TimeUnit.SECONDS.sleep(5);
      final var query = String.format("""
              SELECT *
                FROM alfabank_ru_old.Department
               WHERE pid = '%s'
                 AND MnemonicCode = '%s';""",
          pid,
          mnemonic);
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
      return actualOfficesList;
    } catch (SQLException | InterruptedException e) {
      LOGGER.error(e.toString());
      throw new TestNGException(e.toString());
    }
  }

  private ArrayList<ServiceCodeName> getServiceCodeNames(final Office office) {
    return office.getServices().stream()
        .filter(service -> !service.getCode().equalsIgnoreCase("err"))
        .map(service -> ServiceCodeName.findValueByCode(service.getCode()))
        .sorted(naturalOrder())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private void checkMetroNameCityIdLocation(final Map<String, Object> mapOfActual,
                                            final Map<String, Object> mapOfExpected) {
    LOGGER.info(String.format("Проверяю маппинг станции метро: actual '%s' и expected '%s'",
        mapOfActual.getOrDefault("MetroName", null),
        mapOfExpected.getOrDefault("MetroName", null)));
    final var softly = new SoftAssertions();
    softly.assertThat((String) mapOfActual.getOrDefault("MetroName", null))
        .as("Проверка маппинга станции метро")
        .isEqualTo(mapOfExpected.getOrDefault("MetroName", null));
    LOGGER.info(String.format("Проверяю маппинг города: actual '%s' и expected '%s'",
        mapOfActual.get("CityId"), mapOfExpected.get("CityId")));
    softly.assertThat((Integer) mapOfActual.get("CityId"))
        .as("Проверка маппинга города")
        .isEqualTo(mapOfExpected.get("CityId"));
    ((Location) mapOfActual.get("Location")).equals((Location) mapOfExpected.get("Location"), softly);
    softly.assertAll();
  }

  private void checkOfficeExistenceInDataBase(final Office office,
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
        office.getMnemonic(),
        office.getPid()
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

  public void importFileAssumingSuccess() {
    final var importFileResponse = importFile();
    assertThat(importFileResponse.getStatusCode()).as("Сервис ответил ошибкой").isEqualTo(SC_OK);
    final var notImportedOfficesList = new ArrayList<>();
    importFileResponse.as(FileImportResponse.class).getErrorDetails().forEach(office -> {
      if (office.getMessages().stream().anyMatch(message -> !message.contains(OKVKU.getCode()))) {
        notImportedOfficesList.add(office.getIdMasterSystem());
      }
    });
    assertThat(notImportedOfficesList.size()).as("Есть незагруженные отделения").isEqualTo(0);
    try {
      expectedOffices =
          new ObjectMapper().readValue(INIT_FILE, Offices.class).getOffices().stream().filter(o ->
              o.getStatusCB() != OKVKU).collect(Collectors.toList());
    } catch (IOException e) {
      LOGGER.error(e.getStackTrace());
    }
  }

  private Response importFile() {
    LOGGER.info("Отправляю сообщение с файлом:\n" + describeBusinessObject(INIT_FILE));
    final var response = given().spec(getOfficesImportSpec()).multiPart(INIT_FILE).post();
    describeResponse(LOGGER, response);
    return response;
  }
}
