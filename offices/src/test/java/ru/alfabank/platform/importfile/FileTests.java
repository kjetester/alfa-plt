package ru.alfabank.platform.importfile;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.businessobjects.offices.Kind.EMPTY_KIND;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.CbCodeName;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;

public class FileTests {

  private static Offices offices;
  private static List<Office> officesList;

  /**
   * BeforeTest.
   *
   * @throws IOException IOException
   */
  @BeforeTest
  public static void beforeTest() throws IOException {
    offices = new ObjectMapper().readValue(new File(
            "/Users/juliankolodzey/Projects/alfa/platform/offices/src/test/resources/uws.json"),
        Offices.class);
  }

  @Test
  public static void checkFiasIdIdDaDaTa() {
    officesList = offices.getOffices().stream().filter(office ->
        office.getLocations().stream().anyMatch(location ->
            given().baseUri("https://suggestions.dadata.ru/suggestions/api/4_1/rs/findById/address")
                .contentType(ContentType.JSON)
                .auth().oauth2("Token c700cdb63b3bb969dfad44120d06ba725e629373")
                .body(String.format("{\"query\": \"%s\"}", location.getFiasId()))
                .post().getBody().jsonPath().getList("suggestions").size() < 1)
    ).collect(Collectors.toList());
  }

  @Test
  public static void checkKindsAndIsVisibleSite() {
    officesList = offices.getOffices().stream().filter(office ->
        office.getKinds().contains(EMPTY_KIND) && office.getVisibleSite())
        .collect(Collectors.toList());
  }

  @Test
  public static void checkStatusCbAndIsVisibleSite() {
    officesList = offices.getOffices().stream().filter(office ->
        office.getStatusCB().equals(CbCodeName.OKVKU) && office.getVisibleSite())
        .collect(Collectors.toList());
  }

  @AfterMethod
  public static void afterMethod() {
    officesList.forEach(office -> System.out.println(office.getIdMasterSystem()));
  }
}
