package ru.alfabank.platform.apitest.drafts;

import com.fasterxml.jackson.core.*;
import org.json.*;
import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static ru.alfabank.platform.businessobjects.CityGroup.*;
import static ru.alfabank.platform.businessobjects.Device.*;
import static ru.alfabank.platform.helpers.TestDataHelper.*;

public class CreateTest extends BaseTest {

	@Test(groups = "drafts")
	public void createValueTest() {
		// Make a Draft
		newEntityUid = getNewUuid();
		DataDraft newValueData = new DataDraft.DataDraftBuilder().cityGroups(getCityGroup("RU"))
			.forProperty(getTestProperty().getUid()).value("").enable(true).build();
		operations.add(new WrapperDraft.OperationDraft(newValueData, Entity.propertyValue, Method.create, getNewUuid()));
		body = new WrapperDraft(operations);
		// putting draft
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// getting draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// TODO JSONAssert.assertEquals(response.getBody().asString(), objMapper.writeValueAsString(body), true);
		// publishing draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// checking if draft is absent
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}

	@Test(groups = "drafts")
	public void createPropertyTest() {
		// Make a Draft
		newEntityUid = getNewUuid();
		DataDraft newPropertyData = new DataDraft.DataDraftBuilder().forWidget(getTestWidget().getUid())
			.name("newPropertyName").device(desktop).build();
		DataDraft newValueData = new DataDraft.DataDraftBuilder().cityGroups(getCityGroup("geo-5", "RU"))
			.forProperty(newEntityUid).value("").build();
		operations.addAll(Arrays.asList(
			new WrapperDraft.OperationDraft(newPropertyData, Entity.property, Method.create, newEntityUid),
			new WrapperDraft.OperationDraft(newValueData, Entity.propertyValue, Method.create, getNewUuid())
		));
		body = new WrapperDraft(operations);
		// putting draft
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// getting draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// TODO JSONAssert.assertEquals(response.getBody().asString(), objMapper.writeValueAsString(body), true);
		// publishing draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// checking if draft is absent
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}

	@Test(groups = "drafts")
	public void createRootWidgetTest() throws JsonProcessingException, JSONException {
		// Make a Draft
		newEntityUid = getNewUuid();
		DataDraft newWidgetData = new DataDraft.DataDraftBuilder().name("newWidget")
			.dateFrom("2019-01-01T00:00:00.000Z").dateTo("2020-01-01T00:00:00")
			.device(desktop).enable(true).localization("RU").state("changed")
			.cityGroups(getCityGroup("123")).build();
		DataDraft placementData = new DataDraft.DataDraftBuilder()
			.childUids(putNewChildWidgetToParentWidget(newEntityUid)).build();
		operations.addAll(Arrays.asList(
			new WrapperDraft.OperationDraft(newWidgetData, Entity.widget, Method.create, newEntityUid),
			new WrapperDraft.OperationDraft(placementData, Entity.page, Method.changeLinks, getTestPage().getId())));
		//TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-17612
		body = new WrapperDraft(operations);
		// putting draft
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// getting draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// TODO JSONAssert.assertEquals(response.getBody().asString(), objMapper.writeValueAsString(body), true);
		// publishing draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// checking if draft is absent
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}

	@Test(groups = "drafts")
	public void createChildWidgetTest() {
		newEntityUid = getNewUuid();
		// Make a draft
		DataDraft newWidgetData = new DataDraft.DataDraftBuilder().name("newWidget")
			.enable(true).localization("RU").device(desktop).dateFrom("2019-01-01T00:00:00.000Z")
			.dateTo("2020-01-01T00:00:00").cityGroups(getCityGroup("123")).build();
		DataDraft placementData = new DataDraft.DataDraftBuilder()
			.childUids(putNewChildWidgetToParentWidget(newEntityUid)).build();
		operations.addAll(Arrays.asList(
			new WrapperDraft.OperationDraft(newWidgetData, Entity.widget, Method.create, newEntityUid),
			new WrapperDraft.OperationDraft(placementData, Entity.widget, Method.changeLinks, getTestWidget().getUid())));
		//TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-17612
		body = new WrapperDraft(operations);
		// putting draft
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// getting draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// TODO JSONAssert.assertEquals(response.getBody().asString(), objMapper.writeValueAsString(body), true);
		// publishing draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// checking if draft is absent
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get("content-store/admin-panel/pages/drafts/{pageId}")
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}
}
