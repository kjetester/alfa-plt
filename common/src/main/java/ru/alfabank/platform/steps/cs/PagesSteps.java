package ru.alfabank.platform.steps.cs;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.helpers.UuidHelper.getShortRandUuid;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.contentstore.Page;
import ru.alfabank.platform.businessobjects.contentstore.PageCopyRequest;
import ru.alfabank.platform.businessobjects.contentstore.Widget;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.Team;
import ru.alfabank.platform.steps.BaseSteps;
import ru.alfabank.platform.users.AccessibleUser;

public class PagesSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(PagesSteps.class);

  /**
   * Create page.
   *
   * @param user user
   * @return page id
   */
  public List<Page> getPagesList(final AccessibleUser user) {
    LOGGER.info("Выполняю запрос получения списка страниц");
    var response =
        given()
            .spec(getPageSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .when().get()
            .then().extract().response();
    describeResponse(LOGGER, response);
    response.then().statusCode(SC_OK);
    return List.of(response.as(Page[].class));
  }

  /**
   * Create new enabled page.
   *
   * @param user user
   * @return pege ID
   */
  public Integer createEnabledPage(final AccessibleUser user) {
    String pageUri = getShortRandUuid();
    var page = new Page.Builder()
        .setUri("/qr/automation/" + pageUri)
        .setTitle("title_" + pageUri)
        .setEnable(true)
        .build();
    return createPage(page, user).as(Page.class).getId();
  }

  /**
   * Create new page.
   *
   * @param teams teams list
   * @param user  user
   * @return response
   */
  public Response createPageAndGetResponse(final List<Team> teams,
                                           final AccessibleUser user) {
    String pageUri = getShortRandUuid();
    var page = new Page.Builder()
        .setUri("/qr/automation/" + pageUri)
        .setTitle("title_" + pageUri)
        .setEnable(true)
        .setTeamsList(teams)
        .build();
    return createPage(page, user);
  }

  /**
   * Create new page.
   *
   * @param teams teams list
   * @param user  user
   * @return page id
   */
  public Integer createPage(final List<Team> teams,
                            final AccessibleUser user) {
    String pageUri = getShortRandUuid();
    var page = new Page.Builder()
        .setUri("/qr/automation/" + pageUri)
        .setTitle("title_" + pageUri)
        .setEnable(true)
        .setTeamsList(teams)
        .build();
    return createPage(page, user).then().extract().body().jsonPath().getInt("id");
  }

  /**
   * Create new page.
   *
   * @param start     start date
   * @param end       end date
   * @param isEnabled is enabled
   * @param user      user
   * @return page id
   */
  public Integer createPage(final String start,
                            final String end,
                            final Boolean isEnabled,
                            final List<Team> teams,
                            final AccessibleUser user) {
    String pageUri = getShortRandUuid();
    var page = new Page.Builder()
        .setUri("/qr/automation/" + pageUri)
        .setTitle("title_" + pageUri)
        .setDateFrom(start)
        .setDateTo(end)
        .setEnable(isEnabled)
        .setTeamsList(teams)
        .build();
    return createPage(page, user).then().extract().body().jsonPath().getInt("id");
  }

  /**
   * Create page.
   *
   * @param page page
   * @param user user
   * @return response
   */
  private Response createPage(final Page page,
                              final AccessibleUser user) {
    LOGGER.info("Выполняю запрос создания страницы\n" + describeBusinessObject(page));
    var response =
        given()
            .spec(getPageSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .body(page)
            .when().post();
    describeResponse(LOGGER, response);
    final Page createdPage;
    if (response.getStatusCode() == SC_OK) {
      createdPage = new Page.Builder()
          .using(page)
          .setId(response.then().extract().body().jsonPath().getInt("id"))
          .build();
      CREATED_PAGES.put(createdPage.getId(), createdPage);
    }
    return response;
  }

  /**
   * Get page from CS.
   *
   * @param pageId page ID
   * @param device device
   * @param user   user
   * @return list of widgets
   */
  public Response getPage(final Integer pageId,
                          final Device device,
                          final AccessibleUser user) {
    LOGGER.info(String.format(
        "Запрос страницы '%s' в '/metaInfoContentPageController'",
        pageId));
    final var response =
        given()
            .spec(getMetaInfoContentPageSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .queryParam("device", device)
            .queryParam("pageId", pageId)
            .get();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Get Widgets list from CS.
   *
   * @param pageId page ID
   * @param device device
   * @param user   user
   * @return list of widgets
   */
  public List<Widget> getWidgetsList(final Integer pageId,
                                     final Device device,
                                     final AccessibleUser user) {
    final var response = getPage(pageId, device, user);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    return List.of(response.as(Widget[].class));
  }

  /**
   * Modify Page.
   *
   * @param pageId           page ID
   * @param pageModification page modification
   * @param user             user
   * @return response
   */
  public Response modifyPage(final Integer pageId,
                             final Page pageModification,
                             final AccessibleUser user) {
    LOGGER.info(String.format(
        "Запрос в '/pageController' на изменение страницы '%s':\n%s",
        pageId, describeBusinessObject(CREATED_PAGES.get(pageId))));
    final var response = given().spec(getPageIdSpec())
        .auth().oauth2(user.getJwt().getAccessToken())
        .pathParam("id", pageId)
        .body(pageModification)
        .when().put();
    describeResponse(LOGGER, response);
    if (response.getStatusCode() == SC_OK) {
      final var modifiedPage = response.as(Page.class);
      CREATED_PAGES.replace(pageId, modifiedPage);
    }
    return response;
  }

  /**
   * Copy page.
   *
   * @param pageId page ID
   * @param user   user
   * @return response
   */
  public Response copyPage(final Integer pageId,
                           final CopyMethod copyMethod,
                           final AccessibleUser user) {
    final var page = CREATED_PAGES.get(pageId);
    final var body = new PageCopyRequest.Builder()
        .setMode(copyMethod)
        .setPage(
            new Page.Builder()
                .using(page)
                .setId(null)
                .setUri(page.getUri().replaceAll("/$", "-copy/"))
                .build()
        )
        .build();
    LOGGER.info(String.format(
        "Запрос в '/pageController' на копирование страницы '%s':\n%s\nс параметрами:\n%s",
        pageId, describeBusinessObject(page), describeBusinessObject(body)));
    final var response = given().spec(getPageIdCopySpec())
        .auth().oauth2(user.getJwt().getAccessToken())
        .pathParam("id", pageId)
        .body(body)
        .when().post();
    describeResponse(LOGGER, response);
    if (response.getStatusCode() == SC_OK) {
      final var createdPage = response.as(Page.class);
      CREATED_PAGES.put(createdPage.getId(), createdPage);
    }
    return response;
  }

  /**
   * Delete page.
   *
   * @param pageId page ID
   * @param user   user
   * @return response
   */
  public Response deletePage(final Integer pageId,
                             final AccessibleUser user) {
    LOGGER.info(String.format(
        "Запрос в '/pageController' на удаление страницы '%s':\n%s",
        pageId, describeBusinessObject(CREATED_PAGES.get(pageId))));
    final var response = given().spec(getPageIdSpec())
        .auth().oauth2(user.getJwt().getAccessToken())
        .pathParam("id", pageId)
        .when().delete();
    describeResponse(LOGGER, response);
    if (response.getStatusCode() == SC_OK) {
      CREATED_PAGES.remove(pageId);
    }
    return response;
  }

  /**
   * Delete created pages.
   */
  public void deleteCreatedPages() {
    final var pagesCount = CREATED_PAGES.size();
    LOGGER.info(String.format("Страниц к удалению: %d", pagesCount));
    if (pagesCount > 0) {
      ConcurrentHashMap<Integer, Page> createdPagesMap = new ConcurrentHashMap<>(CREATED_PAGES);
      createdPagesMap.entrySet().parallelStream().forEach(entry -> {
        final var response = deletePage(entry.getValue().getId(), getContentManager());
        if (response.getStatusCode() == SC_OK) {
          LOGGER.info(String.format("Страница '%s' удалена", entry.getValue().getUri()));
          createdPagesMap.remove(entry.getKey());
        } else {
          LOGGER.warn(String.format(
              "Не удалось удалить страницу '%s'\n%s",
              entry.getValue().getUri(),
              response.prettyPrint()));
        }
      });
    }
  }

  public static <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor) {
    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }
}
