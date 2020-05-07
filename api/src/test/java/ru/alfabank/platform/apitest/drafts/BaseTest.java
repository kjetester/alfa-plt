package ru.alfabank.platform.apitest.drafts;

import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;
import ru.alfabank.platform.helpers.*;

import java.time.*;
import java.time.temporal.*;
import java.util.*;

import static io.restassured.RestAssured.*;

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
  public void afterClass() {
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
