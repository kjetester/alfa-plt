package ru.alfabank.platform;

import static ru.alfabank.platform.helpers.KeycloakHelper.logoutAllUsers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import ru.alfabank.platform.steps.abtest.ExperimentSteps;
import ru.alfabank.platform.steps.abtest.OptionSteps;
import ru.alfabank.platform.steps.cs.DraftSteps;
import ru.alfabank.platform.steps.cs.PagesSteps;

public class BaseTest {

  protected static final PagesSteps PAGES_STEPS = new PagesSteps();
  protected static final DraftSteps DRAFT_STEPS = new DraftSteps();
  protected static final ExperimentSteps EXPERIMENT_STEPS = new ExperimentSteps();
  protected static final OptionSteps OPTION_STEPS = new OptionSteps();

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

  protected String getInvalidEndDate() {
    return getCurrentDateTime()
        .plus(1, ChronoUnit.DAYS)
        .minus(1, ChronoUnit.MINUTES)
        .toString();
  }

  protected String getValidEndDate() {
    return getCurrentDateTime()
        .plus(1, ChronoUnit.DAYS)
        .plus(2, ChronoUnit.MINUTES)
        .toString();
  }

  protected Instant getDeadLine() {
    return getCurrentDateTime()
        .plus(1, ChronoUnit.DAYS)
        .plus(2, ChronoUnit.MINUTES)
        .minus(21, ChronoUnit.HOURS);
  }

  protected String getValidEndDatePlus10Minutes() {
    return getCurrentDateTime()
        .plus(1, ChronoUnit.DAYS)
        .plus(10, ChronoUnit.MINUTES)
        .toString();
  }

  protected String getValidEndDatePlusWeek() {
    return getCurrentDateTime()
        .plus(7, ChronoUnit.DAYS)
        .toString();
  }

  protected String getValidEndDatePlus10Seconds() {
    return getCurrentDateTime()
        .plusSeconds(10)
        .toString();
  }
}
