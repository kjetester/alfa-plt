package ru.alfabank.platform.tests.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.killDriver;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.buisenessobjects.User;
import ru.alfabank.platform.buisenessobjects.Widget;
import ru.alfabank.platform.helpers.TestDataHelper;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.acms.SearchPage;
import ru.alfabank.platform.tests.BaseTest;

public class SearchTest extends BaseTest {

  private Widget testWidget;
  private Widget.Property testProperty;
  private Widget.Property.Value testValue;

  /**
   * Before each method actions.
   */
  @BeforeMethod(description = "Авторизация и переход на '" + TEST_PAGE_URI + "'")
  public void beforeMethod() {
    User user = new User();
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(TEST_PAGE_URI);
    Page testPage = new Page(TEST_PAGE_URI, getDriver().getCurrentUrl());
    TestDataHelper testData = new TestDataHelper(user, testPage);
    testWidget = testData.getTestWidget();
    testProperty = testWidget.getProperties()[0];
    testValue = testProperty.getValues()[0];
  }

  @Test(description = "Тест функцирнала поиска виждета по названию")
  public void searchByWidgetNameTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .search(testWidget.getName())
        .checkWidgetMarking(testWidget.getName());
  }

  @Test(description = "Тест функцирнала поиска виджета по UID")
  public void searchByWidgetUidTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .search(testWidget.getUid())
        .checkWidgetMarking(testWidget.getName());
  }

  @Test(description = "Тест функцирнала поиска значения по значению")
  public void searchByPropertyNameTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), SearchPage.class)
        .search(testValue.getValue().asText())
        .checkWidgetMarking(testWidget.getName())
        .openWidgetSidebar(testWidget.getName())
        .checkPropertyMarking(testWidget.getName(), testProperty);
  }

  /**
   * Tear down.
   */
  @AfterMethod(description = "Закрытие сессии")
  public void afterMethod() {
    killDriver();
  }
}
