package ru.alfabank.platform.tests.acms.widget;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners ({TestFailureListener.class})
public class WidgetCopyingTest extends BaseWidgetTest {

  private static final Logger LOGGER = LogManager.getLogger(WidgetCopyingTest.class);

  @Test(description = "Тест копирования виджета\n"
                      + "\tна корневую страницу\n"
                      + "\tс детьми\n"
                      + "\tс пропсами\n")
  public void widgetCopyingTest(final ITestContext testContext)
      throws InterruptedException, JSONException {
    LOGGER.info("Выполняю предусловия");
    Page page = createNewPageInRoot();
    LOGGER.info("Выполняю шаги");
    //TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-19598
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(sourcePage)
        .copyAllWidgets(sourcePage, page.getUri())
        .publishDraft();
    Thread.sleep(5_000L);
    LOGGER.info("Выполняю тест");
    compareCreatedAndSourcePages(page, testContext);
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + sourcePage);
    String expected = getDriver().getPageSource();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + page.getUri());
    String actual = getDriver().getPageSource();
    assertThat(expected).isEqualTo(actual);
  }
}
