package ru.alfabank.platform.widget;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.contentstore.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.utils.TestFailureListener;

@Listeners({TestFailureListener.class})
public class WidgetCreationTest extends ru.alfabank.platform.widget.BaseWidgetTest {

  private static final Logger LOGGER = LogManager.getLogger(WidgetCreationTest.class);

  @Test(description = """
      Тест создания виджета:
      \t1. На корневой странице
      \t2. С гео
      \t3. С пропсами
      """,
      groups = {"widget", "creationWidget"})
  public void widgetCreationTest() {
    // PRECONDITIONS //
    Page page = createNewPageInRoot();
    // STEPS //
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(page.getUri())
        .createNewWidget("MetaTitle")
        .closeWidgetSidebarAndGoToMainPage()
        .saveDraft()
        .publishDraft();
    //TODO: CHECKS

  }
}
