package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;

import java.time.LocalDateTime;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class CommonUser extends User implements AccessibleUser {

  private static CommonUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_common_user";
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
    return List.of(COMMON);
  }

  /**
   * Get user.
   * @return user
   */
  public static CommonUser getCommonUser() {
    if (user == null) {
      user = new CommonUser();
    }
    if (user.jwt == null
        || LocalDateTime.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
