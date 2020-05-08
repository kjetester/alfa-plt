package ru.alfabank.platform.page;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;

import java.time.LocalDateTime;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.utils.TestFailureListener;

@Listeners({TestFailureListener.class})
public class PageDeletionTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(PageDeletionTest.class);
  private static final SoftAssertions SOFTLY = new SoftAssertions();

  @Test(description = "Тест удаления страницы:\n"
      + "\t1. Потомков нет\n"
      + "\t2. Виджетов нет",
      groups = {"page", "deletionPage"})
  public void singleEmptyPageDeletionTest() {
    // PRECONDITIONS //
    Page page = new Page.Builder().using(basePage)
        .setDateFrom(LocalDateTime.now().toString())
        .setDateTo(LocalDateTime.now().plusMinutes(30).toString())
        .setEnable(true)
        .build();
    page = new Page.Builder().using(page).setId(
        given().spec(pageControllerSpec).auth().oauth2(getToken(USER).getAccessToken()).body(page)
            .when().post()
            .then().log().ifStatusCodeMatches(not(200)).extract().body().jsonPath().get("id"))
        .build();
    createdPages.put(page.getUri(), page);
    // STEPS //
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(page.getUri())
        .deletePage();
    LOGGER.info(String.format("Запрос страницы '%s' в '/pageController'", page.getUri()));
    int pageControllerStatusCode = given().spec(pageControllerSpec)
        .auth().oauth2(getToken(USER).getAccessToken())
        .queryParam("uri","/" + page.getUri())
        .when().get().getStatusCode();
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", page.getUri()));
    int contentPageControllerStatusCode = given().spec(contentPageControllerSpec)
        .queryParam("uri", "/" + page.getUri())
        .when().get().getStatusCode();
    SOFTLY.assertThat(pageControllerStatusCode == 404).isTrue();
    SOFTLY.assertThat(contentPageControllerStatusCode == 404).isTrue();
    SOFTLY.assertAll();
    createdPages.remove(page.getUri());
  }
}
