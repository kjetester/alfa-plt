package ru.alfabank.platform.tests.acms.page;

import org.apache.log4j.*;
import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.reporting.*;

import java.time.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.helpers.KeycloakHelper.*;

@Listeners({TestFailureListener.class})
public class PageDeletionTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(PageDeletionTest.class);

  @Test(description = "Тест удаления страницы. Потомков нет. Виджетов нет.")
  public void singleEmptyPageDeletionTest() throws InterruptedException {
    // Предусловия
    Page page = new Page.PageBuilder().using(basicPage)
        .setDateFrom(LocalDateTime.now())
        .setDateTo(LocalDateTime.now().plusMinutes(30))
        .setEnable(true).build();
    page = new Page.PageBuilder().using(page).setId(
        given().spec(pageControllerSpec).auth().oauth2(getToken(USER).getAccessToken()).body(page)
            .when().post()
            .then().log().ifStatusCodeMatches(not(200)).extract().body().jsonPath().get("id"))
        .build();
    createdPages.put(page.getUri(), page);
    // Шаги
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(page.getUri())
        .deletePage();
    LOGGER.info(String.format("Запрос страницы '/%s' в '/pageController'", page.getUri()));
    int pageControllerStatusCode = given().spec(pageControllerSpec)
        .auth().oauth2(getToken(USER).getAccessToken())
        .queryParam("uri","/" + page.getUri())
        .when().get().getStatusCode();
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", page.getUri()));
    int contentPageControllerStatusCode = given().spec(contentPageControllerSpec)
        .queryParam("uri", "/" + page.getUri())
        .when().get().getStatusCode();
    softly.assertThat(pageControllerStatusCode == 404).isTrue();
    softly.assertThat(contentPageControllerStatusCode == 404).isTrue();
    softly.assertAll();
    createdPages.remove(page.getUri());
  }
}
