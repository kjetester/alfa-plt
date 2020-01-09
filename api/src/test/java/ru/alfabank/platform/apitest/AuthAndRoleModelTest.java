package ru.alfabank.platform.apitest;

import static io.restassured.RestAssured.given;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AuthAndRoleModelTest {

  private static final String KEYLOAK_BASE_URL = "https://keycloak.k8s.alfa.link";
  private static final String KEYLAOK_BASE_PATH =
      "/auth/realms/local_users/protocol/openid-connect/token";
  private static final String BASE_PATH =
      "http://feature-alfabankru-13879.cloud-content-store-service.reviews.ci.k8s.alfa.link/admin-panel/pages";

  private String fullAccessToken;
  private String limitedAccessToken;

  /**
   * Getting tokens.
   */
  @BeforeTest
  public void setTestSpec() {
    RequestSpecification fullAccessKeyloakSpec = new RequestSpecBuilder()
        .setRelaxedHTTPSValidation()
        .setBaseUri(KEYLOAK_BASE_URL)
        .setBasePath(KEYLAOK_BASE_PATH)
        .setContentType(ContentType.URLENC.toString())
        .setAccept(ContentType.ANY)
        .addFormParam("client_id","acms")
        .addFormParam("username","user1")
        .addFormParam("password","123")
        .addFormParam("grant_type","password")
        .log(LogDetail.ALL)
        .build();
    RequestSpecification limitedAccessKeyloakSpec = new RequestSpecBuilder()
        .addRequestSpecification(fullAccessKeyloakSpec)
        .removeFormParam("username")
        .addFormParam("username","user2")
        .build();
    fullAccessToken =
        given()
            .spec(fullAccessKeyloakSpec)
        .when()
            .post()
        .then()
            .statusCode(200).extract().body().jsonPath().get("access_token").toString();
    limitedAccessToken =
        given()
            .spec(limitedAccessKeyloakSpec)
        .when()
            .post()
        .then()
            .statusCode(200).extract().body().jsonPath().get("access_token").toString();
  }

  @Test
  public void successAuthTest() {
    given().auth().oauth2(fullAccessToken)
        .when().get(BASE_PATH)
        .then().statusCode(200);
  }

  @Test
  public void failAuthTest() {
    given().auth().oauth2("")
        .when().get(BASE_PATH)
        .then().statusCode(401);
    given()
        .when().get(BASE_PATH)
        .then().statusCode(401);
  }

  @Test
  public void limitationAuthTest() {
    given().auth().oauth2(limitedAccessToken)
        .when().get(BASE_PATH)
        .then().statusCode(403);
  }
}
