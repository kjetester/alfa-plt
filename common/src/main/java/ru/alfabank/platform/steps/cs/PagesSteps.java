package ru.alfabank.platform.steps.cs;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.helpers.UuidHelper.getShortRandUuid;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.Team;
import ru.alfabank.platform.steps.BaseSteps;
import ru.alfabank.platform.users.AccessibleUser;

public class PagesSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(PagesSteps.class);

  /**
   * Create new enabled page.
   *
   * @param user user
   */
  public Integer createEnabledPage(final AccessibleUser user) {
    String pageUri = getShortRandUuid();
    var page = new Page.Builder()
        .setUri("/qr/automation/" + pageUri)
        .setTitle("title_" + pageUri)
        .setEnable(true)
        .build();
    return createPage(page, user);
  }

  /**
   * Create new page.
   *
   * @param user user
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
    return createPage(page, user);
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
                            final AccessibleUser user) {
    String pageUri = getShortRandUuid();
    var page = new Page.Builder()
        .setUri("/qr/automation/" + pageUri)
        .setTitle("title_" + pageUri)
        .setDateFrom(start)
        .setDateTo(end)
        .setEnable(isEnabled)
        .build();
    return createPage(page, user);
  }

  /**
   * Create page.
   *
   * @param page page
   * @param user user
   * @return page id
   */
  private Integer createPage(Page page,
                             final AccessibleUser user) {
    LOGGER.info(String.format("Выполняю запрос создания страницы\n%s",
        describeBusinessObject(page)));
    var response =
        given()
            .spec(getPageSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .body(page)
            .when().post()
            .then().extract().response();
    LOGGER.info(String.format("Получен ответ: %s\n%s",
        response.getStatusCode(),
        response.prettyPrint()));
    response.then().statusCode(SC_OK);
    page = new Page.Builder()
        .using(page)
        .setId(response.then().extract().body().jsonPath().getInt("id"))
        .build();
    CREATED_PAGES.put(page.getId(), page);
    return page.getId();
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
    LOGGER.info(String.format(
        "Запрос страницы '%s' в '/metaInfoContentPageController'",
        pageId));
    Response response =
        given()
            .spec(getMetaInfoContentPageSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .queryParam("device", device)
            .queryParam("pageId", pageId)
            .get();
    assertThat(response.statusCode()).isEqualTo(SC_OK);
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s,",
        response.getStatusLine(),
        response.prettyPrint()));
    return Arrays.asList(response.as(Widget[].class));
  }

  /**
   * Delete created pages.
   */
  public void deleteCreatedPages() {
    final var pagesCount = CREATED_PAGES.size();
    LOGGER.info(String.format("Страниц к удалению: %d", pagesCount));
    if (pagesCount > 0) {
      CREATED_PAGES.entrySet().parallelStream().forEach(entry -> {
        LOGGER.info(String.format("Начинаю процесс удаления страницы '%s'",
            entry.getValue().getUri()));
        final var response = given().spec(getPageIdSpec())
            .auth().oauth2(getContentManager().getJwt().getAccessToken())
            .pathParam("id", entry.getValue().getId())
            .when().delete();
        if (response.getStatusCode() == SC_OK) {
          LOGGER.info(String.format("Страница '%s' удалена", entry.getValue().getUri()));
        } else {
          LOGGER.warn(String.format(
              "Не удалось удалить страницу '%s'\n%s",
              entry.getValue().getUri(),
              response.prettyPrint()));
        }
      });
    }
  }
}
