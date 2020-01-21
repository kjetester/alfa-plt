package ru.alfabank.platform.tests.acms;

import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.tests.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class WidgetDeleteTest extends BaseTest {

  private static final User USER = new User();

  /**
   * Opening page.
   */
  @BeforeClass
  @Parameters ({"testPageUri"})
  public void settingUp(String testPageUri) {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(USER.getLogin(), USER.getPassword())
        .openPagesTree()
        .selectPage(testPageUri);
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