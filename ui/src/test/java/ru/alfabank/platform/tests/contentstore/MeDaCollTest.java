package ru.alfabank.platform.tests.contentstore;

import org.apache.commons.csv.*;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.*;
import org.testng.annotations.*;
import org.testng.log4testng.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.helpers.*;
import ru.alfabank.platform.tests.*;

import java.io.*;
import java.util.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class MeDaCollTest extends BaseTest {

  private Logger logger = Logger.getLogger(MeDaCollTest.class);

  @Test(
      testName = "Pages comparison test",
      groups = "collector",
      dataProvider = "resources")
  public void pagesComparisonTest(Resources resource) {
    logger.info("Checking '" + resource.getName() + "'");
    String expected;
    // expected page
    getDriver().get("http://develop.ci.k8s.alfa.link" + resource.getName());
    expected = getDriver().getPageSource();
    DriverHelper.killDriver();
    // actual page
    getDriver().get("http://develop.ci.k8s.alfa.link/api/v1/collector" + resource.getName());
    String actual = getDriver().getPageSource();
    DriverHelper.killDriver();
    //test
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test data provider.
   * @return test data
   * @throws IOException IOException
   */
  @DataProvider(name = "resources")
  public Object[][] resources() throws IOException {
    Object[][] arr = new Object[resourcesList().size()][1];
    for (int i = 0; i < resourcesList().size();i++) {
      arr[i][0] = resourcesList().get(i);
    }
    return arr;
  }

  private List<Resources> resourcesList() throws IOException {
    String fileName = "urls.csv";
    List<Resources> urlsList = new ArrayList<>();
    BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName)));
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
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
    // test
    Assertions.assertThat(actual).contains("status=500");
  }
}
