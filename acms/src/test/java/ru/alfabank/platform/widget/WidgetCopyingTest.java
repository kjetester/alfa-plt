package ru.alfabank.platform.widget;

import static ru.alfabank.platform.businessobjects.enums.CopyMethod.COPY;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.utils.TestFailureListener;

@Listeners ({TestFailureListener.class})
public class WidgetCopyingTest extends ru.alfabank.platform.widget.BaseWidgetTest {

  private Page createdPage;

  /**
   * Preconditions.
   */
  @BeforeMethod(
      description = "Выполнение предусловий для widgetCopyingTest:\n"
          + "\t1. Создание новой страницы с пустым полем dateFrom в корне страницы '/qr/'",
      onlyForGroups = "copyingWidget",
      alwaysRun = true)
  public void beforeMethod() {
    // PRECONDITIONS //
    createdPage = createNewPageInRoot();
  }

  @Test(
      description = "Тест копирования виджета:\n"
          + "\t1. На корневую страницу\n"
          + "\t2. С детьми\n"
          + "\t3. С пропсами\n",
      groups = {"widget", "copyingWidget"})
  public void widgetCopyingTest() throws InterruptedException {
    // STEPS //
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(sourcePage.getUri())
        .copyAllWidgets(sourcePage, createdPage)
        .publishDraft();
    TimeUnit.SECONDS.sleep(5);
    // CHECKS //
    comparePagesInPageController(createdPage);
    comparePagesMetaInfoContentPageController(createdPage, COPY);
    comparePagesInContentPageController(createdPage, COPY);
  }
}
