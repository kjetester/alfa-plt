package ru.alfabank.platform.tests.acms.page;

import org.apache.log4j.*;
import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.reporting.*;

import java.time.*;
import java.time.temporal.*;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.getSessionStorage;

@Listeners({TestFailureListener.class})
public class PageDeletionTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(PageDeletionTest.class);

  @Test(description = "Тест удаления страницы. Потомков нет. Виджетов нет.")
  public void singleEmptyPageDeletionTest() throws InterruptedException {
    // Предусловие
    Page page = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER.getLogin(), USER.getPassword())
        .openPagesTree()
        .createNewPage(null)
        .fillAndSubmitCreationForm(
            new Page.PageBuilder()
                .using(basicPage)
                .setDateFrom(LocalDateTime.now().minus(0, ChronoUnit.MINUTES))
                .setDateTo(LocalDateTime.now().plus(30, ChronoUnit.MINUTES))
                .setEnable(true)
                .build());
    createdPages.put(page.getPath(), page);
    // Шаги
    PageFactory.initElements(getDriver(), PagesSliderPage.class)
        .selectPage(page.getPath())
        .deletePage();
    createdPages.remove(page.getPath());
    LOGGER.info(String.format("Запрос страницы '/%s' в '/pageController'", page.getPath()));
    accessToken = getSessionStorage().getItem("access-token").replaceAll("\"","");
    int pageControllerStatusCode = given().spec(pageControllerSpec).auth().oauth2(accessToken)
        .queryParam("uri","/" + page.getPath())
        .when().get().statusCode();
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", page.getPath()));
    int contentPageControllerStatusCode = given().spec(contentPageControllerSpec)
        .queryParam("uri", "/" + page.getPath())
        .when().get().statusCode();
    softly.assertThat(pageControllerStatusCode == 404).isTrue();
    softly.assertThat(contentPageControllerStatusCode == 404).isTrue();
    softly.assertAll();
  }
}
