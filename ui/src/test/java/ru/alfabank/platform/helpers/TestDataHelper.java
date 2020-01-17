package ru.alfabank.platform.helpers;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.ContentType.URLENC;
import static org.hamcrest.Matchers.not;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.testng.*;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.buisenessobjects.User;
import ru.alfabank.platform.buisenessobjects.Widget;

public class TestDataHelper {

  private Properties props;
  private Page page;

  /**
   * Class constructor.
   * @param user user
   * @param page test page
   */
  public TestDataHelper(User user, Page page) {
    this.page = page;
    loadProps();
    String oauth2Token = given().relaxedHTTPSValidation()
        .baseUri(props.getProperty("keycloak.base.uri"))
        .basePath(props.getProperty("keycloak.base.path"))
        .contentType(URLENC)
        .formParam("client_id","acms")
        .formParam("username", user.getLogin())
        .formParam("password", user.getPassword())
        .formParam("grant_type", "password")
        .when().post()
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200).extract().body()
        .jsonPath().getString("access_token");
    page.setWidgetList(new ArrayList<>(Arrays.asList(given().relaxedHTTPSValidation()
        .baseUri(props.getProperty("app.base.uri"))
        .basePath(props.getProperty("app.base.path"))
        .auth().oauth2(oauth2Token)
        .contentType(JSON)
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
      ex.printStackTrace();
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
}
