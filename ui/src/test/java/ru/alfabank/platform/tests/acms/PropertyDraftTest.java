package ru.alfabank.platform.tests.acms;

import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.reporting.*;
import ru.alfabank.platform.tests.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

@Listeners({TestFailureListener.class})
public class PropertyDraftTest extends BaseTest {

  User user = new User();

  private String widgetName = "MetaTitle";
  private String propName = "newPropName";

  /**
   * Opening acms.
   */
  @BeforeClass (alwaysRun = true)
  public void settingUp() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage("sme-new/");
  }

  @Test(
      description = "Тест добавления нового Property")
  public void addNewPropertyTest() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(widgetName)
        .createNewProperty(propName)
        .checkIfPropertyWasAdded(propName)
        .submitChanges()
        .checkIfWidgetIsMarkedAsChanged(widgetName)
        .saveDraft()
        .checkIfNoticeAboutDraftExistenceIsPresent()
        .publishDraft()
        .checkIfNoticeAboutDraftExistenceIsNotPresent()
        .openWidgetSidebarToWorkWithWidgetMeta(widgetName)
        .checkIfPropertyWasAdded(propName);
  }

  @Test(
      description = "Тест удаления Property",
      dependsOnMethods = "addNewPropertyTest")
  public void deletePropertyTest() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(widgetName)
        .deleteProperty(propName)
        .checkIfPropertyIsAbsent(propName)
        .submitChanges()
        .checkIfWidgetIsMarkedAsChanged(widgetName)
        .saveDraft()
        .checkIfNoticeAboutDraftExistenceIsPresent()
        .publishDraft()
        .checkIfNoticeAboutDraftExistenceIsNotPresent()
        .openWidgetSidebarToWorkWithWidgetMeta(widgetName)
        .checkIfPropertyIsAbsent(propName);
  }

  /**
   * Return to the Main page.
   */
  @AfterMethod(description = "Навигация на главную страницу")
  public void returnToMainPage() {
    PageFactory.initElements(getDriver(), WidgetSidebarPage.class)
        .closeWidgetSidebarAndGoToMainSlider()
        .openPagesTree()
        .selectPage("sme-new/");
  }
}
