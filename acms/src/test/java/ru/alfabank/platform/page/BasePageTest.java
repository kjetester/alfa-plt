package ru.alfabank.platform.page;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.helpers.DriverHelper.killDriver;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.TestNGException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.ContentPageControllerResponse;
import ru.alfabank.platform.businessobjects.ContentPageControllerResponse.Widget;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;
import ru.alfabank.platform.businessobjects.enums.Experiment;
import ru.alfabank.platform.businessobjects.enums.Team;

public class BasePageTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(BasePageTest.class);

  protected Map<String, Page> createdPages = new HashMap<>();

  /**
   * After method.
   */
  @AfterMethod(
      description = "Закрываю сессию web-driver'а",
      alwaysRun = true)
  public void afterMethod() {
    killDriver();
  }

  /**
   * Remove created instances.
   */
  @AfterClass(
      description = "Удаляю созданные во время теста страницы",
      alwaysRun = true)
  public void afterClass() {
    int pagesCount = createdPages.size();
    if (pagesCount > 0) {
      createdPages.forEach((key, value) -> {
        LOGGER.info(String.format(
            "Есть %d страниц(а)(ы) на удаление",
            pagesCount));
        int statusCode =
            given()
                .spec(pageControllerSpec)
                .basePath(PAGE_CONTROLLER_BASE_PATH + "/{id}")
                .auth().oauth2(getToken(USER).getAccessToken())
                .pathParam("id", createdPages.get(key).getId())
                .when().delete()
                .getStatusCode();
        if (statusCode == 200) {
          LOGGER.info(String.format(
              "Страница '%s' удалена",
              createdPages.get(key).getUri()));
        } else {
          LOGGER.warn(String.format(
              "Не удалось удалить страницу '%s'",
              createdPages.get(key).getUri()));
        }
      });
    }
  }

  /**
   * Create a new page in root.
   *
   * @return page
   */
  protected Page createNewPageInRoot() {
    LOGGER.info("Создаю страницу в корне через 'Content Store API'");
    return getPage(
        new Page.Builder()
            .using(basePage)
            .setDateFrom(LocalDateTime.now().toString())
            .setDateTo(LocalDateTime.now().plusMinutes(30).toString())
            .setEnable(true)
            .build());
  }

  /**
   * Comparing a given page against a source page in the '/content-page-controller'.
   *
   * @param createdPage page
   */
  public void comparePagesInPageController(final Page createdPage) {
    LOGGER.info(String.format(
        "Сравнение страниц в /page-controller. Исходной: '%s' vs. копии: '%s'",
        sourcePage.getUri(),
        createdPage.getUri()));
    Page expected = getPageFromPageController();
    Page actual = getPageFromPageController(createdPage);
    expected.equals(actual);
  }

  /**
   * Get expected / actual (if page parameter was passed)
   * result from the '/page-controller'.
   *
   * @param page created page
   * @return page from the '/page-controller'
   */
  protected Page getPageFromPageController(final Page... page) {
    int pageId = page.length > 0 ? page[0].getId() : sourcePage.getId();
    LOGGER.info(String.format(
        "Запрос страницы с id=%d в '/pageController'",
        pageId));
    Response response =
        given()
            .spec(pageControllerSpec)
            .basePath(PAGE_CONTROLLER_BASE_PATH + "{id}")
            .pathParam("id", pageId)
            .auth().oauth2(getToken(USER).getAccessToken())
            .when().get()
            .then().extract().response();
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s,",
        response.getStatusLine(),
        response.prettyPrint()));
    assertThat(response.statusCode()).isEqualTo(200);
    return response.as(Page.class);
  }

  /**
   * Compare pages in the '/content-page-controller/.
   *
   * @param createdPage page
   * @param method      copy page method
   */
  protected void comparePagesInContentPageController(
      final Page createdPage, final CopyMethod method) {
    LOGGER.info(String.format(
        "Сравнение страниц в /content-page-controller. Исходная: '%s' vs. копия: '%s'",
        sourcePage.getUri(),
        createdPage.getUri()));
    List<ContentPageControllerResponse.Widget> expected =
        getPageFromContentPageController();
    List<Widget> actual =
        getPageFromContentPageController(createdPage);
    assertThat(actual.size()).isEqualTo(expected.size());
    IntStream.range(0, expected.size()).forEach(i -> expected.get(i).equals(actual.get(i), method));
    LOGGER.info("Невалидных различий не выявлено");
  }

  /**
   * Get expected / actual (if was passed) page(s) from the '/content-page-controller'.
   *
   * @param page created page
   * @return list of Widgets
   */
  protected List<ContentPageControllerResponse.Widget> getPageFromContentPageController(
      final Page... page) {
    String pageUri = page.length > 0 ? page[0].getUri() : sourcePage.getUri();
    LOGGER.info(String.format(
        "Запрос страницы '%s' в '/contentPageController'",
        pageUri));
    Response response = given().spec(contentPageControllerSpec).queryParam("uri", pageUri)
        .when().get().then().extract().response();
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s,",
        response.getStatusLine(),
        response.prettyPrint()));
    assertThat(response.statusCode()).isEqualTo(200);
    ContentPageControllerResponse contentPageControllerResponse = response
        .as(ContentPageControllerResponse.class);
    return contentPageControllerResponse.getWidgets();
  }

  /**
   * Compare pages in the '/meta-info-content-page-controller'.
   *
   * @param createdPage page
   * @param method      copy page method
   */
  protected void comparePagesMetaInfoContentPageController(
      final Page createdPage, final CopyMethod method) {
    LOGGER.info(String.format(
        "Сравнение страниц в /meta-info-content-page-controller. Исходной: '%s' vs. копии: '%s'",
        sourcePage.getUri(),
        createdPage.getUri()));
    List<Widget> expected = getPageFromMetaInfoContentPageController();
    List<Widget> actual = getPageFromMetaInfoContentPageController(createdPage);
    assertThat(actual.size()).isEqualTo(expected.size());
    IntStream.range(0, expected.size()).forEach(i -> expected.get(i).equals(actual.get(i), method));
    LOGGER.info("Невалидных различий не выявлено");
  }

  /**
   * Get expected / actual (if was passed) page(s) from the '/meta-info-content-page-controller'.
   *
   * @param page created page
   * @return list of Widgets
   */
  private List<Widget> getPageFromMetaInfoContentPageController(final Page... page) {
    Integer pageId = page.length > 0 ? page[0].getId() : sourcePage.getId();
    LOGGER.info(String.format(
        "Запрос страницы '%s' в '/metaInfoContentPageController'",
        pageId));
    Response response =
        given()
            .spec(metaInfoContentPageControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .queryParam("pageId", pageId)
            .when().get()
            .then().extract().response();
    assertThat(response.statusCode()).isEqualTo(200);
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s,",
        response.getStatusLine(),
        response.prettyPrint()));
    return Arrays.asList(response.as(Widget[].class));
  }

  /**
   * Request to page-controller API.
   *
   * @param page page
   * @return page with id
   */
  private Page getPage(Page page) {
    page = new Page.Builder()
        .using(page)
        .setId(
            given()
                .spec(pageControllerSpec)
                .auth().oauth2(getToken(USER).getAccessToken())
                .body(page)
                .when()
                .post()
                .then()
                .log().ifStatusCodeMatches(not(SC_OK))
                .statusCode(SC_OK)
                .extract().body().jsonPath().get("id"))
        .build();
    LOGGER.info(String.format(
        "Создана страница: '%s'",
        page.toString()));
    createdPages.put(page.getUri(), page);
    return page;
  }

  /**
   * Check created page at the '/page-controller'.
   *
   * @param page page
   */
  protected void checkCreatedPageAtPageController(Page page) {
    checkPageForMandatoryFields(page);
    // Проверка наличия созданной страницы в Системе и ее свойств
    LOGGER.info(String.format(
        "Запрос страницы '%s' в '/pageController'",
        page.getUri()));
    Response pageControllerResponse =
        given()
            .spec(pageControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .queryParam("uri", page.getUri())
            .when()
            .get()
            .then()
            .extract().response();
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s",
        pageControllerResponse.statusLine(),
        pageControllerResponse.prettyPrint()));
    SoftAssertions softly = new SoftAssertions();
    softly
        .assertThat(pageControllerResponse.getStatusCode())
        .isEqualTo(200);
    JsonPath pageControllerJson = pageControllerResponse.getBody().jsonPath();
    softly
        .assertThat(pageControllerJson.getInt("id"))
        .isEqualTo(page.getId());
    softly
        .assertThat(pageControllerJson.getString("uri"))
        .isEqualTo(page.getUri());
    softly
        .assertThat(pageControllerJson.getString("title"))
        .isEqualTo(page.getTitle());
    softly
        .assertThat(pageControllerJson.getBoolean("enable"))
        .isEqualTo(page.isEnable());
    if (page.getDescription() != null) {
      softly
          .assertThat(pageControllerJson.getString("description"))
          .isEqualTo(page.getDescription());
    }
    if (page.getDateFrom() == null) {
      softly
          .assertThat(pageControllerJson.getString("dateFrom"))
          .isNullOrEmpty();
    } else {
      softly
          .assertThat(pageControllerJson.getString("dateFrom"))
          .contains(page.getDateFrom().substring(0, 16));
    }
    if (page.getDateTo() == null) {
      softly
          .assertThat(pageControllerJson.getString("dateTo"))
          .isNullOrEmpty();
    } else {
      softly
          .assertThat(pageControllerJson.getString("dateTo"))
          .contains(page.getDateTo().substring(0, 16));
    }
    if (page.getTeams() == null) {
      softly
          .assertThat(pageControllerJson.getList("teams"))
          .isNullOrEmpty();
    } else {
      softly
          .assertThat(pageControllerJson.getList("teams"))
          .containsAll(page.getTeams().stream().map(Team::getId).collect(Collectors.toList()));
    }
    softly.assertAll();
  }

  /**
   * Check created page in the '/content-page-controller'.
   *
   * @param page page
   */
  protected void checkCreatedPageAtContentPageController(Page page) {
    checkPageForMandatoryFields(page);
    LOGGER.info(String.format(
        "Запрос страницы '%s' в '/contentPageController'",
        page.getUri()));
    Response contentPageControllerResponse = given().spec(contentPageControllerSpec)
        .queryParam("uri", page.getUri()).get().then().extract().response();
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s",
        contentPageControllerResponse.statusLine(),
        contentPageControllerResponse.prettyPrint()));
    SoftAssertions softly = new SoftAssertions();
    if (page.getWidgetList().isEmpty() || !page.isEnable()) {
      softly
          .assertThat(contentPageControllerResponse.getStatusCode())
          .isEqualTo(404);
      if (page.isEnable()) {
        softly
            .assertThat(contentPageControllerResponse.getStatusCode())
            .isEqualTo(404);
        softly
            .assertThat(contentPageControllerResponse.asString())
            .contains("Widgets not found");
      } else {
        // Проверка того, что contentPageController не отдаст неактивную страницу
        softly
            .assertThat(contentPageControllerResponse.getStatusCode())
            .isEqualTo(404);
        softly
            .assertThat(contentPageControllerResponse.asString())
            .contains("Page not found");
      }
    } else {
      // Проверка того, что contentPageController отдаст активную страницу с виджетами
      JsonPath contentPageControllerJson = contentPageControllerResponse.getBody().jsonPath();
      softly
          .assertThat(contentPageControllerResponse.getStatusCode())
          .isEqualTo(200);
      softly
          .assertThat(contentPageControllerJson.getString("title"))
          .isEqualTo(page.getTitle());
      softly
          .assertThat(contentPageControllerJson.getString("description"))
          .isEqualTo(page.getDescription());
      softly
          .assertThat(contentPageControllerJson.getList("widgets").size())
          .isEqualTo(page.getWidgetList().stream().filter(w ->
              w.getExperimentOptionName().contains(Experiment.DEFAULT.toString())).count());
    }
    softly.assertAll();
  }

  /**
   * Check page for mandatory fields.
   *
   * @param page page
   */
  private void checkPageForMandatoryFields(Page page) {
    if (page.getTitle() == null
        || page.getUri() == null
        || page.getId() == null
        || page.isEnable() == null) {
      throw new TestNGException("Страница не имеет обязательных параметров");
    }
  }
}
