package ru.alfabank.platform.users;

import java.time.Instant;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class AuditRollbackUser extends User implements AccessibleUser {

  private static AuditRollbackUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_audit_rollback_user";
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
  public static AuditRollbackUser getAuditRollbackUser() {
    if (user == null) {
      user = new AuditRollbackUser();
    }
    if (user.jwt == null
        || Instant.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
