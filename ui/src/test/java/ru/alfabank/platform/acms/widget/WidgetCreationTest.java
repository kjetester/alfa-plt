package ru.alfabank.platform.acms.widget;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.acms.WidgetSidebarPage;
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners ({TestFailureListener.class})
public class WidgetCreationTest extends ru.alfabank.platform.acms.widget.BaseWidgetTest {

  private static final Logger LOGGER = LogManager.getLogger(WidgetCreationTest.class);

  @Test(description = "Тест создания виджета:\n"
      + "\t1. На корневой странице\n"
      + "\t2. С гео\n"
      + "\t3. С пропсами\n",
      groups = {"widget","creationWidget"})
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
