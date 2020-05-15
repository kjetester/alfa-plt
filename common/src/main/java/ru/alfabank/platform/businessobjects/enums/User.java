package ru.alfabank.platform.businessobjects.enums;

import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL;
import static ru.alfabank.platform.businessobjects.enums.Team.SME;
import static ru.alfabank.platform.businessobjects.enums.Team.UNCLAIMED;

import java.util.List;
import org.apache.log4j.LogManager;

public enum User {

  CONTENT_MANAGER("content_manager", "123",
      List.of(CREDIT_CARD, DEBIT_CARD, INVEST, MORTGAGE, PIL, SME, COMMON, UNCLAIMED)),
  CREDIT_CARD_USER("credit_card_user", "123", List.of(CREDIT_CARD)),
  DEBIT_CARD_USER("debit_card_user", "123", List.of(DEBIT_CARD)),
  INVEST_USER("invest_user", "123", List.of(INVEST)),
  MORTGAGE_USER("mortgage_user", "123", List.of(MORTGAGE)),
  PIL_USER("pil_user", "123", List.of(PIL)),
  SME_USER("sme_user", "123", List.of(SME)),
  COMMON_USER("common_user", "123", List.of(COMMON)),
  UNCLAIMED_USER("unclaimed_user", "123", null),
  AUDIT_VIEW_USER("audit_viewer", "123", List.of()),
  AUDIT_ROLLBACK_USER("audit_rollbacker", "123", List.of());

  private final String login;
  private final String password;
  private final List<Team> teams;

  User(final String login, final String password, final List<Team> teams) {
    this.login = login;
    this.password = password;
    this.teams = teams;
    LogManager.getLogger(User.class).debug(
        String.format("Используется пользователь с логином '%s' и паролем '%s'", login, password));
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public List<Team> getTeams() {
    return teams;
  }
}
