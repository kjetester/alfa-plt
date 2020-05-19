package ru.alfabank.platform.audit;

import static ru.alfabank.platform.users.ContentManager.getContentManager;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.steps.cs.AuditSteps;
import ru.alfabank.platform.steps.cs.PagesSteps;

public class AuditBaseTest extends BaseTest {

  private static final PagesSteps PAGE_STEP = new PagesSteps();
  protected static final AuditSteps AUDIT_STEP = new AuditSteps();

  @BeforeTest
  public void beforeTest() {
    PAGE_STEP.createEnabledPage(getContentManager());
  }

  @AfterTest
  public void afterTest() {
    PAGE_STEP.deleteCreatedPages();
  }
}
