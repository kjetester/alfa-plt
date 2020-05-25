package ru.alfabank.platform;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static ru.alfabank.platform.helpers.KeycloakHelper.logoutAllUsers;

import java.time.Instant;
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
