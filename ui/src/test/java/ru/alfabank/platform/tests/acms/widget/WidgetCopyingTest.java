package ru.alfabank.platform.tests.acms.widget;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;

import java.time.LocalDateTime;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.support.PageFactory;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners ({TestFailureListener.class})
public class WidgetCopyingTest extends BaseWidgetTest {

  private static final Logger LOGGER = LogManager.getLogger(WidgetCopyingTest.class);

  @Test(description = "Тест копирования виджета на страницу.\n"
                      + "Есть дети.\n")
  public void widgetCopyingTest() throws InterruptedException, JSONException {
    // Предусловие
    Page page = new Page.PageBuilder().using(basicPage)
        .setDateFrom(LocalDateTime.now())
        .setDateTo(LocalDateTime.now().plusMinutes(30))
        .setEnable(true).build();
    page = new Page.PageBuilder().using(page).setId(
        given()
            .spec(pageControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .body(page)
        .when()
            .post()
        .then()
            .log().ifStatusCodeMatches(not(200))
            .statusCode(200)
            .extract().body().jsonPath().get("id"))
        .build();
    createdPages.put(page.getUri(), page);
    // Шаги
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(sourcePage)
        .copyAllWidgets(sourcePage, page.getUri())
        .publishDraft();
    Thread.sleep(5_000L);
    // ТЕСТ
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", page.getUri()));
    String actual = given().spec(contentPageControllerSpec)
        .queryParam("uri", page.getUri())
        .when().get().then().extract().response().getBody().asString();
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", sourcePage));
    String expected = given().spec(contentPageControllerSpec)
        .queryParam("uri", sourcePage)
        .when().get().then().extract().response().getBody().asString();
    LOGGER.info(
        String.format("Сравнение страниц - исходной: '%s' и новой: '%s'",
            sourcePage, page.getUri()));
    JSONAssert.assertEquals(expected, actual, new CustomComparator(
        JSONCompareMode.LENIENT,
        new Customization("title", (o1, o2) -> true),
        new Customization("description", (o1, o2) -> true),
        new Customization("uid", (o1, o2) -> true),
        new Customization("widgets[*].uid", (o1, o2) -> true),
        new Customization("widgets[*].children..uid", (o1, o2) -> true)
        ));
    LOGGER.info("Успех");
  }
}
