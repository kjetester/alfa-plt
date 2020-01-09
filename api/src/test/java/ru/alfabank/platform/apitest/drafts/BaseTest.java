package ru.alfabank.platform.apitest.drafts;

import static io.restassured.RestAssured.given;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.alfabank.platform.businessobjects.Device;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;
import ru.alfabank.platform.helpers.TestDataHelper;

public class BaseTest {

  protected static List<Object> operations = new ArrayList<>();
  protected static WrapperDraft body;
  protected static Instant dateFrom = Instant.now().minus(10, ChronoUnit.DAYS);
  protected static Instant dateTo = Instant.now().plus(10, ChronoUnit.DAYS);

  protected String newEntityUid;

  /**
   * Set up.
   */
  @BeforeSuite(groups = "drafts")
  public static void setUp() {
    TestDataHelper.setRequestSpec();
    TestDataHelper.setCityGroups();
    TestDataHelper.setPageList();
    TestDataHelper.setPagesWidgetMap(Device.desktop);
    // setWidgetMap(Device.mobile);
    TestDataHelper.setTestObjects();
    // addParamToRequestSpec("pageId", getTestPage().getId());
    TestDataHelper.addQueryParamsToRequestSpec("device", Device.desktop);
    TestDataHelper.removeAllDraftsForUser();
  }

  /**
   * Clear operations.
   */
  @AfterClass(alwaysRun = true)
  public void afterTest() {
    operations.clear();
  }

  /**
   * Delete grafts for current User on the Page in case of test(s) failure.
   * @param context test context
   */
  @AfterSuite
  public void tearDown(final ITestContext context) {
    if (context.getFailedTests().size() > 0) {
      given()
          .spec(TestDataHelper.getRequestSpecification())
          .pathParam("pageId", TestDataHelper.getTestPage().getId())
          .get("content-store/admin-panel/pages/drafts/{pageId}");
    }
  }
}
