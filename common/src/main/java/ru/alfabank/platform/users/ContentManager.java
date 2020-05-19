package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL;
import static ru.alfabank.platform.businessobjects.enums.Team.SME;
import static ru.alfabank.platform.businessobjects.enums.Team.UNCLAIMED;

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
    return List.of(CREDIT_CARD, DEBIT_CARD, INVEST, MORTGAGE, PIL, SME, COMMON, UNCLAIMED);
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
