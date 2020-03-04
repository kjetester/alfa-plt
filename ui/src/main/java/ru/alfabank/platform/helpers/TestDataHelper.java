package ru.alfabank.platform.helpers;

import static org.hamcrest.Matchers.not;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.TestNGException;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.buisenessobjects.User;
import ru.alfabank.platform.buisenessobjects.Widget;

public class TestDataHelper {

  private static final Logger LOGGER = LogManager.getLogger(TestDataHelper.class);

  private Properties props;
  private Page page;
  private Page createdPage;

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
    page.setWidgetList(new ArrayList<>(Arrays.asList(
        RestAssured.given().relaxedHTTPSValidation()
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
