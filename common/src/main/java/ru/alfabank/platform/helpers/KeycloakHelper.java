package ru.alfabank.platform.helpers;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static ru.alfabank.platform.users.CommonUser.getCommonUser;
import static ru.alfabank.platform.users.CreditCardUser.getCreditCardUser;
import static ru.alfabank.platform.users.DebitCardUser.getDebitCardUser;
import static ru.alfabank.platform.users.InvestUser.getInvestUser;
import static ru.alfabank.platform.users.MortgageUser.getMortgageUser;
import static ru.alfabank.platform.users.PilUser.getPilUser;
import static ru.alfabank.platform.users.SmeUser.getSmeUser;
import static ru.alfabank.platform.users.UnclaimedUser.getUnclaimedUser;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.users.AccessibleUser;
import ru.alfabank.platform.users.CommonUser;
import ru.alfabank.platform.users.CreditCardUser;
import ru.alfabank.platform.users.DebitCardUser;
import ru.alfabank.platform.users.InvestUser;
import ru.alfabank.platform.users.MortgageUser;
import ru.alfabank.platform.users.PilUser;
import ru.alfabank.platform.users.SmeUser;
import ru.alfabank.platform.users.UnclaimedUser;

/**
 * Keycloak helper.
 */
public class KeycloakHelper extends AbstractBusinessObject {

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
  public static AccessToken getToken(final AccessibleUser user) {
    if (tokensSet == null
        || LocalDateTime.now().isAfter(tokensSet.getExpireRefreshTokenTime())
        || !decode(tokensSet.getAccessToken()).equals(user.getLogin())) {
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
      LOGGER.info("Получен ответ\n" + describeBusinessObject(tokensSet));
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
   * Decode JWT.
   * @param accessToken accessToken
   * @return login
   */
  private static String decode(String accessToken) {
    try {
      return JWT.decode(accessToken)
          .getClaims()
          .get("name")
          .asString()
          .toLowerCase()
          .replaceAll(" ", "_");
    } catch (JWTDecodeException exception) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Log out.
   */
  public static void logoutAllUsers() {
    final var users = List.of(
        getCreditCardUser(),
        getDebitCardUser(),
        getInvestUser(),
        getMortgageUser(),
        getPilUser(),
        getSmeUser(),
        getCommonUser(),
        getUnclaimedUser());
    for (AccessibleUser user : users) {
      LOGGER.info("Выполняю разлогин");
      Map<String, String> formParams = Map.of(
          "client_id", "acms",
          "refresh_token", user.getJwt().getRefreshToken());
      given().relaxedHTTPSValidation().baseUri(KEYCLOAK_BASE_URL)
          .basePath(KEYCLOAK_BASE_URI + LOGOUT_RESOURCE).contentType(ContentType.URLENC)
          .formParams(formParams).log().all().when().post().then().log().ifError()
          .statusCode(SC_NO_CONTENT);
    }
  }
}
