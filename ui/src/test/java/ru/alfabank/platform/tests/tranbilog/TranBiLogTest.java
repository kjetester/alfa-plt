package ru.alfabank.platform.tests.tranbilog;

import io.restassured.builder.*;
import io.restassured.filter.log.*;
import io.restassured.http.*;
import io.restassured.response.*;
import io.restassured.specification.*;
import org.apache.log4j.*;
import org.assertj.core.api.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.transitions.*;

import static io.restassured.RestAssured.*;
import static ru.alfabank.platform.helpers.UUIDHelper.*;

public class TranBiLogTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(TranBiLogTest.class);

  private RequestSpecification minReqSpec;
  private RequestSpecification spec;
  private Body body;

  /**
   * Setting up.
   */
  @Parameters({"env"})
  @BeforeClass
  public void setUp(@Optional("develop") String env) {
    String baseUrl = "http://" + env + ".ci.k8s.alfa.link/api/v1/tranbilog";
    minReqSpec = new RequestSpecBuilder()
        .setBaseUri(baseUrl)
        .setBasePath("log")
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

  @Test(
      description = "Позитивный тест со статусами OK & ERROR",
      dataProvider = "status")
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
    LOGGER.info(response.asString());
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Test(
      description = "Позитивный тест без заголовков",
      priority = 1)
  public void positiveNoHeadersTest() {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid()).build();
    Response response = given().spec(minReqSpec).body(modifiedBody).when().post();
    LOGGER.info(response.asString());
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Test(
      description = "Негативный тест - вадидация по businessUid",
      dataProvider = "empty fields",
      priority = 2)
  public void negativeBusinessUidValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    LOGGER.info(response.asString());
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(
      description = "Негативный тест - вадидация по clientDate",
      dataProvider = "empty fields",
      priority = 2)
  public void negativeClientDateValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
        .setClientDate(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    LOGGER.info(response.asString());
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(
      description = "Негативный тест - вадидация по recipient",
      dataProvider = "empty fields",
      priority = 2)
  public void negativeRecipientValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
        .setRecipient(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    LOGGER.info(response.asString());
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(
      description = "Негативный тест - вадидация по clientData",
      dataProvider = "empty client data",
      priority = 2)
  public void negativeClientDataValidationTest(Object value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
        .setClientData(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    LOGGER.info(response.asString());
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(
      description = "Негативный тест - вадидация по data",
      dataProvider = "empty data",
      priority = 2)
  public void negativeDataValidationTest(Object value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(getNewUuid())
        .setData(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    LOGGER.info(response.asString());
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
