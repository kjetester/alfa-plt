package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.PIL;

import java.time.Instant;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class PilUser extends User implements AccessibleUser {

  private static PilUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_pil_user";
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
    return List.of(PIL);
  }

  /**
   * Get user.
   *
   * @return user
   */
  public static PilUser getPilUser() {
    if (user == null) {
      user = new PilUser();
    }
    if (user.jwt == null
        || Instant.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
