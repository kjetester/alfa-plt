package ru.alfabank.platform.tests.alfasite;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Cookie;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import ru.alfabank.platform.buisenessobjects.Resources;
import ru.alfabank.platform.tests.BaseTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static ru.alfabank.platform.helpers.DriverSingleton.getDriver;
import static ru.alfabank.platform.helpers.DriverSingleton.killDriver;

public class meDaCollTest extends BaseTest {

	private Logger logger = Logger.getLogger(meDaCollTest.class);

	@Test(
			testName = "Pages comparison test",
			groups = "collector",
			dataProvider = "resources")
	public void pagesComparisonTest(Resources resource) {
		logger.info("Checking '" + resource.getName() + "'");
		// expected page
		getDriver().get("http://develop.ci.k8s.alfa.link" + resource.getName());
		String expected = getDriver().getPageSource();
		killDriver();
		// actual page
		getDriver().get("http://develop.ci.k8s.alfa.link/api/v1/collector" + resource.getName());
		String actual = getDriver().getPageSource();
		killDriver();
		//test
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DataProvider(name = "resources")
	public Object[][] resources() throws IOException {
		Object[][] arr = new Object[resourcesList().size()][1];
		for(int i =0; i < resourcesList().size();i++){
			arr[i][0] = resourcesList().get(i);
		}
		return arr;
	}

	private List<Resources> resourcesList() throws IOException {
		String fileName = "urls.csv";
		List<Resources> urlsList = new ArrayList<>();
		BufferedReader bReader =
				new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName)));
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(bReader);
		for (CSVRecord record : records) {
			Resources resource = new Resources();
			resource.setUrl(record.get(0).replaceAll("[\"]", ""));
			urlsList.add(resource);
		}
		return urlsList;
	}


	@Test(
			testName = "Pages comparison test",
			groups = "collector")
	public void smeWithCookiePageTest() {
		getDriver().get("http://develop.ci.k8s.alfa.link/api/v1/collector/sme/");
		getDriver().manage().deleteAllCookies();
		getDriver().manage().addCookie(new Cookie("fallback-bypass","true"));
		getDriver().navigate().refresh();
		String actual = getDriver().getPageSource();
//		killDriver();
		//test
		Assertions.assertThat(actual).contains("status=500");
	}
}
