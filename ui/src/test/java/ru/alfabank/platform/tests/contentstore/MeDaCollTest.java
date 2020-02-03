package ru.alfabank.platform.tests.contentstore;

import org.apache.commons.csv.*;
import org.apache.log4j.*;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.*;
import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.helpers.*;
import ru.alfabank.platform.reporting.*;
import ru.alfabank.platform.tests.*;

import java.io.*;
import java.util.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

@Listeners(DifferenceListener.class)
public class MeDaCollTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(MeDaCollTest.class);

  @Test(testName = "Pages comparison test", dataProvider = "resource")
  public void pagesComparisonTest(Resources resource, ITestContext context) {
    LOGGER.info(String.format("Checking '%s'", resource.getName()));
    getDriver().get(baseUrl + resource.getName());
    String expected = getDriver().getPageSource();
    DriverHelper.killDriver();
    getDriver().get(String.format("%s/api/v1/collector%s", baseUrl, resource.getName()));
    String actual = getDriver().getPageSource();
    DriverHelper.killDriver();
    context.setAttribute("case", resource.getName());
    context.setAttribute("expected", expected);
    context.setAttribute("actual", actual);
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  @Test(testName = "Pages comparison test", enabled = false)
  public void smeWithCookiePageTest() {
    getDriver().get("http://develop.ci.k8s.alfa.link/api/v1/collector/sme/");
    getDriver().manage().deleteAllCookies();
    getDriver().manage().addCookie(new Cookie("fallback-bypass","true"));
    getDriver().navigate().refresh();
    String actual = getDriver().getPageSource();
    // test
    Assertions.assertThat(actual).contains("status=500");
  }

  /**
   * Test data provider.
   * @return test data
   * @throws IOException IOException
   */
  @DataProvider(name = "resource")
  public Object[][] resource() throws IOException {
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
            new InputStreamReader(
                this.getClass().getResourceAsStream("/" + fileName)));
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
    for (CSVRecord record : records) {
      Resources resource = new Resources();
      resource.setUrl(record.get(0).replaceAll("[\"]", ""));
      urlsList.add(resource);
    }
    return urlsList;
  }
}
