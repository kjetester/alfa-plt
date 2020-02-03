package ru.alfabank.platform.tests.acms;

import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.helpers.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.reporting.*;
import ru.alfabank.platform.tests.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

@Listeners({ScreenShotListener.class})
public class SearchTest extends BaseTest {

  private Widget testWidget;
  private Widget.Property testProperty;
  private Widget.Property.Value testValue;

  /**
   * Before each method actions.
   */
  @BeforeMethod(description = "Авторизация и переход на страницу")
  public void beforeMethod() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUrl + "acms/", user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage("sme-new/");
    Page testPage = new Page("sme-new/", getDriver().getCurrentUrl());
    TestDataHelper testData = new TestDataHelper(user, testPage);
    testWidget = testData.getTestWidget();
    testProperty = testWidget.getProperties()[0];
    testValue = testProperty.getValues()[0];
  }

  @Test(description = "Тест функцирнала поиска виждета по названию")
  public void searchByWidgetNameTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .searchFor(testWidget.getName())
        .checkIfWidgetIsMarkedAsFound(testWidget.getName());
  }

  @Test(description = "Тест функцирнала поиска виджета по UID")
  public void searchByWidgetUidTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .searchFor(testWidget.getUid())
        .checkIfWidgetIsMarkedAsFound(testWidget.getName());
  }

  @Test(description = "Тест функцирнала поиска по значению")
  public void searchByPropertyNameTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .searchFor(testValue.getValue().asText())
        .checkIfWidgetIsMarkedAsFound(testWidget.getName())
        .openWidgetSidebar(testWidget.getName())
        .checkPropertyMarking(testProperty.getName());
  }

  @Test(description = "Тест игнорования подстроки при поиске виджета по UID",
      priority = 1)
  public void ignoreSubstringWhileSearchingByWidgetUidTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .searchFor(testWidget.getUid().substring(3, (testWidget.getUid().length() - 3)))
        .checkNoWidgetIsMarked();
  }

  /**
   * Tear down.
   */
  @AfterMethod(description = "Закрытие сессии")
  public void afterMethod() {
    killDriver();
  }
}
