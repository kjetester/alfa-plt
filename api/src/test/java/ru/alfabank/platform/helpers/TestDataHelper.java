package ru.alfabank.platform.helpers;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.Matchers.not;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ru.alfabank.platform.businessobjects.CityGroup;
import ru.alfabank.platform.businessobjects.Device;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.businessobjects.Property;
import ru.alfabank.platform.businessobjects.Value;
import ru.alfabank.platform.businessobjects.Widget;

public class TestDataHelper {

  private static final String KEYCLOAK_BASE_URL = "https://keycloak.k8s.alfa.link";
  private static final String KEYCLOAK_BASE_PATH =
      "auth/realms/local_users/protocol/openid-connect/token";
  private static final String BASE_URI = "http://develop.ci.k8s.alfa.link";
  private static final String BASE_PATH = "api/v1";
  private static final String USERNAME = "user1";
  private static final String PASSWORD = "123";

  public static final String RESOURCE_URL = "/content-store";
  public static final String CONTENT_STORE_ADMIN_URL = RESOURCE_URL + "/admin-panel/pages";
  public static final String DRAFT_CONTROLLER_URL = RESOURCE_URL
      + "/admin-panel/pages/{pageId}/drafts";
  public static final String META_INFO_PAGE_CONTROLLER_URL = RESOURCE_URL
      + "/admin-panel/meta-info-page-contents";

  private static String oauth2Token;
  private static RequestSpecification requestSpecification;
  private static List<Page> pageList;
  private static List<String> widgetsUidListOnTestPage = new ArrayList<>();
  private static Widget.Children[] testWidgetChildren;
  protected static Map<String, List<Widget>> pagesWidgetMap;
  protected static Map<String, JsonPath> pagesJsonMap;
  protected static Page testPage;
  protected static Widget testWidget;
  protected static Property testProperty;
  protected static Value testPropertyValue;

  public static Map<Entity, String> createdEntities = new HashMap<>();

  /**
   *  getting authorization token.
   */
  private static void setAuthToken() {
    RequestSpecification keycloakReqSpec = new RequestSpecBuilder()
        .setRelaxedHTTPSValidation()
        .setBaseUri(KEYCLOAK_BASE_URL)
        .setBasePath(KEYCLOAK_BASE_PATH)
        .setContentType(ContentType.URLENC)
        .addFormParam("client_id","acms")
        .addFormParam("username", USERNAME)
        .addFormParam("password", PASSWORD)
        .addFormParam("grant_type", "password")
        .build();
    oauth2Token = given().spec(keycloakReqSpec).when().post().then().log()
        .ifStatusCodeMatches(not(200)).statusCode(200).extract().body()
        .jsonPath().getString("access_token");
  }

  /**
   *  Sets the requests specification.
   */
  public static void setRequestSpec() {
    setAuthToken();
    requestSpecification = new RequestSpecBuilder()
        .setRelaxedHTTPSValidation()
        .setBaseUri(BASE_URI)
        .setBasePath(BASE_PATH)
        .setAuth(oauth2(oauth2Token))
        .setContentType(ContentType.JSON)
        .build();
  }

  /**
   * Adds new params to the request specification and rebuilds it.
   * @param paramName param name
   * @param paramValue param value
   */
  public static void addParamToRequestSpec(String paramName, String... paramValue) {
    requestSpecification = new RequestSpecBuilder()
      .addRequestSpecification(requestSpecification)
      .addPathParam(paramName, paramValue)
      .build();
  }

  /**
   * Adds new query params to the request specification and rebuilds it.
   * @param paramName param name
   * @param paramValue param value
   */
  public static void addQueryParamsToRequestSpec(String paramName, Object... paramValue) {
    requestSpecification = new RequestSpecBuilder()
      .addRequestSpecification(requestSpecification)
      .addQueryParam(paramName, paramValue)
      .build();
  }

  /**
   * Gets the city groups.
   */
  public static void setCityGroups() {
    CityGroup cityGroup =
        given().spec(requestSpecification)
            .when().get("geo-group/city-groups")
            .then().statusCode(200).extract().as(CityGroup.class);
  }

  /**
   * Gets the pages.
   */
  public static void setPageList() {
    pageList = new ArrayList<>(Arrays.asList(given()
      .spec(requestSpecification)
      .when()
      .get(CONTENT_STORE_ADMIN_URL)
      .then().log().ifStatusCodeMatches(not(200))
      .statusCode(200)
      .extract().as(Page[].class)));
  }

  /**
   * Gets the widgets its properties and its values on every page.
   * @param device device
   */
  public static void setPagesWidgetMap(Device device) {
    pagesWidgetMap = new HashMap<>();
    pagesJsonMap = new HashMap<>();
    List<Page> pageWithNoOneWidgetlList = new ArrayList<>();
    pageList.forEach(p -> {
      Response response = given().spec(requestSpecification)
          .queryParams("device", device, "pageId", p.getId())
          .get(META_INFO_PAGE_CONTROLLER_URL);
      if (!response.getStatusLine().contains("200")) {
        try {
          throw new ErrorWhileGettingWidgetsException(
            String.format("Couldn't get Widgets on the Page with id: '%s' "
                    + "and uri: '%s' for device '%s'", p.getId(), p.getUri(), device));
        } catch (ErrorWhileGettingWidgetsException e) {
          pageWithNoOneWidgetlList.add(p);
        }
      } else {
        response.then().log().ifStatusCodeMatches(not(200));
        response.then().extract().as(Widget[].class);
        pagesWidgetMap.put(p.getId(), Arrays.asList(response.then().extract().as(Widget[].class)));
        pagesJsonMap.put(p.getId(), response.jsonPath());
      }
    });
    pageList.removeAll(pageWithNoOneWidgetlList);
  }

  /**
   * Defines and sets test objects.
   */
  public static void setTestObjects() {
    try {
      if (pageList.size() > 0 && pagesWidgetMap.size() > 0) {
        loop:
        for (Map.Entry<String, List<Widget>> page : pagesWidgetMap.entrySet()) {
          List<Widget> widgetList = page.getValue();
          for (int i = 0; i < widgetList.size(); i++) {
            if (widgetList.get(i).getProperties().size() > i) {
              if (widgetList.get(i).getProperties().get(i).getValues().size() > 0) {
                testPage = pageList.get(i);
                System.out.printf("Test Page has been set to: '%s'\n", testPage.getId());
                testWidget = widgetList.get(i);
                System.out.printf("Test Widget has been set to: '%s'\n", testWidget.getUid());
                testProperty = testWidget.getProperties().get(i);
                System.out.printf("Test Property has been set to: '%s'\n", testProperty.getUid());
                testPropertyValue = testProperty.getValues().get(i);
                System.out.printf("Test Value has been set to: '%s'\n", testPropertyValue.getUid());
                break loop;
              }
            }
          }
        }
      } else {
        throw new NoTestDataException();
      }
    } catch (NoTestDataException e) {
      System.out.println(e.getMessage());
    }
    pagesWidgetMap.get(testPage.getId()).forEach(
        widget -> widgetsUidListOnTestPage.add(widget.getUid()));
    System.out.println("The Widgets UIDs on the Test Page are: ");
    widgetsUidListOnTestPage.forEach(System.out::println);
    testWidgetChildren = testWidget.getChildren();
    System.out.println(
        testWidgetChildren.length > 0 ? "The Widget children are: " :
            "The Widget hasn't any child.");
    Arrays.asList(testWidgetChildren).forEach(System.out::println);
  }

  /**
   * Swaps outers in an array.
   * @return array of entities
   */
  public static Object[] getSwappedOutersWidgetsUidArray() {
    if (createdEntities.containsKey(Entity.widget)) {
      widgetsUidListOnTestPage.add(0, createdEntities.get(Entity.widget));
    }
    List<String> initList = widgetsUidListOnTestPage;
    if (initList.size() > 1) {
      Collections.swap(initList, 0, initList.size() - 1);
    }
    return initList.toArray();
  }

  /**
   * Puts a new UID on the top of a root widgets on a page.
   * @param newUid new UID
   * @return array of entities
   */
  public static Object[] putNewRootWidgetOnTheTop(String newUid) {
    if (widgetsUidListOnTestPage.size() > 0) {
      List<String> uidList = widgetsUidListOnTestPage;
      uidList.add(0, newUid);
      return uidList.toArray();
    } else {
      return new String[]{newUid};
    }
  }

  /**
   * Puts a new UID on the top of the test widget children list.
   * @param newUid new UID
   * @return array of child
   */
  public static Object[] putNewChildWidgetToParentWidget(String newUid) {
    if (testWidgetChildren.length > 0) {
      List<String> uidList = new ArrayList<>(Collections.singletonList(
          testWidgetChildren[0].getUid()));
      uidList.add(0, newUid);
      return uidList.toArray();
    } else {
      return new String[]{newUid};
    }
  }

  /**
   * Deletes all drafts for the user on the test page.
   */
  public static void removeAllDraftsForUser() {
    Response response =
        given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
            .when().get(DRAFT_CONTROLLER_URL);
    if (response.statusCode() == 200) {
      given().spec(requestSpecification).pathParam("pageId", testPage.getId())
          .when().delete(DRAFT_CONTROLLER_URL)
          .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    }
  }

  /**
   * Generates a new UID.
   * @return new UID
   */
  public static String getNewUuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  public static RequestSpecification getRequestSpecification() {
    return requestSpecification;
  }

  public static Page getTestPage() {
    return testPage;
  }

  public static Widget getTestWidget() {
    return testWidget;
  }

  public static Property getTestProperty() {
    return testProperty;
  }

  public static Value getTestPropertyValue() {
    return testPropertyValue;
  }

  public static void setTestWidget(Widget testWidget) {
    TestDataHelper.testWidget = testWidget;
  }

  public static void setTestProperty(Property testProperty) {
    TestDataHelper.testProperty = testProperty;
  }

  public static void setTestPropertyValue(Value testPropertyValue) {
    TestDataHelper.testPropertyValue = testPropertyValue;
  }
}
