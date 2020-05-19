package ru.alfabank.platform.users;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.steps.BaseSteps.LOGGED_IN_USERS;

import io.restassured.http.ContentType;
import java.time.Instant;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.AccessToken;

public class User {

  protected static final Logger LOGGER = LogManager.getLogger(User.class);
  protected static final String KEYCLOAK_BASE_URL = "https://keycloak.k8s.alfa.link";
  protected static final String KEYCLOAK_BASE_URI =
      "/auth/realms/local_users/protocol/openid-connect";
  protected static final String TOKEN_RESOURCE = "/token";
  protected static final String LOGOUT_RESOURCE = "/logout";

  /**
   * Get or refresh access token.
   *
   * @param user user
   */
  public static void getAccessToken(final AccessibleUser user) {
    if (user.getJwt() == null || Instant.now().isAfter(user.getJwt().getExpireRefreshTokenTime())) {
      LOGGER.info("Выполняю запрос токена авторизации");
      Map<String, String> formParams = Map.of(
          "client_id", "acms",
          "username", user.getLogin(),
          "password", user.getPassword(),
          "grant_type", "password");
      user.setJwt(given().relaxedHTTPSValidation().baseUri(KEYCLOAK_BASE_URL)
          .basePath(KEYCLOAK_BASE_URI + TOKEN_RESOURCE).contentType(ContentType.URLENC)
          .formParams(formParams).when().post().then().statusCode(SC_OK).extract()
          .body().as(AccessToken.class));
      LOGGER.info("Получен ответ\n" + describeBusinessObject(user.getJwt()));
      if (LOGGED_IN_USERS.containsKey(user)) {
        LOGGED_IN_USERS.replace(user, user.getJwt());
      } else {
        LOGGED_IN_USERS.put(user, user.getJwt());
      }
    } else if (Instant.now().isAfter(user.getJwt().getExpireAccessTokenTime())) {
      LOGGED_IN_USERS.remove(user);
      LOGGER.info("Выполняю запрос обновления токена авторизации");
      Map<String, String> formParams = Map.of(
          "client_id", "acms",
          "grant_type", "refresh_token",
          "refresh_token", user.getJwt().getRefreshToken());
      user.setJwt(given().relaxedHTTPSValidation().baseUri(KEYCLOAK_BASE_URL)
          .basePath(KEYCLOAK_BASE_URI + TOKEN_RESOURCE).contentType(ContentType.URLENC)
          .formParams(formParams).when().post().then().statusCode(SC_OK).extract()
          .body().as(AccessToken.class));
      LOGGED_IN_USERS.put(user, user.getJwt());
    }
  }

  /**
   * Log out.
   */
  public static void logout(final AccessibleUser user) {
    if (user.getJwt() != null
        && Instant.now().isBefore(user.getJwt().getExpireAccessTokenTime())) {
      LOGGER.info("Выполняю разлогин");
      Map<String, String> formParams = Map.of(
          "client_id", "acms",
          "refresh_token", user.getJwt().getRefreshToken());
      given().relaxedHTTPSValidation().baseUri(KEYCLOAK_BASE_URL)
          .basePath(KEYCLOAK_BASE_URI + LOGOUT_RESOURCE).contentType(ContentType.URLENC)
          .formParams(formParams).log().all().when().post().then().log().ifError()
          .statusCode(SC_NO_CONTENT);
    } else {
      getAccessToken(user);
      logout(user);
    }
  }
}
