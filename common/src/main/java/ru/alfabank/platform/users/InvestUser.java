package ru.alfabank.platform.users;

import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;

import java.time.LocalDateTime;
import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public class InvestUser extends User implements AccessibleUser {

  private static InvestUser user;

  private AccessToken jwt;

  @Override
  public String getLogin() {
    return "test_invest_user";
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
    return List.of(INVEST);
  }

  /**
   * Get user.
   * @return user
   */
  public static InvestUser getInvestUser() {
    if (user == null) {
      user = new InvestUser();
    }
    if (user.jwt == null
        || LocalDateTime.now().isAfter(user.jwt.getExpireAccessTokenTime())) {
      getAccessToken(user);
    }
    return user;
  }
}
