package ru.alfabank.platform.apitest.drafts;

import com.fasterxml.jackson.core.*;
import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static ru.alfabank.platform.businessobjects.CityGroup.*;
import static ru.alfabank.platform.helpers.TestDataHelper.*;

public class ModifyTest extends BaseTest {

	@Test(groups = "drafts")
	public void modifyValueTest() {
		// Make a Draft (all (geo and value) change)
		DataDraft valueData = new DataDraft.DataDraftBuilder().cityGroups(getCityGroup("geo-5"))
			.forProperty(getTestProperty().getUid()).value("value").build();
		operations.add(
			new WrapperDraft.OperationDraft(
				valueData, Entity.propertyValue, Method.change, getTestPropertyValue().getUid()));
		body = new WrapperDraft(operations);
		// putting draft
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// getting draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		//TODO: need to replace with rest-assured methods
		// JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);

		// publishing draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// checking if draft is absent
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}

	@Test(groups = "drafts")
	public void modifyPropertyTest() {
		// Make a Draft (device change)
		DataDraft propertyData = new DataDraft.DataDraftBuilder().forWidget(getTestWidget().getUid())
			.name(getTestProperty().getName()).device(Device.mobile).build();
		operations.add(
			new WrapperDraft.OperationDraft(propertyData, Entity.property, Method.change, getTestProperty().getUid()));
		body = new WrapperDraft(operations);
		// putting draft
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// getting draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		//TODO: need to replace with rest-assured methods
		// JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);

		// publishing draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// checking if draft is absent
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}

	@Test(groups = "drafts")
	public void modifyWidgetTest() {
		// Make a Draft (dates change)
		DataDraft widgetData = new DataDraft.DataDraftBuilder()
			.dateFrom("2019-01-01T00:00:00.000Z").dateTo("2020-01-01T00:00:00").device(getTestWidget().getDevice())
			.enable(getTestWidget().isEnabled()).localization(getTestWidget().getLocalization()).state("")
			.name(getTestWidget().getName()).cityGroups(getCityGroup("123")).build();
		operations.add(
			new WrapperDraft.OperationDraft(widgetData, Entity.widget, Method.change, getTestWidget().getUid()));
		body = new WrapperDraft(operations);
		// putting draft
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// getting draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		//TODO: need to replace with rest-assured methods
		// JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);

		// publishing draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// checking if draft is absent
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}

	@Test(groups = "drafts")
	public void modifyPageTest() throws JsonProcessingException {
		// Make a Draft (widgets order change)
		DataDraft pageData = new DataDraft.DataDraftBuilder()
			.childUids(getSwappedOutersWidgetsUidArray())
			.build();
		operations.add(
			new WrapperDraft.OperationDraft(pageData, Entity.page, Method.changeLinks, getTestPage().getId()));
		body = new WrapperDraft(operations);
		// putting draft
		given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
			.when().put(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// getting draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		//TODO: need to replace with rest-assured methods
		// JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);

		// publishing draft
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().post("content-store/admin-panel/pages/drafts/{pageId}/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
		// checking if draft is absent
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}
}
