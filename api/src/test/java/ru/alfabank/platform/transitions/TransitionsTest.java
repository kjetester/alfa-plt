package ru.alfabank.platform.transitions;

import io.restassured.builder.*;
import io.restassured.filter.log.*;
import io.restassured.http.*;
import io.restassured.response.*;
import io.restassured.specification.*;
import org.apache.logging.log4j.*;
import org.assertj.core.api.*;
import org.testng.annotations.*;
import ru.alfabank.platform.transitions.buisnessobjects.*;

import static io.restassured.RestAssured.*;
import static org.apache.logging.log4j.LogManager.*;
import static ru.alfabank.platform.helpers.TestDataHelper.*;

public class TransitionsTest {

  private static final Logger log = getLogger(TransitionsTest.class);
  private static final String BASE_URL = "http://develop.ci.k8s.alfa.link/api/v1/tranbilog";
  private static final String REFERER_URL = "https://alfabank.ru/get-money/credit/credit-cash/";
  private static final String RECIPIENT_URL = "https://anketa.alfabank.ru/alfaform-common-land/";
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit";
  private static final String EXPECTED_ERROR_MESSAGE = "Должно быть не пустое значение";

  private RequestSpecification minReqSpec;
  private RequestSpecification spec;
  private Body body;

  /**
   * Setting up.
   */
  @BeforeClass
  public void setUp() {
    minReqSpec = new RequestSpecBuilder()
        .setBaseUri(BASE_URL).setBasePath("log")
        .addHeader("Referer", REFERER_URL)
        .setContentType(ContentType.JSON).log(LogDetail.ALL).build();
    spec = new RequestSpecBuilder().addRequestSpecification(minReqSpec)
        .addHeader("User-Agent", USER_AGENT).build();
    Body.ClientData clientData = new Body.ClientData.ClientDataBuilder()
        .setPlatformId("alfasite").setBannerId("alfasite_cc_cross-sell-cc").build();
    Body.Data data = new Body.Data.DataBuilder().setProduct("cc").setProductType("visa_classic")
        .setLastName("Петров").setFirstName("Алексей").setMiddleName("Иванович")
        .setGender("m").setPhone("79261457895").setEmail("vashapochta@domain.ru")
        .setRegion("Москва").setLendingAmount("100000").setCreditTerm("24")
        .setCardID("SU").setPacketID("T04").setPrefilContractId("PDTT04").build();
    body = new Body.BodyBuilder().setBusinessUid("00005677e4cb4ba687ad774ac82a4e30")
        .setClientDate("2020-01-31T14:17:10+00:00")
        .setRecipient(RECIPIENT_URL).setStatus("ok")
        .setFeedBackData(null).setClientData(clientData).setData(data).build();
  }

  @Test(dataProvider = "status")
  public void positiveSmokeTest(String status) {
    Body modifiedBody;
    if (status.equalsIgnoreCase("error")) {
      modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
          .setStatus(status).setFeedBackData(
              new Body.FeedBackData.FeedBackDataBuilder().setStatusCode("500")
                  .setMessage("Internal Server Error").build()).build();
    } else {
      modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
          .setStatus(status).build();
    }
    Response response = given().spec(spec).body(modifiedBody).when().post();
    response.then().log().all(true);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Test(priority = 1)
  public void positiveNoMandatoryHeadersTest() {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid()).build();
    Response response = given().spec(minReqSpec).body(modifiedBody).when().post();
    response.then().log().all(true);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Test(dataProvider = "empty fields", priority = 2)
  public void negativeBusinessUidValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all(true);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(dataProvider = "empty fields", priority = 2)
  public void negativeClientDateValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
        .setClientDate(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all(true);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(dataProvider = "empty fields", priority = 2)
  public void negativeRecipientValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
        .setRecipient(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all(true);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(dataProvider = "empty client data", priority = 2)
  public void negativeClientDataValidationTest(Object value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
        .setClientData(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all(true);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(dataProvider = "empty data", priority = 2)
  public void negativeDataValidationTest(Object value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
        .setData(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all(true);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @DataProvider(name = "empty client data")
  public Object[][] emptyClientData() {
    return new Object[][]{{null}, {new Body.EmptyClientData()}};
  }

  @DataProvider(name = "empty data")
  public Object[][] emptyData() {
    return new Object[][]{{null}, {new Body.EmptyData()}};
  }

  @DataProvider(name = "empty fields")
  public Object[][] fields() {
    return new Object[][]{{null}, {""}};
  }

  @DataProvider(name = "status")
  public Object[][] status() {
    return new Object[][]{{"OK"}, {"ok"}, {"ERROR"}, {"error"}};
  }
}
