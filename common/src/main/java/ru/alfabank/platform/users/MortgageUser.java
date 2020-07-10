package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE_TEAM;

import java.time.Instant;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class MortgageUser extends User implements AccessibleUser {

  private static MortgageUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_mortgage_user";
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
    return List.of(MORTGAGE_TEAM);
  }

  /**
   * Get user.
   *
   * @return user
   */
  public static MortgageUser getMortgageUser() {
    if (user == null) {
      user = new MortgageUser();
    }
    if (user.jwt == null
        || Instant.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
