package ru.alfabank.platform.tests.acms.widget;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.time.LocalDateTime;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners ({TestFailureListener.class})
public class WidgetCreationTest extends BaseWidgetTest {

  private static final Logger LOGGER = LogManager.getLogger(WidgetCreationTest.class);

  @Test(description = "Тест создания виджета\n"
                    + "\tна корневой странице\n"
                    + "\tс гео\n"
                    + "\tс пропсами\n")
  public void widgetCreationTest() throws InterruptedException {
    LOGGER.info("Выполняю предусловия");
    Page page = createNewPageInRoot();
    LOGGER.info("Выполняю шаги");
    final String pageTitle = "The Test Widget on the Test Page";
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage(page.getUri())
        .createNewWidget("MetaTitle")
        .createNewProperty("title")
        .modifyValue("title", pageTitle, "RU")
        .submitChanges()
        .saveDraft()
        .publishDraft();
    LOGGER.info("Выполняю тест");
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(basePage + page.getUri())
        .checkPageTitleAfter(LocalDateTime.now(), pageTitle, "MSK_MO");
  }
}
