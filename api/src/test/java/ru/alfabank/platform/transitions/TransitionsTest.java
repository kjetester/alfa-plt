package ru.alfabank.platform.transitions;

import io.restassured.builder.*;
import io.restassured.filter.log.*;
import io.restassured.http.*;
import io.restassured.response.*;
import io.restassured.specification.*;
import org.assertj.core.api.*;
import org.testng.annotations.*;
import ru.alfabank.platform.transitions.buisnessobjects.*;

import static io.restassured.RestAssured.*;

public class TransitionsTest {

  public static final String EXPECTED_ERROR_MESSAGE = "Должно быть не пустое значение";

  private RequestSpecification minReqSpec;
  private RequestSpecification spec;
  private Body body;

  /**
   * Setting up.
   */
  @BeforeClass
  public void setUp() {
    minReqSpec = new RequestSpecBuilder()
        .setBaseUri("http://feature-swagger-webflux.tranbilog.reviews.ci.k8s.alfa.link").setBasePath("log")
        .addHeader("Referer", "https://alfabank.ru/get-money/credit/credit-cash/")
        .setContentType(ContentType.JSON).log(LogDetail.ALL).build();
    spec = new RequestSpecBuilder()
        .addRequestSpecification(minReqSpec)
        .addHeader("User-Agent", "Mozilla / 5.0(Windows NT 10.0; Win64; x64) AppleWebKit"
            + " / 537.36(KHTML, like Gecko) Chrome / 72.0 .3626 .109 Safari / 537.36")
        .build();
    Body.ClientData clientData = new Body.ClientData.ClientDataBuilder()
        .setPlatformId("alfasite").setBannerId("alfasite_cc_cross-sell-cc").build();
    Body.Data data = new Body.Data.DataBuilder().setProduct("cc").setProductType("visa_classic")
        .setLastName("Петров").setFirstName("Алексей").setMiddleName("Иванович")
        .setGender("m").setPhone("79261457895").setEmail("vashapochta@domain.ru")
        .setRegion("Москва").setLendingAmount("100000").setCreditTerm("24")
        .setCardID("SU").setPacketID("T04").setPrefilContractId("PDTT04").build();
    body = new Body.BodyBuilder().setBusinessUid("00005677e4cb4ba687ad774ac82a4e30")
        .setClientDate("2018-01-15T22:39:40+00:00")
        .setRecipient("https://anketa.alfabank.ru/alfaform-common-land/").setStatus("ok")
        .setFeedBackData(null).setClientData(clientData).setData(data).build();
  }

  @Test(dataProvider = "status")
  public void smokeTest(String status) {
    Body modifiedBody;
    if (status.equalsIgnoreCase("error")) {
      modifiedBody = new Body.BodyBuilder().using(body)
          .setStatus(status)
          .setFeedBackData(new Body.FeedBackData.FeedBackDataBuilder()
              .setStatusCode("500").setMessage("Internal Server Error").build())
          .build();
    } else {
      modifiedBody = new Body.BodyBuilder().using(body).setStatus(status).build();
    }
    Response response = given().spec(spec).body(modifiedBody).when().post();
    response.then().log().all();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Test
  public void noMandatoryHeadersTest() {
    Response response = given().spec(minReqSpec).body(body).when().post();
    response.then().log().all();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Test(dataProvider = "empty fields")
  public void businessUidValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(dataProvider = "empty fields")
  public void clientDateValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setClientDate(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(dataProvider = "empty fields")
  public void recipientValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setRecipient(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(dataProvider = "empty client data")
  public void clientDataValidationTest(Object value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setClientData(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().ifError();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .contains(EXPECTED_ERROR_MESSAGE);
  }

  @Test(dataProvider = "empty data")
  public void dataValidationTest(Object value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setData(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    response.then().log().all();
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
