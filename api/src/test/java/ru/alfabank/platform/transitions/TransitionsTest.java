package ru.alfabank.platform.transitions;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.transitions.buisnessobjects.Body;

import static io.restassured.RestAssured.given;

public class TransitionsTest {

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
        .setContentType(ContentType.JSON).build();
    spec = new RequestSpecBuilder()
        .addRequestSpecification(minReqSpec)
        .addHeader("Referer", "https://alfabank.ru/get-money/credit/credit-cash/")
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
        .setClientDate("2018-01-15T22:39:40.57Z")
        .setRecipient("https://anketa.alfabank.ru/alfaform-common-land/").setStatus("ok")
        .setFeedBackData(null).setClientData(clientData).setData(data).build();
  }

  @Test(dataProvider = "status")
  public void smokeTest(String status) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setStatus(status).build();
    Response response = given().spec(spec).body(modifiedBody).when().post();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Test
  public void noMandatoryHeadersTest() {
    Response response = given().spec(minReqSpec).body(body).when().post();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Test(dataProvider = "invalid values")
  public void businessUidValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setBusinessUid(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .isEqualTo("Должно быть не пустое значение");
  }

  @Test(dataProvider = "invalid values")
  public void clientDateValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setClientDate(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .isEqualTo("Должно быть не пустое значение");
  }

  @Test(dataProvider = "invalid values")
  public void recipientValidationTest(String value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setRecipient(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .isEqualTo("Должно быть не пустое значение");
  }

  @Test(dataProvider = "invalid values")
  public void clientDataValidationTest(Object value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setClientData(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .isEqualTo("Должно быть не пустое значение");
  }

  @Test(dataProvider = "invalid values")
  public void dataValidationTest(Object value) {
    Body modifiedBody = new Body.BodyBuilder().using(body).setData(value).build();
    Response response = given().spec(spec).body(modifiedBody).post();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
    Assertions.assertThat(response.getBody().jsonPath().get("message").toString())
        .isEqualTo("Должно быть не пустое значение");
  }

  @DataProvider(name = "invalid values")
  public Object[][] empties() {
    return new Object[][]{{null}, {""}};
  }

  @DataProvider(name = "status")
  public Object[][] status() {
    return new Object[][]{{"OK"}, {"ok"}, {"ERROR"}, {"error"}};
  }
}
