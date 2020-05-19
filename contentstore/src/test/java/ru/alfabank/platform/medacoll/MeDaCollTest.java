package ru.alfabank.platform.medacoll;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.helpers.DriverHelper;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;
import ru.alfabank.platform.utils.DifferenceListener;

@Listeners(DifferenceListener.class)
public class MeDaCollTest extends MedacollBaseTest {

  private static final Logger LOGGER = LogManager.getLogger(MeDaCollTest.class);

  @Test(description = "Сравнение страниц полученных из 'ASSR' и из'Meta Data Collector'",
      testName = "Pages comparison test",
      dataProvider = "pages")
  public void pagesComparisonTest(Page page, ITestContext context) {
    String pageUrl = page.getUri();
    LOGGER.info(String.format("Checking '%s'", pageUrl));
    context.setAttribute("case", pageUrl);
    PageFactory.initElements(getDriver(), AlfaSitePage.class).open(baseUri.concat(pageUrl));
    String expected = getDriver().getPageSource();
    context.setAttribute("expected", expected);
    DriverHelper.killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(String.format("%sapi/v1/collector/%s", baseUri, pageUrl));
    String actual = getDriver().getPageSource();
    context.setAttribute("actual", actual);
    DriverHelper.killDriver();
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test data provider.
   *
   * @return test data
   * @throws IOException IOException
   */
  @DataProvider(name = "pages")
  public Object[][] pages() throws IOException {
    Object[][] arr = new Object[resourcesList().size()][1];
    for (int i = 0; i < resourcesList().size(); i++) {
      arr[i][0] = resourcesList().get(i);
    }
    return arr;
  }

  private List<Page> resourcesList() throws IOException {
    String fileName = "urls.csv";
    List<Page> urlsList = new ArrayList<>();
    BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(
                this.getClass().getResourceAsStream("/" + fileName)));
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
    for (CSVRecord record : records) {
      Page resource = new Page(record.get(0));
      urlsList.add(resource);
    }
    return urlsList;
  }
}
