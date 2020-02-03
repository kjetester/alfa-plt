package ru.alfabank.platform.helpers;

import io.restassured.*;
import io.restassured.http.*;
import org.apache.log4j.*;
import org.testng.*;
import ru.alfabank.platform.buisenessobjects.*;

import java.io.*;
import java.util.*;

import static org.hamcrest.Matchers.*;

public class TestDataHelper {

  private static final Logger LOGGER = LogManager.getLogger(TestDataHelper.class);

  private Properties props;
  private Page page;
  private Page createdPage;

  public TestDataHelper() {

  }

  /**
   * Class constructor.
   * @param user user
   * @param page test page
   */
  public TestDataHelper(User user, Page page) {
    this.page = page;
    loadProps();
    String oauth2Token = RestAssured.given().relaxedHTTPSValidation()
        .baseUri(props.getProperty("keycloak.base.uri"))
        .basePath(props.getProperty("keycloak.base.path"))
        .contentType(ContentType.URLENC)
        .formParam("client_id","acms")
        .formParam("username", user.getLogin())
        .formParam("password", user.getPassword())
        .formParam("grant_type", "password")
        .when().post()
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200).extract().body()
        .jsonPath().getString("access_token");
    page.getWidgetList(new ArrayList<>(Arrays.asList(RestAssured.given().relaxedHTTPSValidation()
        .baseUri(props.getProperty("app.base.uri"))
        .basePath(props.getProperty("app.base.path"))
        .auth().oauth2(oauth2Token)
        .contentType(ContentType.JSON)
        .queryParams("device", "desktop")
        .queryParams("pageId", page.getId())
        .when().get("/content-store/admin-panel/meta-info-page-contents")
        .then().log().ifStatusCodeMatches(not(200))
        .statusCode(200).extract().body().as(Widget[].class))));
  }

  /**
   * Loading props.
   */
  public void loadProps() {
    try (InputStream input = new FileInputStream("src/test/resources/props.properties")) {
      Properties props = new Properties();
      props.load(input);
      this.props = props;
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage());
    }
  }

  /**
   * Defining the test widget.
   * @return widget
   */
  public Widget getTestWidget() {
    return page.getWidgetList().stream().findFirst().orElseThrow(()
        -> new TestNGException("No one test widget is defined"));
  }

  public Page getCreatedPage() {
    return createdPage;
  }

  public void setCreatedPage(Page createdPage) {
    this.createdPage = createdPage;
  }
}
