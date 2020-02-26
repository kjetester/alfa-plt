package ru.alfabank.platform.tests.acms.widget;

import io.restassured.builder.*;
import io.restassured.http.*;
import io.restassured.path.json.*;
import io.restassured.specification.*;
import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.reporting.*;
import ru.alfabank.platform.tests.*;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.getSessionStorage;

@Listeners ({TestFailureListener.class})

public class WidgetCreationTest extends BaseTest {

  private RequestSpecification pageControllerSpec;
  private RequestSpecification contentPageControllerSpec;
  private List<Page> createdPages;
  private List<Widget> createdWidgets;

  /**
   * Define test env.
   */
  @BeforeClass
  public void beforeClass() {
    pageControllerSpec = new RequestSpecBuilder()
        .setBaseUri(baseUri).setBasePath("api/v1/content-store/admin-panel/pages")
        .setContentType(ContentType.JSON).setRelaxedHTTPSValidation().build();
    contentPageControllerSpec = new RequestSpecBuilder()
        .setBaseUri(baseUri).setBasePath("/api/v1/content-store/page-contents")
        .addQueryParam("city_uid", "21")
        .addQueryParam("device", "desktop")
        .setContentType(ContentType.JSON).setRelaxedHTTPSValidation().build();
  }

  @Test(description = "Тест создания виджета в корне. Все поля заполнены. Активен. Проперти нет.")
  public void activeWidgetWithNoPropsCreationTest() throws InterruptedException {
    Page localPage = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER.getLogin(), USER.getPassword())
        .openPagesTree()
        .createNewPage(null)
        .fillAndSubmitCreationForm(basePage);
    PageFactory.initElements(getDriver(), MainPage.class)
        .checkPageOpened(localPage.getPath());

    JsonPath savedPage =
        given().spec(pageControllerSpec).auth().oauth2(getSessionStorage().getItem("access-token")
            .replaceAll("\"","")).queryParam("uri","/" + localPage.getPath())
            .when().get().then().log().ifStatusCodeMatches(not(200)).statusCode(200)
            .extract().response().getBody().jsonPath();
    assertThat(savedPage.getInt("id")).isEqualTo(localPage.getId());
    assertThat(savedPage.getString("uri")).isEqualTo("/" + localPage.getPath());
    assertThat(savedPage.getString("title")).isEqualTo(localPage.getTitle());
    assertThat(savedPage.getString("description")).isEqualTo(localPage.getDescription());
    assertThat(savedPage.getBoolean("enable")).isEqualTo(localPage.isEnable());
    assertThat(savedPage.getString("dateFrom"))
        .contains(localPage.getDateFrom().toString().substring(0, 16));
    assertThat(savedPage.getString("dateTo"))
        .contains(localPage.getDateTo().toString().substring(0, 16));

    JsonPath view =
        given().spec(contentPageControllerSpec).queryParam("uri", "/" + localPage.getPath()).when()
            .get().then().log().ifStatusCodeMatches(not(404)).statusCode(404).extract().response()
            .getBody().jsonPath();
    assertThat(view.getString("code")).isEqualTo("404");
    assertThat(view.getString("message")).isEqualTo("Widgets not found.");
  }
}
