package ru.alfabank.platform.tests.acms.page;

import io.restassured.path.json.*;
import org.apache.log4j.*;
import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.reporting.*;

import java.time.*;

import static io.restassured.RestAssured.*;
import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.helpers.KeycloakHelper.*;

@Listeners({TestFailureListener.class})
public class PageCreationTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(PageCreationTest.class);

  @Test(description = "Тест создания страницы в корне.\n"
                    + "Все поля заполнены.\n"
                    + "Активна.\n"
                    + "Виджетов нет.")
  public void activePageCreationTest() throws InterruptedException {
    Page page = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .createNewPage(null)
        .fillAndSubmitCreationForm(new Page.PageBuilder().using(basicPage)
            .setDateFrom(LocalDateTime.now())
            .setDateTo(LocalDateTime.now().plusMinutes(30))
            .setEnable(true).build());
    // Проверка перехода к созданной странице
    PageFactory.initElements(getDriver(), MainPage.class).checkPageOpened(page.getUri());
    createdPages.put(page.getUri(), page);
    // Проверка наличия созданной страницы в Системе и ее свойств
    LOGGER.info(String.format("Запрос страницы '/%s' в '/pageController'", page.getUri()));
    JsonPath savedPage = given().spec(pageControllerSpec)
        .auth().oauth2(getToken(USER).getAccessToken())
        .queryParam("uri","/" + page.getUri())
        .when().get().then().extract().response().getBody().jsonPath();
    LOGGER.info("Получен ответ:\n" + savedPage.prettyPrint());
    softly.assertThat(savedPage.getInt("id")).isEqualTo(page.getId());
    softly.assertThat(savedPage.getString("uri")).isEqualTo("/" + page.getUri());
    softly.assertThat(savedPage.getString("title")).isEqualTo(page.getTitle());
    softly.assertThat(savedPage.getString("description")).isEqualTo(page.getDescription());
    softly.assertThat(savedPage.getBoolean("enable")).isEqualTo(page.isEnable());
    softly.assertThat(savedPage.getString("dateFrom"))
        .contains(page.getDateFrom().substring(0, 16));
    softly.assertThat(savedPage.getString("dateTo"))
        .contains(page.getDateTo().substring(0, 16));
    // Проверка того, что contentPageController не отдаст страницу без виджетов
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", page.getUri()));
    JsonPath view = given().spec(contentPageControllerSpec).queryParam("uri", "/" + page.getUri())
            .when().get().then().extract().response().getBody().jsonPath();
    LOGGER.info("Получен ответ:\n" + view.prettyPrint());
    softly.assertThat(view.getString("code")).isEqualTo("404");
    softly.assertThat(view.getString("message")).isEqualTo("Widgets not found.");
    softly.assertAll();
  }

  @Test(description = "Тест создания страницы в корне.\n"
                    + "Все поля заполнены.\n"
                    + "Неактивна.\n"
                    + "Виджетов нет.")
  public void inactivePageCreationTest() throws InterruptedException {
    Page page = new Page.PageBuilder().using(basicPage)
        .setEnable(false)
        .build();
    String parentPage = "qr";
    page = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .createNewPage(parentPage)
        .fillAndSubmitCreationForm(page);
    createdPages.put(page.getUri(), page);
    // Проверка перехода к созданной странице
    PageFactory.initElements(getDriver(), MainPage.class).checkPageOpened(page.getUri());
    // Проверка наличия созданной страницы в Системе и ее свойств
    LOGGER.info(String.format("Запрос страницы '/%s' в '/pageController'", page.getUri()));
    String pageUri = "/" + parentPage + "/" + page.getUri();
    JsonPath savedPage = given().spec(pageControllerSpec)
        .auth().oauth2(getToken(USER).getAccessToken())
        .queryParam("uri", pageUri)
        .when().get().then().extract().response().getBody().jsonPath();
    LOGGER.info("Получен ответ:\n" + savedPage.prettyPrint());
    softly.assertThat(savedPage.getInt("id")).isEqualTo(page.getId());
    softly.assertThat(savedPage.getString("uri")).isEqualTo(pageUri);
    softly.assertThat(savedPage.getString("title")).isEqualTo(page.getTitle());
    softly.assertThat(savedPage.getString("description")).isEqualTo(page.getDescription());
    softly.assertThat(savedPage.getBoolean("enable")).isEqualTo(page.isEnable());
    softly.assertThat(savedPage.getString("dateFrom")).isNullOrEmpty();
    softly.assertThat(savedPage.getString("dateTo")).isNullOrEmpty();
    // Проверка того, что contentPageController не отдаст неактивную страницу
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", pageUri));
    JsonPath view = given().spec(contentPageControllerSpec).queryParam("uri", pageUri)
        .when().get().then().extract().response().getBody().jsonPath();
    LOGGER.info("Получен ответ:\n" + view.prettyPrint());
    softly.assertThat(view.getString("code")).isEqualTo("404");
    //TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-18982
    softly.assertThat(view.getString("message")).isEqualTo("Page not found.");
    softly.assertAll();
  }
}
