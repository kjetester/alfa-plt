package ru.alfabank.platform.users;

import java.time.Instant;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class AuditViewUser extends User implements AccessibleUser {

  private static AuditViewUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_audit_view_user";
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
   *
   * @return user
   */
  public static AuditViewUser getAuditViewUser() {
    if (user == null) {
      user = new AuditViewUser();
    }
    if (user.jwt == null
        || Instant.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
