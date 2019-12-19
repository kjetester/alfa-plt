package ru.alfabank.platform.apitest;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.helpers.TestDataHelper.addQueryParamsToRequestSpec;
import static ru.alfabank.platform.helpers.TestDataHelper.getRequestSpecification;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestPage;
import static ru.alfabank.platform.helpers.TestDataHelper.removeAllDraftsForUser;
import static ru.alfabank.platform.helpers.TestDataHelper.setCityGroups;
import static ru.alfabank.platform.helpers.TestDataHelper.setPageList;
import static ru.alfabank.platform.helpers.TestDataHelper.setPagesWidgetMap;
import static ru.alfabank.platform.helpers.TestDataHelper.setRequestSpec;
import static ru.alfabank.platform.helpers.TestDataHelper.setTestObjects;

import java.util.ArrayList;
import java.util.List;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.alfabank.platform.businessobjects.Device;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;

public class BaseTest {

  protected static List<Object> operations = new ArrayList<>();
  protected static WrapperDraft body;

  protected String newEntityUid;

  /**
   * Set up.
   */
  @BeforeSuite
  public static void setUp() {
    setRequestSpec();
    setCityGroups();
    setPageList();
    setPagesWidgetMap(Device.desktop);
    // setWidgetMap(Device.mobile);
    setTestObjects();
    // addParamToRequestSpec("pageId", getTestPage().getId());
    addQueryParamsToRequestSpec("device", Device.desktop);
    removeAllDraftsForUser();
  }

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
          .spec(getRequestSpecification())
          .pathParam("pageId", getTestPage().getId())
          .get("content-store/admin-panel/pages/drafts/{pageId}");
    }
  }
}
