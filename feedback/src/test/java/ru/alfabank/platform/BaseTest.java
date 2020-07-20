package ru.alfabank.platform;

import static ru.alfabank.platform.helpers.KeycloakHelper.logoutAllUsers;

import org.testng.annotations.AfterSuite;
import ru.alfabank.platform.steps.feedback.FeedbackSteps;

public class BaseTest {

  protected static final FeedbackSteps FEEDBACK_STEPS = new FeedbackSteps();

  @AfterSuite(description = "Logout All Users")
  public void afterSuite() {
    logoutAllUsers();
  }
}
