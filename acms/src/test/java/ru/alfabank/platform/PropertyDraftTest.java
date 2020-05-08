package ru.alfabank.platform;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.acms.WidgetSidebarPage;
import ru.alfabank.platform.utils.TestFailureListener;

@Listeners({TestFailureListener.class})
public class PropertyDraftTest extends BaseTest {

  private String widgetName = "MetaTitle";
  private String propName = "newPropName";

  /**
   * Opening acms.
   */
  @BeforeClass (alwaysRun = true)
  public void settingUp() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri, USER)
        .openPagesTree()
        .openPage("sme-new/");
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
        .openPage("sme-new/");
  }
}
