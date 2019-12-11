package ru.alfabank.platform.apitest.drafts;

import com.fasterxml.jackson.core.*;
import io.restassured.response.Response;
import org.assertj.core.api.*;
import org.json.*;
import org.skyscreamer.jsonassert.*;
import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroups;

public class ModifyTest extends BaseTest {

	@Test
	public void ModifyPropertyValueTest() throws JSONException, JsonProcessingException {
		// Make a Draft
		List<PageDraft.Operations> operations = new ArrayList<>();
			operations.add(new PageDraft.Operations(
					new PageDraft.Operations.Data(testPropertyValue.getUid(),"",getCityGroups()),
					Entity.propertyValue,
					Method.change,
					testProperty.getUid()
			));
			PageDraft draft = new PageDraft(
					operations,
					"v1"
				);
			String body = objMapper.writeValueAsString(draft);
			// putting draft
			Response putDraftResponse = given()
					.spec(spec).body(body)
				.pathParam("pageId", testPage.getId())
				.put("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(putDraftResponse.getStatusCode()).as("putting draft").isEqualTo(200);
		// getting draft
		Response getDraftResponse = given().spec(spec)
				.pathParam("pageId", testPage.getId())
				.get("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(getDraftResponse.getStatusCode()).as("getting draft").isEqualTo(200);
		JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, false);
//		Assertions.assertThat(getDraftResponse.getBody().asString()).as("").isEqualTo(body);
		// posting draft
		Response postDraftResponce = given().spec(spec)
				.pathParam("pageId", testPage.getId())
				.post("content-store/admin-panel/pages/drafts/{pageId}/execute");
		Assertions.assertThat(postDraftResponce.getStatusCode()).as("posting draft").isEqualTo(200);
		// checking if draft is absent
		Response getAbsentDraftResponse = given().spec(spec)
				.pathParam("pageId", testPage.getId())
				.get("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(getAbsentDraftResponse.getStatusCode()).as("checking if draft is absent").isEqualTo(400);
	}

	@Test
	public void ModifyPropTest() {

	}

	@Test
	public void ModifyWidgetTest() {

	}

	@Test
	public void ModifyPageTest() {

	}
}
