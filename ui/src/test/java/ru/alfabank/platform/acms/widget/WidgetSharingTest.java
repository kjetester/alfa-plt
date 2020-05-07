package ru.alfabank.platform.acms.widget;

import static ru.alfabank.platform.buisenessobjects.enums.CopyMethod.SHARE;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners ({TestFailureListener.class})
public class WidgetSharingTest extends ru.alfabank.platform.acms.widget.BaseWidgetTest {

  @Test (description = "Тест шаринга виджета:\n"
      + "\t1. На корневую страницу\n"
      + "\t2. С детьми\n"
      + "\t3. С пропсами\n",
      groups = {"widget","sharingWidget"})
  public void widgetSharingWithChildren() throws InterruptedException {
    // PRECONDITIONS
    Page createdPage = createNewPageInRoot();
    // STEPS
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(sourcePage.getUri())
        .shareAllWidgets(sourcePage, createdPage)
        .publishDraft();
    TimeUnit.SECONDS.sleep(5);
    // CHECKS
    // CHECKS //
    comparePagesInPageController(createdPage);
    comparePagesMetaInfoContentPageController(createdPage, SHARE);
    comparePagesInContentPageController(createdPage, SHARE);
  }
}
