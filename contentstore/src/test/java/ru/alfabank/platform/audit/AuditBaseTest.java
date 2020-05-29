package ru.alfabank.platform.audit;

import static ru.alfabank.platform.users.ContentManager.getContentManager;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.steps.cs.AuditSteps;
import ru.alfabank.platform.steps.cs.PagesSteps;

public class AuditBaseTest extends BaseTest {

  @BeforeTest
  public void beforeTest() {
    PAGES_STEPS.createEnabledPage(getContentManager());
  }

  @AfterTest
  public void afterTest() {
    PAGES_STEPS.deleteCreatedPages();
  }
}
