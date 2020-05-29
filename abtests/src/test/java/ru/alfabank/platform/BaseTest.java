package ru.alfabank.platform;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static ru.alfabank.platform.helpers.KeycloakHelper.logoutAllUsers;
import static ru.alfabank.platform.users.CommonUser.getCommonUser;
import static ru.alfabank.platform.users.CreditCardUser.getCreditCardUser;
import static ru.alfabank.platform.users.DebitCardUser.getDebitCardUser;
import static ru.alfabank.platform.users.InvestUser.getInvestUser;
import static ru.alfabank.platform.users.MortgageUser.getMortgageUser;
import static ru.alfabank.platform.users.PilUser.getPilUser;
import static ru.alfabank.platform.users.SmeUser.getSmeUser;
import static ru.alfabank.platform.users.UnclaimedUser.getUnclaimedUser;

import java.time.Instant;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import ru.alfabank.platform.steps.abtest.ExperimentSteps;
import ru.alfabank.platform.steps.abtest.OptionSteps;
import ru.alfabank.platform.steps.cs.DraftSteps;
import ru.alfabank.platform.steps.cs.PagesSteps;
import ru.alfabank.platform.users.CommonUser;
import ru.alfabank.platform.users.CreditCardUser;
import ru.alfabank.platform.users.DebitCardUser;
import ru.alfabank.platform.users.InvestUser;
import ru.alfabank.platform.users.MortgageUser;
import ru.alfabank.platform.users.PilUser;
import ru.alfabank.platform.users.SmeUser;
import ru.alfabank.platform.users.UnclaimedUser;

public class BaseTest {

  protected static final PagesSteps PAGES_STEPS = new PagesSteps();
  protected static final DraftSteps DRAFT_STEPS = new DraftSteps();
  protected static final ExperimentSteps EXPERIMENT_STEPS = new ExperimentSteps();
  protected static final OptionSteps OPTION_STEPS = new OptionSteps();
  protected static final CreditCardUser CREDIT_CARD_USER = getCreditCardUser();
  protected static final DebitCardUser DEBIT_CARD_USER = getDebitCardUser();
  protected static final InvestUser INVEST_USER = getInvestUser();
  protected static final MortgageUser MORTGAGE_USER = getMortgageUser();
  protected static final PilUser PIL_USER = getPilUser();
  protected static final SmeUser SME_USER = getSmeUser();
  protected static final CommonUser COMMON_USER = getCommonUser();
  protected static final UnclaimedUser UNCLAIMED_USER = getUnclaimedUser();

  /**
   * Clean up.
   */
  @AfterTest(description = "Удаление созданных экспериментов")
  public void afterTest() {
    EXPERIMENT_STEPS.deleteCreatedExperiments();
  }

  /**
   * Clean up.
   */
  @AfterSuite(description = "Удаление созданных страниц")
  public void afterSuite() {
    PAGES_STEPS.deleteCreatedPages();
    logoutAllUsers();
  }

  protected static Instant getCurrentDateTime() {
    return Instant.now();
  }

  protected String getValidExperimentEndDate() {
    return getCurrentDateTime()
        .plus(1, DAYS)
        .plus(3, MINUTES)
        .toString();
  }

  protected String getValidExperimentEndDatePlusWeek() {
    return getCurrentDateTime()
        .plus(7, DAYS)
        .toString();
  }

  protected String getInvalidExperimentEndDate() {
    return getCurrentDateTime()
        .plus(1, DAYS)
        .minus(1, MINUTES)
        .toString();
  }

  protected String getValidWidgetDateFrom() {
    return getCurrentDateTime().plus(3, HOURS).toString()
        .replaceAll("\\d{3}Z", "");
  }

  protected String getValidWidgetDateTo() {
    return getCurrentDateTime().plus(27, HOURS).plus(15, MINUTES).toString()
        .replaceAll("\\d{3}Z", "");
  }
}
