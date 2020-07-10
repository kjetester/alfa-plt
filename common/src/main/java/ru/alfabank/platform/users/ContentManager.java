package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.COMMON_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.SME_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.UNCLAIMED_TEAM;

import java.time.Instant;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class ContentManager extends User implements AccessibleUser {

  private static ContentManager user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_content_manager";
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
    return List.of(
        CREDIT_CARD_TEAM,
        DEBIT_CARD_TEAM,
        INVEST_TEAM,
        MORTGAGE_TEAM,
        PIL_TEAM,
        SME_TEAM,
        COMMON_TEAM,
        UNCLAIMED_TEAM);
  }

  /**
   * Get user.
   *
   * @return user
   */
  public static ContentManager getContentManager() {
    if (user == null) {
      user = new ContentManager();
    }
    if (user.jwt == null
        || Instant.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
