package ru.alfabank.platform.apitest.drafts.create;

import org.testng.*;
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

public class CreatePropertyValueTest extends BaseTest {

	@BeforeClass
	public void makeDraft() {
		newEntityUid = getNewUuid();
		DataDraft newValueData = new DataDraft.DataDraftBuilder()
			.forProperty(getTestProperty().getUid())
			.value("")
			.cityGroups(getCityGroup("RU"))
			.enable(true)
			.build();
		operations.add(
			new WrapperDraft.OperationDraft(newValueData, Entity.propertyValue, Method.create, newEntityUid));
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
		createdEntities.put(Entity.propertyValue, newEntityUid);
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
			createdEntities.put(Entity.widget, newEntityUid);
	}
}
