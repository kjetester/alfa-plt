package ru.alfabank.platform.apitest.drafts.modify;

import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroup;
import static ru.alfabank.platform.helpers.TestDataHelper.*;

public class ModifyPropertyValueTest extends BaseTest {

	@BeforeClass
	public void makeDraft() {
		DataDraft valueData = new DataDraft.DataDraftBuilder()
			.forProperty(getTestProperty().getUid())
			.value("value")
			.cityGroups(getCityGroup("geo-5"))
			.build();
		operations.add(
			new WrapperDraft.OperationDraft(
				valueData, Entity.propertyValue, Method.change, getTestPropertyValue().getUid()));
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
		// JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);
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
