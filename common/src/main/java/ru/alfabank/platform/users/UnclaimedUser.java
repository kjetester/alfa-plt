package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;

import java.time.LocalDateTime;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class UnclaimedUser extends User implements AccessibleUser {

  private static UnclaimedUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_unclaimed_user";
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
    return List.of();
  }

  /**
   * Get user.
   * @return user
   */
  public static UnclaimedUser getUnclaimedUser() {
    if (user == null) {
      user = new UnclaimedUser();
    }
    if (user.jwt == null
        || LocalDateTime.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
