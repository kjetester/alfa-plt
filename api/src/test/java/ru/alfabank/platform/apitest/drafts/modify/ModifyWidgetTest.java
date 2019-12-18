package ru.alfabank.platform.apitest.drafts.modify;

import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroup;
import static ru.alfabank.platform.helpers.TestDataHelper.*;
import static ru.alfabank.platform.helpers.TestDataHelper.RESOURCE;

public class ModifyWidgetTest extends BaseTest {

	@BeforeClass
	public void makeDraft() {
		DataDraft widgetData = new DataDraft.DataDraftBuilder()
			.dateFrom("2019-01-01T00:00:00.000Z").dateTo("2020-01-01T00:00:00").device(getTestWidget().getDevice())
			.enable(getTestWidget().isEnabled()).localization(getTestWidget().getLocalization()).state("")
			.name(getTestWidget().getName()).cityGroups(getCityGroup("123")).build();
		operations.add(
			new WrapperDraft.OperationDraft(widgetData, Entity.widget, Method.change, getTestWidget().getUid()));
		body = new WrapperDraft(operations);
	}

	@Test
	public void saveTest() {
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
	}

	@Test(dependsOnMethods = "saveTest")
	public void getTest() {
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		//TODO: need to replace with rest-assured methods
	}

	@Test(dependsOnMethods = {"saveTest", "getTest"})
	public void publishTest() {
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
	}

	@Test(dependsOnMethods = "publishTest")
	public void absentPublishedDraftTest() {
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}
}
