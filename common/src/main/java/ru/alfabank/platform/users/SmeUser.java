package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.PIL;
import static ru.alfabank.platform.businessobjects.enums.Team.SME;

import java.time.LocalDateTime;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class SmeUser extends User implements AccessibleUser {

  private static SmeUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_sme_user";
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
    return List.of(SME);
  }

  /**
   * Get user.
   * @return user
   */
  public static SmeUser getSmeUser() {
    if (user == null) {
      user = new SmeUser();
    }
    if (user.jwt == null
        || LocalDateTime.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
