package ru.alfabank.platform;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.killDriver;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.businessobjects.Property;
import ru.alfabank.platform.businessobjects.Value;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.helpers.TestDataHelper;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.acms.SearchPage;
import ru.alfabank.platform.utils.TestFailureListener;

@Listeners({TestFailureListener.class})
public class SearchTest extends BaseTest {

  private Widget testWidget;
  private Property testProperty;
  private Value testValue;

  /**
   * Before each method actions.
   */
  @BeforeMethod(description = "Авторизация и переход на страницу")
  public void beforeMethod() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri + "acms/", USER)
        .openPagesTree()
        .openPage("sme-new/");
    //TODO: broken here
    Page testPage = new Page.Builder().build();
    TestDataHelper testData = new TestDataHelper(USER, testPage);
    testWidget = testData.getTestWidget();
    testProperty = testWidget.getProperties().get(0);
    testValue = testProperty.getValues().get(0);
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
        .searchFor(testValue.getValue().toString())
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
