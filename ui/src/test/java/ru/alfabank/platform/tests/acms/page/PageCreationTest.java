package ru.alfabank.platform.tests.acms.page;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners({TestFailureListener.class})
public class PageCreationTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(PageCreationTest.class);
  private static final SoftAssertions SOFTLY = new SoftAssertions();

  @Test(description = "Тест создания страницы в корне.\n"
                    + "Все поля заполнены.\n"
                    + "Активна.\n"
                    + "Виджетов нет.")
  public void activePageCreationTest() throws InterruptedException {
    // Шаги
    Page page = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .createNewPageWithinPage(null)
        .fillAndSubmitCreationForm(new Page.PageBuilder().using(basicPage)
            .setDateFrom(LocalDateTime.now())
            .setDateTo(LocalDateTime.now().plusMinutes(30))
            .setEnable(true).build());
    // Проверка перехода к созданной странице
    final String uri = page.getUri();
    PageFactory.initElements(getDriver(), MainPage.class).checkPageOpened(uri);
    createdPages.put(uri, page);
    // Проверка наличия созданной страницы в Системе и ее свойств
    LOGGER.info(String.format("Запрос страницы '%s' в '/pageController'", uri));
    Response pageControllerResponse = given().spec(pageControllerSpec)
        .auth().oauth2(getToken(USER).getAccessToken())
        .queryParam("uri", uri)
        .when().get().then().extract().response();
    LOGGER.info("Получен ответ:\n" + pageControllerResponse.prettyPrint());
    SOFTLY.assertThat(pageControllerResponse.getStatusCode()).isEqualTo(200);
    JsonPath pageControllerJson = pageControllerResponse.getBody().jsonPath();
    SOFTLY.assertThat(pageControllerJson.getInt("id")).isEqualTo(page.getId());
    SOFTLY.assertThat(pageControllerJson.getString("uri")).isEqualTo(uri);
    SOFTLY.assertThat(pageControllerJson.getString("title")).isEqualTo(page.getTitle());
    SOFTLY.assertThat(pageControllerJson.getString("description")).isEqualTo(page.getDescription());
    SOFTLY.assertThat(pageControllerJson.getBoolean("enable")).isEqualTo(page.isEnable());
    SOFTLY.assertThat(pageControllerJson.getString("dateFrom"))
        .contains(page.getDateFrom().substring(0, 16));
    SOFTLY.assertThat(pageControllerJson.getString("dateTo"))
        .contains(page.getDateTo().substring(0, 16));
    // Проверка того, что contentPageController не отдаст страницу без виджетов
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", uri));
    Response contentPageControllerResponse = given().spec(contentPageControllerSpec)
        .queryParam("uri", uri)
        .when().get().then().extract().response();
    LOGGER.info("Получен ответ:\n" + contentPageControllerResponse.prettyPrint());
    SOFTLY.assertThat(contentPageControllerResponse.getStatusCode()).isEqualTo(404);
    SOFTLY.assertThat(contentPageControllerResponse.asString()).contains("Widgets not found");
    SOFTLY.assertAll();
  }

  @Test(description = "Тест создания страницы в корне.\n"
                    + "Все поля заполнены.\n"
                    + "Неактивна.\n"
                    + "Виджетов нет.")
  public void inactivePageCreationTest() throws InterruptedException {
    Page page = new Page.PageBuilder()
        .using(basicPage)
        .setEnable(false)
        .build();
    String parentPage = "qr";
    page = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .createNewPageWithinPage(parentPage)
        .fillAndSubmitCreationForm(page);
    final String uri = page.getUri();
    createdPages.put(uri, page);
    // Проверка перехода к созданной странице
    PageFactory.initElements(getDriver(), MainPage.class).checkPageOpened(uri);
    // Проверка наличия созданной страницы в Системе и ее свойств
    LOGGER.info(String.format("Запрос страницы '%s' в '/pageController'", uri));
    String pageUri = "/" + parentPage + uri;
    Response pageControllerResponse = given().spec(pageControllerSpec)
        .auth().oauth2(getToken(USER).getAccessToken())
        .queryParam("uri", pageUri)
        .when().get().then().extract().response();
    LOGGER.info("Получен ответ:\n" + pageControllerResponse.prettyPrint());
    JsonPath pageControllerJson = pageControllerResponse.getBody().jsonPath();
    SOFTLY.assertThat(pageControllerJson.getInt("id")).isEqualTo(page.getId());
    SOFTLY.assertThat(pageControllerJson.getString("uri")).isEqualTo(pageUri);
    SOFTLY.assertThat(pageControllerJson.getString("title")).isEqualTo(page.getTitle());
    SOFTLY.assertThat(pageControllerJson.getString("description")).isEqualTo(page.getDescription());
    SOFTLY.assertThat(pageControllerJson.getBoolean("enable")).isEqualTo(page.isEnable());
    SOFTLY.assertThat(pageControllerJson.getString("dateFrom")).isNullOrEmpty();
    SOFTLY.assertThat(pageControllerJson.getString("dateTo")).isNullOrEmpty();
    // Проверка того, что contentPageController не отдаст неактивную страницу
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", pageUri));
    Response contentPageControllerResponse = given().spec(contentPageControllerSpec)
        .queryParam("uri", pageUri)
        .when().get().then().extract().response();
    LOGGER.info("Получен ответ:\n" + contentPageControllerResponse.prettyPrint());
    SOFTLY.assertThat(contentPageControllerResponse.getStatusCode()).isEqualTo(404);
    //TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-18982
    SOFTLY.assertThat(contentPageControllerResponse.asString()).contains("Page not found");
    SOFTLY.assertAll();
  }
}
