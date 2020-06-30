package ru.alfabank.platform.medacoll;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.contentstore.Page;
import ru.alfabank.platform.helpers.DriverHelper;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;
import ru.alfabank.platform.utils.DifferenceListener;

@Listeners(DifferenceListener.class)
public class MeDaCollTest extends MedacollBaseTest {

  private static final Logger LOGGER = LogManager.getLogger(MeDaCollTest.class);

  @Test(description = "Сравнение страниц полученных из 'ASSR' и из'Meta Data Collector'",
      testName = "Pages comparison test",
      dataProvider = "contentStorePagesUris")
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
}
