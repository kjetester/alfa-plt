package ru.alfabank.platform.tests.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import io.qameta.allure.testng.TestInstanceParameter;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.User;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.tests.BaseTest;

public class PropertyDraftTest extends BaseTest {

  User user = new User();

  @TestInstanceParameter
  private String widgetName = "MetaTitle";
  @TestInstanceParameter
  private String propName = "newPropName";

  @Test(description = "Addition a new property to the Widget")
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

  @Test(description = "Deletion a property from the Widget")
  public void deleteProperty() {
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
  @AfterMethod(description = "Navigate to The Main Page")
  public void returnToMainPage() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage("sme-new");
  }
}
