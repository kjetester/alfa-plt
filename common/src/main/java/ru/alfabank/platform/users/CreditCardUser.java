package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD_TEAM;

import java.time.Instant;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class CreditCardUser extends User implements AccessibleUser {

  private static CreditCardUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_credit_card_user";
  }

  @Override
  public String getPassword() {
    return "123";
  }

  @Override
  public AccessToken getJwt() {
    return jwt;
  }

  @Override
  public void setJwt(AccessToken jwt) {
    this.jwt = jwt;
  }

  @Override
  public List<Team> getTeams() {
    return List.of(CREDIT_CARD_TEAM);
  }

  /**
   * Get user.
   *
   * @return user
   */
  public static CreditCardUser getCreditCardUser() {
    if (user == null) {
      user = new CreditCardUser();
    }
    if (user.jwt == null
        || Instant.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
