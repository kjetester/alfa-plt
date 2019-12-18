package ru.alfabank.platform.apitest.drafts.delete;

import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.helpers.TestDataHelper.*;
import static ru.alfabank.platform.helpers.TestDataHelper.RESOURCE;

public class DeletePropertyValueTest extends BaseTest {

	@BeforeClass
	public void makeDraft() {
		operations.add(new WrapperDraft.OperationDraft(
			null, Entity.propertyValue, Method.delete, createdEntities.get(Entity.propertyValue)));
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
			.when().post(RESOURCE + "/execute")
			.then().log().ifStatusCodeMatches(not(200)).statusCode(200);
	}

	@Test(dependsOnMethods = "publishTest")
	public void absentPublishedDraftTest() {
		given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
			.when().get(RESOURCE)
			.then().log().ifStatusCodeMatches(not(404)).statusCode(404);
	}

	@AfterClass
	public void setCreatedValue(ITestContext context) {
		if (context.getFailedTests().size() == 0)
			createdEntities.remove(Entity.propertyValue);
	}
}
