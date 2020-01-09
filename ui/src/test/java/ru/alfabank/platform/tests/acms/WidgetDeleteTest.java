package ru.alfabank.platform.tests.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.User;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.tests.BaseTest;

public class WidgetDeleteTest extends BaseTest {

  private static final String PAGE_URI = "alfacard-test";
  private static User user = new User();

  /**
   * Opening page.
   */
  @BeforeClass
  public void settingUp() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(PAGE_URI);
  }

  @Test(description = "Deletion a not shared widget hasn't a child")
  public void deletionNotSharedWidgetHasNoChildTest() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .deleteNonSharedWidgetHasNoChilds()
        .saveDraft();
    //TODO: no creation method .publishDraft()
  }

  @Test(description = "Deletion a not shared widget has some childs")
  public void deletionNotSharedWidgetHasChildTest() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .deleteNonSharedWidgetHasChilds()
        .saveDraft();
    //TODO: no creation method .publishDraft()
  }
}
