package ru.alfabank.platform.helpers;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.not;

import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.User;

/**
 * Keycloak helper.
 */
public class KeycloakHelper {

  private static final Logger LOGGER = LogManager.getLogger(KeycloakHelper.class);
  private static final String KEYCLOAK_BASE_URL = "https://keycloak.k8s.alfa.link";
  private static final String KEYCLOAK_BASE_URI =
      "/auth/realms/local_users/protocol/openid-connect";
  private static final String TOKEN_RESOURCE = "/token";
  private static final String LOGOUT_RESOURCE = "/logout";

  private static AccessToken tokensSet = null;

  /**
   * Refresh access token.
   * @param user user
   * @return actual valid token
   */
  public static AccessToken getToken(final User user) {
    if (tokensSet == null || LocalDateTime.now().isAfter(tokensSet.getExpireRefreshTokenTime())) {
      LOGGER.info("Выполняю запрос токена авторизации");
      Map<String, String> formParams = Map.of(
          "client_id", "acms",
          "username", user.getLogin(),
          "password", user.getPassword(),
          "grant_type", "password");
      tokensSet = given().relaxedHTTPSValidation().baseUri(KEYCLOAK_BASE_URL)
              .basePath(KEYCLOAK_BASE_URI + TOKEN_RESOURCE).contentType(ContentType.URLENC)
              .formParams(formParams).log().all().when().post().then().log()
              .ifError().statusCode(SC_OK).extract().body().as(AccessToken.class);
      return tokensSet;
    } else if (LocalDateTime.now().isAfter(tokensSet.getExpireAccessTokenTime())) {
      LOGGER.info("Выполняю запрос обновления токена авторизации");
      Map<String, String> formParams = Map.of(
          "client_id", "acms",
          "grant_type", "refresh_token",
          "refresh_token", tokensSet.getRefreshToken());
      tokensSet = given().relaxedHTTPSValidation().baseUri(KEYCLOAK_BASE_URL)
          .basePath(KEYCLOAK_BASE_URI + TOKEN_RESOURCE).contentType(ContentType.URLENC)
          .formParams(formParams).log().all().when().post().then().log()
          .ifError().statusCode(SC_OK).extract().body().as(AccessToken.class);
    }
    return tokensSet;
  }

  /**
   * Log out.
   */
  public static void logout(final User user) {
    if (tokensSet != null && LocalDateTime.now().isBefore(tokensSet.getExpireAccessTokenTime())) {
      LOGGER.info("Выполняю разлогин");
      Map<String, String> formParams = Map.of(
          "client_id", "acms",
          "refresh_token", tokensSet.getRefreshToken());
      given().relaxedHTTPSValidation().baseUri(KEYCLOAK_BASE_URL)
          .basePath(KEYCLOAK_BASE_URI + LOGOUT_RESOURCE).contentType(ContentType.URLENC)
          .formParams(formParams).log().all().when().post().then().log().ifError()
          .statusCode(SC_NO_CONTENT);
    } else {
      getToken(user);
      logout(user);
    }
  }
}
