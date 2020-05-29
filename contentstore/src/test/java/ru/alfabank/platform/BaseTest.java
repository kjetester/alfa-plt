package ru.alfabank.platform;

import static ru.alfabank.platform.helpers.KeycloakHelper.logoutAllUsers;
import static ru.alfabank.platform.users.AuditRollbackUser.getAuditRollbackUser;
import static ru.alfabank.platform.users.AuditViewUser.getAuditViewUser;
import static ru.alfabank.platform.users.CommonUser.getCommonUser;
import static ru.alfabank.platform.users.ContentManager.getContentManager;
import static ru.alfabank.platform.users.CreditCardUser.getCreditCardUser;
import static ru.alfabank.platform.users.DebitCardUser.getDebitCardUser;
import static ru.alfabank.platform.users.InvestUser.getInvestUser;
import static ru.alfabank.platform.users.MortgageUser.getMortgageUser;
import static ru.alfabank.platform.users.PilUser.getPilUser;
import static ru.alfabank.platform.users.SmeUser.getSmeUser;
import static ru.alfabank.platform.users.UnclaimedUser.getUnclaimedUser;

import org.testng.annotations.AfterSuite;
import ru.alfabank.platform.steps.abtest.ExperimentSteps;
import ru.alfabank.platform.steps.abtest.OptionSteps;
import ru.alfabank.platform.steps.cs.AuditSteps;
import ru.alfabank.platform.steps.cs.DraftSteps;
import ru.alfabank.platform.steps.cs.PagesSteps;
import ru.alfabank.platform.users.AuditRollbackUser;
import ru.alfabank.platform.users.AuditViewUser;
import ru.alfabank.platform.users.CommonUser;
import ru.alfabank.platform.users.ContentManager;
import ru.alfabank.platform.users.CreditCardUser;
import ru.alfabank.platform.users.DebitCardUser;
import ru.alfabank.platform.users.InvestUser;
import ru.alfabank.platform.users.MortgageUser;
import ru.alfabank.platform.users.PilUser;
import ru.alfabank.platform.users.SmeUser;
import ru.alfabank.platform.users.UnclaimedUser;

public class BaseTest {

  protected static final PagesSteps PAGES_STEPS = new PagesSteps();
  protected static final AuditSteps AUDIT_STEPS = new AuditSteps();
  protected static final DraftSteps DRAFT_STEPS = new DraftSteps();

  protected static final ContentManager CONTENT_MANAGER = getContentManager();
  protected static final CreditCardUser CREDIT_CARD_USER = getCreditCardUser();
  protected static final DebitCardUser DEBIT_CARD_USER = getDebitCardUser();
  protected static final InvestUser INVEST_USER = getInvestUser();
  protected static final MortgageUser MORTGAGE_USER = getMortgageUser();
  protected static final PilUser PIL_USER = getPilUser();
  protected static final SmeUser SME_USER = getSmeUser();
  protected static final CommonUser COMMON_USER = getCommonUser();
  protected static final UnclaimedUser UNCLAIMED_USER = getUnclaimedUser();
  protected static final AuditViewUser AUDIT_VIEW_USER = getAuditViewUser();
  protected static final AuditRollbackUser AUDIT_ROLLBACK_USER = getAuditRollbackUser();

  /**
   * Clean up.
   */
  @AfterSuite(description = "Удаление созданных страниц")
  public void afterSuite() {
    PAGES_STEPS.deleteCreatedPages();
    logoutAllUsers();
  }
}
