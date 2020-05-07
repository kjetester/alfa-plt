package ru.alfabank.platform.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners({TestFailureListener.class})
public class WidgetDeleteTest extends BaseTest {

  /**
   * Opening page.
   */
  @BeforeClass
  public void settingUp() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage("sme-new/");
  }

  @Test(description = "Deletion a not shared widget hasn't a child")
  public void deletionNotSharedWidgetHasNoChildTest() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .deleteNonSharedWidgetHasNoChildren()
        .saveDraft();
    //TODO: no creation method .publishDraft()
  }

  @Test(description = "Deletion a not shared widget has some childs")
  public void deletionNotSharedWidgetHasChildTest() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .deleteNonSharedWidgetHasChildren()
        .saveDraft();
    //TODO: no creation method .publishDraft()
  }
}
