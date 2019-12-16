package ru.alfabank.platform.helpers;

import io.restassured.builder.*;
import io.restassured.filter.log.*;
import io.restassured.http.*;
import io.restassured.specification.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

import static io.restassured.RestAssured.*;

public class TestDataHelper {

	private static final String BASE_URL = "http://develop.ci.k8s.alfa.link";
	private static final String BASE_PATH = "api/v1";
	private static final String USERNAME = "assr";
	private static final String PASSWORD = "bXGmRieeXMuvaGbh";

	private static RequestSpecification requestSpecification;
	protected static Map<String, List<Widget>> widgetMap;
	protected static Page testPage;
	protected static Widget testWidget;
	protected static Property testProperty;
	protected static Value testPropertyValue;
	private static CityGroup cityGroup;
	private static List<Page> pageList;
	private static List<String> widgetsUidListOnTestPage = new ArrayList<>();
	private static Widget.Children[] testWidgetChildren;

	// sets the requests specification
	public static void setRequestSpec() {
		requestSpecification = new RequestSpecBuilder()
			.setBaseUri (BASE_URL)
			.setBasePath (BASE_PATH)
			.setAuth(basic(USERNAME, PASSWORD))
			.setContentType(ContentType.JSON)
			.setAccept(ContentType.JSON)
			.log(LogDetail.ALL)
			.build();
	}

	// adds new params to the request specification and rebuilds it
	public static void addParamToRequestSpec(String paramName, String... paramValue) {
		requestSpecification = new RequestSpecBuilder()
			.addRequestSpecification(requestSpecification)
			.addPathParam(paramName, paramValue)
			.build();
	}

	// adds new query params to the request specification and rebuilds it
	public static void addQueryParamsToRequestSpec(String paramName, Object... paramValue) {
		requestSpecification = new RequestSpecBuilder()
			.addRequestSpecification(requestSpecification)
			.addQueryParam(paramName, paramValue)
			.build();
	}

	// gets the test data (city groups)
	public static void setCityGroups() {
		cityGroup = given()
			.spec(requestSpecification)
			.when()
			.get("geo-group/city-groups")
			.then()
			.statusCode(200)
			.extract().as(CityGroup.class);
	}

	// gets the test data (pages)
	public static void setPageList() {
		pageList = Arrays.asList(given()
			.spec(requestSpecification)
			.when()
			.get("content-store/admin-panel/pages")
			.then()
			.statusCode(200)
			.extract().as(Page[].class));
	}

	// gets the test data (widgets + properties + values on every page)
	public static void setWidgetMap() {
		widgetMap = new HashMap<>();
		pageList.forEach(p -> {
			widgetMap.put(
				p.getId(),
				Arrays.asList(
					given()
						.spec(requestSpecification)
						.queryParams(
							"device", Device.desktop,
							"uri", p.getUri())
						.when()
						.get("/content-store/admin-panel/meta-info-page-contents")
						.then()
						.statusCode(200)
						.extract().as(Widget[].class)));
		});
	}

	// defines and sets test objects
	public static void setTestObjects() {
		try {
			if (pageList.size() > 0 && widgetMap.size() > 0) {
			  loop:
				for (Map.Entry<String, List<Widget>> page : widgetMap.entrySet()) {
					List<Widget> widgetList = page.getValue();
					for (int i = 0; i < widgetList.size(); i++) {
						if (widgetList.get(i).getProperties().size() > i) {
							if (widgetList.get(i).getProperties().get(i).getValues().size() > 0) {
								testPage = pageList.get(i);
								System.out.printf("Test Page has been set to: '%s'\n", testPage.getId());
								testWidget = widgetList.get(i);
								System.out.printf("Test Widget has been set to: '%s'\n", testWidget.getUid());
								testProperty = testWidget.getProperties().get(i);
								System.out.printf("Test Property has been set to: '%s'\n", testProperty.getUid());
								testPropertyValue = testProperty.getValues().get(i);
								System.out.printf("Test Value has been set to: '%s'\n", testPropertyValue.getUid());
								break loop;
							}
						}
					}
				}
			} else throw new NoTestDataException();
		} catch (NoTestDataException e) {
			System.out.println(e.getMessage());
		}
		widgetMap.get(testPage.getId()).forEach(widget -> widgetsUidListOnTestPage.add(widget.getUid()));
		System.out.println("The Widgets UIDs on the Test Page are: ");
		widgetsUidListOnTestPage.forEach(System.out::println);
		testWidgetChildren = testWidget.getChildren();
		System.out.println(
			testWidgetChildren.length > 0 ? "The Widget children are: " : "The Widget hasn't any child.");
		Arrays.asList(testWidgetChildren).forEach(System.out::println);
	}

	// swaps outers in an array
	public static Object[] getSwappedOutersWidgetsUidArray() {
		List<String> initList = widgetsUidListOnTestPage;
		if (initList.size() > 1) {
			Collections.swap(initList, 0, initList.size() - 1);
			return initList.toArray();
		} else {
			return initList.toArray();
		}
	}

	// puts a new UID on the top of a root widgets on a page
	public static Object[] putNewRootWidgetOnTheTop(String newUid) {
		if (widgetsUidListOnTestPage.size() > 0) {
			List<String> uidList = widgetsUidListOnTestPage;
			uidList.add(0, newUid);
			return uidList.toArray();
		} else return new String[]{newUid};
	}

	// puts a new UID on the top of the test widget children list
	public static Object[] putNewChildWidgetToParentWidget(String newUid) {
		if (testWidgetChildren.length > 0) {
			List<String> uidList = Arrays.asList(testWidgetChildren[0].getUid());
			uidList.add(0, newUid);
			return uidList.toArray();
		} else return new String[]{newUid};
	}

	// generates a new UUID
	public static String getNewUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static RequestSpecification getRequestSpecification() {
		return requestSpecification;
	}

	public static Page getTestPage() {
		return testPage;
	}

	public static Widget getTestWidget() {
		return testWidget;
	}

	public static Property getTestProperty() {
		return testProperty;
	}

	public static Value getTestPropertyValue() {
		return testPropertyValue;
	}
}