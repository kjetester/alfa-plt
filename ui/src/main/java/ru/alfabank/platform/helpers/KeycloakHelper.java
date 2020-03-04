package ru.alfabank.platform.helpers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;

import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.buisenessobjects.AccessToken;
import ru.alfabank.platform.buisenessobjects.User;

public class KeycloakHelper {

  private static final Logger LOGGER = LogManager.getLogger(KeycloakHelper.class);
  private static final String KEYCLOAK_BASE_URI = "https://keycloak.k8s.alfa.link";
  private static final String KEYCLOAK_BASE_PATH =
      "/auth/realms/local_users/protocol/openid-connect/token";

  private static AccessToken accessToken = null;

  /**
   * Refresh access token.
   * @param user user
   * @return actual valid token
   */
  public static AccessToken getToken(User user) {
    if (accessToken == null || accessToken.getExpireTime().isAfter(LocalDateTime.now())) {
      Map<String, String> formParams = new HashMap<String, String>() {{
          put("client_id", "acms");
          put("username", user.getLogin());
          put("password", user.getPassword());
          put("grant_type", "password");
        }};
      accessToken =
          given()
              .relaxedHTTPSValidation()
              .baseUri(KEYCLOAK_BASE_URI)
              .basePath(KEYCLOAK_BASE_PATH)
              .contentType(ContentType.URLENC)
              .formParams(formParams)
              .log().all()
          .when()
              .post()
          .then()
              .log().ifStatusCodeMatches(not(200))
              .extract().body().as(AccessToken.class);
      accessToken.setExpireTime(
          LocalDateTime.now().plus(accessToken.getExpiresIn(), ChronoUnit.SECONDS));
    }
    return accessToken;
  }
}
