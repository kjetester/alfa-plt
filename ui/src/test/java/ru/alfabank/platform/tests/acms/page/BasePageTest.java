package ru.alfabank.platform.tests.acms.page;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.helpers.DriverHelper.killDriver;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;
import static ru.alfabank.platform.helpers.UUIDHelper.getShortRandUuid;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.tests.BaseTest;

public class BasePageTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(BasePageTest.class);
  protected static final String PAGE_CONTROLLER_BASE_PATH = "api/v1/content-store/admin-panel/pages/";

  protected Page basicPage;
  protected RequestSpecification pageControllerSpec;
  protected RequestSpecification contentPageControllerSpec;
  protected Map<String, Page> createdPages = new HashMap<>();

  /**
   * Define test env.
   */
  @BeforeClass
  public void beforeClass() {
    LOGGER.debug("Устанавливаю конфгурацию запросов к page-controller");
    pageControllerSpec = new RequestSpecBuilder()
        .setRelaxedHTTPSValidation()
        .setBaseUri(baseUri)
        .setBasePath(PAGE_CONTROLLER_BASE_PATH)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL)
        .build();
    LOGGER.debug("Устанавливаю конфгурацию запросов к content-page-controller");
    contentPageControllerSpec = new RequestSpecBuilder().log(LogDetail.ALL)
        .setBaseUri(baseUri).setBasePath("/api/v1/content-store/page-contents")
        .addQueryParam("city_uid", "21")
        .addQueryParam("device", "desktop")
        .setContentType(ContentType.JSON).setRelaxedHTTPSValidation().build();
  }

  /**
   * Define test data.
   */
  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() {
    String pageUrl = getShortRandUuid();
    LOGGER.debug("Собираю pojo тестовой страницы");
    basicPage = new Page.PageBuilder()
        .setUri(pageUrl)
        .setTitle("title_" + pageUrl)
        .setDescription("description_" + pageUrl)
        .setKeywords("keywords_" + pageUrl).build();
  }

  /**
   * Tear down.
   */
  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    killDriver();
  }

  /**
   * Remove created instances.
   */
  @AfterClass(alwaysRun = true)
  public void afterClass() {
    int i = createdPages.size();
    if (i > 0) {
      createdPages.forEach((key, value) -> {
        LOGGER.info(String.format("Есть %d страниц(ы) на удаление", i));
        int statusCode =
            given()
                .spec(pageControllerSpec)
                .basePath(PAGE_CONTROLLER_BASE_PATH + "/{id}")
                .auth().oauth2(getToken(USER).getAccessToken())
                .pathParam("id", createdPages.get(key).getId())
            .when().delete()
                .getStatusCode();
        if (statusCode == 200) {
          LOGGER.info(
              String.format(
                  "Страница '/%s' удалена", createdPages.get(key).getUri()));
        } else {
          LOGGER.warn(String.format(
              "Не удалось удалить страницу '/%s'", createdPages.get(key).getUri()));
        }
      });
    }
  }
}
