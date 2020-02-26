package ru.alfabank.platform.tests.acms.page;

import io.restassured.builder.*;
import io.restassured.filter.log.*;
import io.restassured.http.*;
import io.restassured.specification.*;
import org.apache.log4j.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.tests.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.helpers.UUIDHelper.*;

public class BasePageTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(BasePageTest.class);

  protected String pageControllerBasePath = "api/v1/content-store/admin-panel/pages/";
  protected Page basicPage;
  protected RequestSpecification pageControllerSpec;
  protected RequestSpecification contentPageControllerSpec;
  protected Map<String, Page> createdPages = new HashMap<>();
  protected String accessToken;

  /**
   * Define test env.
   */
  @BeforeClass
  public void beforeClass() {
    LOGGER.debug("Устанавливаю конфгурацию запросов к page-controller");
    pageControllerSpec = new RequestSpecBuilder().log(LogDetail.ALL)
        .setBaseUri(baseUri).setBasePath(pageControllerBasePath)
        .setContentType(ContentType.JSON).setRelaxedHTTPSValidation().build();
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
  public void setUpBasePage() {
    String pageUrl = getShortRandUuid() + "/";
    LOGGER.debug("Собираю pojo тестовой страницы");
    basicPage = new Page.PageBuilder()
        .setPath(pageUrl)
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
  public void cleanUp() {
    int i = createdPages.size();
    if (i > 0) {
      createdPages.entrySet().forEach(entry -> {
        LOGGER.info(String.format("Есть %d страниц(ы) на удаление", i));
        String key = entry.getKey();
        int statusCode =
            given()
                .spec(pageControllerSpec)
                .basePath(pageControllerBasePath + "/{id}")
                .auth().oauth2(accessToken)
                .pathParam("id", createdPages.get(key).getId())
            .when().delete()
                .getStatusCode();
        if (statusCode == 200) {
          LOGGER.info(
              String.format(
                  "Страница '/%s' удалена", createdPages.get(key).getPath()));
        } else {
          LOGGER.warn(String.format(
              "Не удалось удалить страницу '/%s'", createdPages.get(key).getPath()));
        }
      });
    }
  }
}
