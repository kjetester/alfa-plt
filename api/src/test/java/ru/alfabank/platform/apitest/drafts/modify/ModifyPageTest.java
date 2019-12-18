package ru.alfabank.platform.apitest.drafts.modify;

import com.fasterxml.jackson.core.*;
import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static ru.alfabank.platform.businessobjects.CityGroup.*;
import static ru.alfabank.platform.helpers.TestDataHelper.*;

public class ModifyPageTest extends BaseTest {

	@BeforeClass
	public void makeDraft() {
		DataDraft pageData = new DataDraft.DataDraftBuilder()
			.childUids(getSwappedOutersWidgetsUidArray())
			.build();
		operations.add(
			new WrapperDraft.OperationDraft(pageData, Entity.page, Method.changeLinks, getTestPage().getId()));
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
