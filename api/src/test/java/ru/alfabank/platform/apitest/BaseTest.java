package ru.alfabank.platform.apitest;

import com.fasterxml.jackson.databind.*;
import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;
import ru.alfabank.platform.helpers.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static ru.alfabank.platform.helpers.TestDataHelper.*;

public class BaseTest {

	protected static List<Object> operations = new ArrayList<>();
	protected static WrapperDraft body;

	protected ObjectMapper objMapper = new ObjectMapper();
	protected String newEntityUid;

	@BeforeSuite
	public static void setUp() {
		setRequestSpec();
		setCityGroups();
		setPageList();
		setWidgetMap(Device.desktop);
//		setWidgetMap(Device.mobile);
		setTestObjects();
//		addParamToRequestSpec("pageId", getTestPage().getId());
		addQueryParamsToRequestSpec("device", Device.desktop);
		removeAllDraftsForUser();
	}

	@AfterClass(alwaysRun = true)
	public void afterTest() {
		operations.clear();
	}

	@AfterSuite
	public void tearDown(final ITestContext iTestContext) {
		// deleting the created draft anyway if exist
		if (iTestContext.getFailedTests().size() > 0) {
			given()
				.spec(getRequestSpecification())
				.pathParam("pageId", getTestPage().getId())
				.get("content-store/admin-panel/pages/drafts/{pageId}");
		}
	}
}