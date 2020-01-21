package ru.alfabank.platform.tests.acms;

import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.helpers.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.tests.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class SearchTest extends BaseTest {

  private Widget testWidget;
  private Widget.Property testProperty;
  private Widget.Property.Value testValue;

  /**
   * Before each method actions.
   */
  @BeforeMethod(description = "Авторизация и переход на страницу")
  @Parameters({"testPageUri"})
  public void beforeMethod(String testPageUri) {
    User user = new User();
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(testPageUri);
    Page testPage = new Page(testPageUri, getDriver().getCurrentUrl());
    TestDataHelper testData = new TestDataHelper(user, testPage);
    testWidget = testData.getTestWidget();
    testProperty = testWidget.getProperties()[0];
    testValue = testProperty.getValues()[0];
  }

  @Test(description = "Тест функцирнала поиска виждета по названию")
  public void searchByWidgetNameTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .searchFor(testWidget.getName())
        .checkWidgetMarking(testWidget.getName());
  }

  @Test(description = "Тест функцирнала поиска виджета по UID")
  public void searchByWidgetUidTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .searchFor(testWidget.getUid())
        .checkWidgetMarking(testWidget.getName());
  }

  @Test(description = "Тест функцирнала поиска значения по значению")
  public void searchByPropertyNameTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .searchFor(testValue.getValue().asText())
        .checkWidgetMarking(testWidget.getName())
        .openWidgetSidebar(testWidget.getName())
        .checkPropertyMarking(testProperty.getName());
  }

  /**
   * Tear down.
   */
  @AfterMethod(description = "Закрытие сессии")
  public void afterMethod() {
    killDriver();
  }
}
