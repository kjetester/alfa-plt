package ru.alfabank.platform.apitest.drafts;

import com.fasterxml.jackson.core.*;
import io.restassured.response.*;
import org.assertj.core.api.*;
import org.json.*;
import org.skyscreamer.jsonassert.*;
import org.testng.annotations.*;
import ru.alfabank.platform.apitest.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.property.*;
import ru.alfabank.platform.businessobjects.draft.value.*;
import ru.alfabank.platform.businessobjects.draft.widget.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static ru.alfabank.platform.businessobjects.CityGroup.*;

public class ModifyTest extends BaseTest {

	@Test
	public void modifyPropertyValueTest() throws JsonProcessingException, JSONException {
		// Make a Draft (all (geo and value) change)
		List<ValueDraft.Operations> operations = new ArrayList<>();
		operations.add(
			new ValueDraft.Operations(
				new ValueDraft.Operations.Data(
					getCityGroup("geo-5"),
					testProperty.getUid(),
					"q"),
				Entity.propertyValue,
				Method.change,
				testPropertyValue.getUid()));
		ValueDraft draft = new ValueDraft(operations, "01");
		String body = objMapper.writeValueAsString(draft);
		// putting draft
		Response putDraftResponse = given()
			.spec(spec).body(body)
			.pathParam("pageId", testPage.getId())
			.put("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(putDraftResponse.getStatusCode())
			.as(putDraftResponse.getBody().asString() + "\n" + body)
		.isEqualTo(200);
		// getting draft
		Response getDraftResponse = given().spec(spec)
			.pathParam("pageId", testPage.getId())
			.get("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(getDraftResponse.getStatusCode())
			.as(getDraftResponse.getBody().asString() + "\n" + body)
			.isEqualTo(200);
		JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);
		// publishing draft
		Response postDraftResponse = given().spec(spec)
			.pathParam("pageId", testPage.getId())
			.post("content-store/admin-panel/pages/drafts/{pageId}/execute");
		Assertions.assertThat(
			postDraftResponse.getStatusCode())
			.as(postDraftResponse.getBody().asString() + "\n" + body)
			.isEqualTo(200);
		// checking if draft is absent
		Response getAbsentDraftResponse = given().spec(spec)
			.pathParam("pageId", testPage.getId())
			.get("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(getAbsentDraftResponse.getStatusCode())
			.as(getAbsentDraftResponse.getBody().asString())
			.isEqualTo(404);
	}

	@Test
	public void modifyPropertyTest() throws JsonProcessingException, JSONException {
		// Make a Draft (device change)
		List<PropertyDraft.Operations> operations = new ArrayList<>();
		operations.add(
			new PropertyDraft.Operations(
				new PropertyDraft.Operations.Data(
					testWidget.getUid(),
					testProperty.getName(),
					Device.mobile
					),
				Entity.property,
				Method.change,
				testProperty.getUid()
			));
		PropertyDraft draft = new PropertyDraft(operations, "01");
		String body = objMapper.writeValueAsString(draft);
		// putting draft
		Response putDraftResponse = given()
			.spec(spec).body(body)
			.pathParam("pageId", testPage.getId())
			.put("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(putDraftResponse.getStatusCode())
			.as(putDraftResponse.getBody().asString() + "\n" + body)
			.isEqualTo(200);
		// getting draft
		Response getDraftResponse = given().spec(spec)
			.pathParam("pageId", testPage.getId())
			.get("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(getDraftResponse.getStatusCode())
			.as(getDraftResponse.getBody().asString() + "\n" + body)
			.isEqualTo(200);
		JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);
		// publishing draft
		Response postDraftResponse = given().spec(spec)
			.pathParam("pageId", testPage.getId())
			.post("content-store/admin-panel/pages/drafts/{pageId}/execute");
		Assertions.assertThat(
			postDraftResponse.getStatusCode())
			.as(postDraftResponse.getBody().asString() + "\n" + body)
			.isEqualTo(200);
		// checking if draft is absent
		Response getAbsentDraftResponse = given().spec(spec)
			.pathParam("pageId", testPage.getId())
			.get("content-store/admin-panel/pages/drafts/{pageId}");
		Assertions.assertThat(getAbsentDraftResponse.getStatusCode())
			.as(getAbsentDraftResponse.getBody().asString())
			.isEqualTo(404);
	}

	@Test
	public void modifyWidgetTest() throws JsonProcessingException, JSONException {
			// Make a Draft (dates change)
			List<WidgetDraft.Operations> operations = new ArrayList<>();
			operations.add(
				new WidgetDraft.Operations(
					testWidget.getUid(),
					Entity.widget,
					Method.change,
					new WidgetDraft.Operations.Data(
						"2019-01-01T00:00:00.000Z",
						"2020-01-01T00:00:00",
						Device.desktop,
						testWidget.isEnabled(),
						testWidget.getLocalization(),
						"changed",
						testWidget.getName(),
						getCityGroup("123"))
				));
			String body = objMapper.writeValueAsString(
				new WidgetDraft("01", operations));
			// putting draft
			Response putDraftResponse = given()
				.spec(spec).body(body)
				.pathParam("pageId", testPage.getId())
				.put("content-store/admin-panel/pages/drafts/{pageId}");
			Assertions.assertThat(putDraftResponse.getStatusCode())
				.as(putDraftResponse.getBody().asString() + "\n" + body)
				.isEqualTo(200);
			// getting draft
			Response getDraftResponse = given().spec(spec)
				.pathParam("pageId", testPage.getId())
				.get("content-store/admin-panel/pages/drafts/{pageId}");
			Assertions.assertThat(getDraftResponse.getStatusCode())
				.as(getDraftResponse.getBody().asString() + "\n" + body)
				.isEqualTo(200);
			JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);
			// publishing draft
			Response postDraftResponse = given().spec(spec)
				.pathParam("pageId", testPage.getId())
				.post("content-store/admin-panel/pages/drafts/{pageId}/execute");
			Assertions.assertThat(
				postDraftResponse.getStatusCode())
				.as(postDraftResponse.getBody().asString() + "\n" + body)
				.isEqualTo(200);
			// checking if draft is absent
			Response getAbsentDraftResponse = given().spec(spec)
				.pathParam("pageId", testPage.getId())
				.get("content-store/admin-panel/pages/drafts/{pageId}");
			Assertions.assertThat(getAbsentDraftResponse.getStatusCode())
				.as(getAbsentDraftResponse.getBody().asString())
				.isEqualTo(404);
	}

	@Test
	public void modifyPageTest() {

	}
}
