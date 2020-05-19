package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;

import java.time.Instant;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class DebitCardUser extends User implements AccessibleUser {

  private static DebitCardUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_debit_card_user";
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
    return List.of(DEBIT_CARD);
  }

  /**
   * Get user.
   *
   * @return user
   */
  public static DebitCardUser getDebitCardUser() {
    if (user == null) {
      user = new DebitCardUser();
    }
    if (user.jwt == null
        || Instant.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
