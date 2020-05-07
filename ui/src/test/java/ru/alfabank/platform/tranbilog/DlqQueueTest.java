package ru.alfabank.platform.tranbilog;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.helpers.UuidHelper.getNewUuid;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.TestNGException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.transitions.Body;
import ru.alfabank.platform.buisenessobjects.transitions.RabbitBody;

public class DlqQueueTest extends ru.alfabank.platform.tranbilog.BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(DlqQueueTest.class);
  private static final String USERNAME = "guest";
  private static final String PASSWORD = "z9P5izOpyZZGOmG3q10OusvD";
  private static final String BASE_URL = "http://rabbitmq.k8s.alfa.link";
  private static final BasicAuthScheme BASIC_AUTH_SCHEME = new BasicAuthScheme();
  private static final String WRITE_BASE_PATH = "/api/exchanges/{vhost}/{exchangeName}/publish";
  private static final String READ_BASE_PATH = "api/queues/{vhost}/{queueName}/get";
  private static final String EXCHANGE_NAME = "business-log";
  private static final String QUEUE_NAME = "business-log.business-log-service.dlq";
  private static final String READ_JSON_BODY =
      "{\"count\":100,\"ackmode\":\"ack_requeue_false\",\"encoding\":\"auto\"}";

  private RequestSpecification writeSpec;
  private RequestSpecification readSpec;
  private Body defaultBody;
  private Body.FeedBackData feedBackData;
  private Body.ClientData clientData;
  private Body.Data data;

  /**
   * Set up.
   * @param vhost environment
   */
  @Parameters({"env"})
  @BeforeTest
  public void setUp(
      @ParameterKey ("environment") @Optional("preprod") String vhost) {
    LOGGER.debug(String.format(
        "Настраиваю параметры авторизации:\n\t\t username: '%s',\n\t\tpassword: '%s'",
        USERNAME, PASSWORD));
    BASIC_AUTH_SCHEME.setUserName(USERNAME);
    BASIC_AUTH_SCHEME.setPassword(PASSWORD);
    LOGGER.debug("Настраиваю базовую спецификацию");
    RequestSpecification baseSpec = new RequestSpecBuilder()
        .setBaseUri(BASE_URL)
        .setContentType(ContentType.JSON)
        .setAuth(BASIC_AUTH_SCHEME)
        .addPathParam("vhost", vhost)
        .build();
    LOGGER.debug("Настраиваю спецификацию записи");
    writeSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(WRITE_BASE_PATH)
        .addPathParam("exchangeName", EXCHANGE_NAME)
        .build();
    LOGGER.debug("Настраиваю спецификацию чтения");
    readSpec = new RequestSpecBuilder()
        .addRequestSpecification(baseSpec)
        .setBasePath(READ_BASE_PATH)
        .addPathParam("queueName", QUEUE_NAME)
        .build();
    feedBackData = new Body.FeedBackData.FeedBackDataBuilder()
        .setStatusCode("500")
        .setMessage("Internal Server Error")
        .build();
    clientData = new Body.ClientData.ClientDataBuilder()
        .setPlatformId("alfasite")
        .setBannerId("alfasite_cc_cross-sell-cc")
        .build();
    data = new Body.Data.DataBuilder()
        .setProduct("cc")
        .setProductType("visa_classic")
        .setLastName("Петров")
        .setFirstName("Алексей")
        .setMiddleName("Иванович")
        .setGender("m")
        .setPhone("79261457895")
        .setEmail("vashapochta@domain.ru")
        .setRegion("Москва")
        .setLendingAmount("100000")
        .setCreditTerm("24")
        .setCardID("SU")
        .setPacketID("T04")
        .setPrefilContractId("PDTT04")
        .build();
    defaultBody = new Body.BodyBuilder()
        .setBusinessUid(getNewUuid())
        .setClientDate("2020-01-31T14:17:10+00:00")
        .setReferer(REFERER_URL)
        .setRecipient(RECIPIENT_URL)
        .setClientData(clientData)
        .setData(data)
        .build();
  }

  @BeforeMethod(alwaysRun = true)
  public void cleanUp() {
    given().spec(readSpec).body(READ_JSON_BODY).post();
  }

  @Test(
      description = "Позитивный тест со статусами OK & ERROR",
      dataProvider = "status")
  public void positiveTest(
      @ParameterKey("status")
      final String status) throws InterruptedException {
    LOGGER.info(String.format("Условие: статус '%s'", status));
    Body modifiedBody;
    switch (status.toLowerCase()) {
      case "error": modifiedBody = new Body.BodyBuilder().using(defaultBody).setStatus(status)
          .setFeedBackData(feedBackData).build();
        break;
      case "ok": modifiedBody = new Body.BodyBuilder().using(defaultBody).setStatus(status)
          .setFeedBackData(null).build();
        break;
      default: throw new TestNGException("Что-то пошло не так");
    }
    Response writeRes = given().spec(writeSpec).body(new RabbitBody(modifiedBody)).when().post();
    LOGGER.info(writeRes.asString());
    assertThat(writeRes.getStatusCode()).isEqualTo(200);
    TimeUnit.SECONDS.sleep(5);
    soft.assertThat(writeRes.getStatusCode()).isEqualTo(200);
    soft.assertThat(writeRes.getBody().jsonPath().getBoolean("routed")).isTrue();
    soft.assertAll();
    String readRes = given().spec(readSpec).body(READ_JSON_BODY).post()
        .then().statusCode(200).extract().body().asString();
    LOGGER.info(readRes);
    soft.assertThat(readRes).isEqualTo("[]");
    soft.assertAll();
  }

  @Test(
      description = "Позитивный тест с максимумом полей",
      priority = 1)
  public void maxFieldsTest() throws InterruptedException {
    Body modifiedBody = new Body.BodyBuilder()
        .using(defaultBody)
        .setStatus("error")
        .setServerTime(LocalDateTime.now())
        .setFeedBackData(feedBackData)
        .setPageUri(REFERER_URL)
        .setUserAgent(USER_AGENT)
        .setIp("217.12.97.206")
        .build();
    Response writeRes = given().spec(writeSpec).body(new RabbitBody(modifiedBody)).when().post();
    LOGGER.info(writeRes.asString());
    soft.assertThat(writeRes.getStatusCode()).isEqualTo(200);
    soft.assertThat(writeRes.getBody().jsonPath().getBoolean("routed")).isTrue();
    soft.assertAll();
    TimeUnit.SECONDS.sleep(5);
    String readRes = given().spec(readSpec).body(READ_JSON_BODY).post()
        .then().statusCode(200).extract().body().asString();
    LOGGER.info(readRes);
    assertThat(readRes).isEqualTo("[]");
  }

  @Test(
      description = "Негативный тест",
      dataProvider = "empty fields",
      priority = 2)
  public void negativeTest(
      @ParameterKey("param") final String param,
      @ParameterKey("value") final Object value) throws InterruptedException {
    LOGGER.info(String.format("Условие: '%s' -> '%s'", param, value));
    Body modifiedBody;
    switch (param) {
      case "businessUid":   modifiedBody = new Body.BodyBuilder().using(defaultBody)
          .setBusinessUid(value).setStatus("ok").build();
        break;
      case "clientDate":    modifiedBody = new Body.BodyBuilder().using(defaultBody)
          .setClientDate(value).setStatus("ok").build();
        break;
      case "referer":       modifiedBody = new Body.BodyBuilder().using(defaultBody)
          .setReferer(value).setStatus("ok").build();
        break;
      case "recipient":     modifiedBody = new Body.BodyBuilder().using(defaultBody)
          .setRecipient(value).setStatus("ok").build();
        break;
      case "feedBackData":  modifiedBody = new Body.BodyBuilder().using(defaultBody)
          .setFeedBackData(value).setStatus("error").build();
        break;
      case "clientData":    modifiedBody = new Body.BodyBuilder().using(defaultBody)
          .setClientData(value).setStatus("ok").build();
        break;
      case "data":          modifiedBody = new Body.BodyBuilder().using(defaultBody)
          .setData(value).setStatus("ok").build();
        break;
      default:              throw new TestNGException("Что-то пошло не так");
    }
    Response writeRes = given().spec(writeSpec).body(new RabbitBody(modifiedBody)).when().post();
    LOGGER.info(String.format("Получен ответ: \n%s", writeRes.asString()));
    assertThat(writeRes.getStatusCode()).as("Статус код должен быть '200'").isEqualTo(200);
    TimeUnit.SECONDS.sleep(5);
    String readRes = given().spec(readSpec).body(READ_JSON_BODY)
        .when().post().then().extract().body().asString();
    LOGGER.info(String.format("Результат обработки сообщения очередью: \n%s", readRes));
    assertThat(
        readRes.contains(String.format("default message [%s]", param))
            && readRes.contains(EXPECTED_ERROR_MESSAGE))
        .as("Проверяю наличие описания ошибки с указанием на поле")
        .isTrue();
  }

  /**
   * Data provider.
   * @return Object[][]
   */
  @DataProvider(name = "status")
  public Object[][] status() {
    return new Object[][]{
        {"OK"},
        {"ok"},
        {"ERROR"},
        {"error"}
    };
  }

  /**
   * DataProvider.
   * @return Object[][]
   */
  @DataProvider(name = "empty fields")
  public Object[][] fields() {
    return new Object[][]{
        {"businessUid", null}, {"businessUid", ""},
        {"clientDate", null}, {"clientDate", ""},
        {"referer", null}, {"referer", ""},
        {"recipient", null}, {"recipient", ""},
        {"clientData", null}, {"clientData", new Body.EmptyClientData()},
        {"data", null}, {"data", new Body.EmptyData()},
        {"feedBackData", null}, {"feedBackData", new Body.EmptyClientData()}
    };
  }
}
