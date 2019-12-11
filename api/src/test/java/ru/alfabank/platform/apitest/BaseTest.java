package ru.alfabank.platform.apitest;

import com.fasterxml.jackson.databind.*;
import io.restassured.builder.*;
import io.restassured.http.*;
import io.restassured.specification.*;
import org.testng.annotations.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

import static io.restassured.RestAssured.*;

public class BaseTest {

	protected static RequestSpecification spec;
	protected static CityGroup cityGroupList;
	protected static List<Page> pageList;
	protected static Map<String, List<Widget>> widgetMap;
	protected ObjectMapper objMapper = new ObjectMapper();

	protected static Page testPage;
	protected static Widget testWidget;
	protected static Property testProperty;
	protected static Value testPropertyValue;

	@BeforeSuite
	public static void setUp() {
		spec = new RequestSpecBuilder()
				.setBaseUri ("http://develop.ci.k8s.alfa.link")
				.setBasePath ("api/v1")
				.addHeader("Authorization", "Basic YXNzcjpiWEdtUmllZVhNdXZhR2Jo")
				.setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON)
//				.log(LogDetail.URI)
				.build ();

		cityGroupList = given()
				.spec(spec)
				.when()
				.get("geo-group/city-groups")
				.then()
				.statusCode(200)
				.extract().as(CityGroup.class);

		pageList = Arrays.asList(given()
				.spec(spec)
				.when()
				.get("content-store/admin-panel/pages")
				.then()
				.statusCode(200)
				.extract().as(Page[].class));

		widgetMap = new HashMap<>();
		pageList.forEach(p -> {
			widgetMap.put(
					p.getId(),
					Arrays.asList(
							given()
									.spec(spec)
									.queryParams("device", Device.desktop)
									.queryParams("uri", p.getUri())
									.when()
									.get("/content-store/admin-panel/meta-info-page-contents")
									.then()
									.statusCode(200)
									.extract().as(Widget[].class)));
		});
		testPage = pageList.get(0);
		testWidget = widgetMap.get(testPage.getId()).get(0);
		testProperty = testWidget.getProperties().get(0);
		testPropertyValue = testProperty.getValues().get(0);
	}

	@AfterSuite
	public void tearDown() {
		given().spec(spec)
				.pathParam("pageId", testPage.getId())
				.get("content-store/admin-panel/pages/drafts/{pageId}");
	}
}