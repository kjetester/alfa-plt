package ru.alfabank.platform.tests.acms;

import io.qameta.allure.testng.*;
import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.tests.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class PropertyDraftTest extends BaseTest {

  User user = new User();

  @TestInstanceParameter
  private String widgetName = "MetaTitle";
  @TestInstanceParameter
  private String propName = "newPropName";

  /**
   * Opening acms.
   */
  @BeforeClass (alwaysRun = true)
  @Parameters({"testPageUri"})
  public void settingUp(String testPageUri) {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(testPageUri);
  }

  @Test(
      description = "Тест добавления нового Property")
  public void addNewPropertyTest() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(widgetName)
        .createNewProperty(propName)
        .checkIfPropertyWasAdded(propName)
        .submitChanges()
        .checkIfWidgetIsMarked(widgetName)
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
        .checkIfWidgetIsMarked(widgetName)
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
        .closeWidgetSidebar()
        .openPagesTree()
        .selectPage("sme-new/");
  }
}
