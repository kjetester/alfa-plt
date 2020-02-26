package ru.alfabank.platform.tests.acms;

import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.reporting.*;
import ru.alfabank.platform.tests.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

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
