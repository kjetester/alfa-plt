package ru.alfabank.platform.tests.acms.widget;

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
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners ({TestFailureListener.class})
public class WidgetSharingTest extends BaseWidgetTest {

  private static final Logger LOGGER = LogManager.getLogger(WidgetSharingTest.class);

  @Test (description = "Тест шаринга виджета\n"
                    + "\tна корневую страницу\n"
                    + "\tс детьми\n")
  public void widgetSharingWithChildren(final ITestContext testContext)
      throws InterruptedException, JSONException {
    LOGGER.info("Выполняю предусловия");
    Page page = createNewPageInRoot();
    LOGGER.info("Выполняю шаги");
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(sourcePage)
        .shareAllWidgets(sourcePage, page.getUri())
        .publishDraft();
    Thread.sleep(5_000L);
    LOGGER.info("Выполняю тест");
    compareCreatedAndSourcePages(page, testContext);
  }
}
