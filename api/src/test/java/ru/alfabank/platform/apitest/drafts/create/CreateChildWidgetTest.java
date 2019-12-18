package ru.alfabank.platform.apitest.drafts.create;

import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroup;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.helpers.TestDataHelper.*;
import static ru.alfabank.platform.helpers.TestDataHelper.RESOURCE;

public class CreateChildWidgetTest extends BaseTest {

	@BeforeClass
	public void makeDraft() {
		newEntityUid = getNewUuid();
		DataDraft newWidgetData = new DataDraft.DataDraftBuilder()
			.name("ChildWidget_" + newEntityUid)
			.dateFrom("2019-01-01T00:00:00.000Z")
			.dateTo("2020-01-01T00:00:00")
			.device(desktop)
			.enable(true)
			.localization("RU")
			.cityGroups(getCityGroup("123"))
			.build();
		DataDraft placementData = new DataDraft.DataDraftBuilder()
			.childUids(putNewChildWidgetToParentWidget(newEntityUid))
			.build();
		operations.addAll(Arrays.asList(
			new WrapperDraft.OperationDraft(newWidgetData, Entity.widget, Method.create, newEntityUid),
			//TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-17612
			new WrapperDraft.OperationDraft(placementData, Entity.widget, Method.changeLinks, getTestWidget().getUid())));
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
		// TODO JSONAssert.assertEquals(response.getBody().asString(), objMapper.writeValueAsString(body), true);
	}

	@Test(dependsOnMethods = {"saveTest", "getTest"})
	public void publishTest() {
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post(RESOURCE + "/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
	}

	@Test(dependsOnMethods = "publishTest")
	public void absentPublishedDraftTest() {
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}
}
