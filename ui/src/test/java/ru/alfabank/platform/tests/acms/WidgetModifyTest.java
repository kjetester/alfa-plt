package ru.alfabank.platform.tests.acms;

import static ru.alfabank.platform.helpers.DateHelper.getNowPlus;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.killDriver;

import java.util.Calendar;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.User;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;
import ru.alfabank.platform.tests.BaseTest;

public class WidgetModifyTest extends BaseTest {

  private static final String   TEST_PAGE_URI = "sme-new";
  private static final String   TEST_WIDGET = "MetaTitle";
  private static final String WIDGET_GEO_GROUPS = "ru";
  private static final String   TEST_PROPERTY = "title";
  private static final String VAL_1 = "mskmo";
  private static final String VAL_2 = "bez_msk_mo";
  private static final User user = new User();

  /**
   * Opening acms.
   */
  @BeforeClass(alwaysRun = true)
  public void settingUp() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(TEST_PAGE_URI);
  }

  @Test(
      description = "Тест изменения виджета и проперти для разных городов",
      priority = 0)
  public void widgetModifyTest() throws InterruptedException {

    final Calendar startActiveTime = getNowPlus(-1, Calendar.HOUR);
    final Calendar endActiveTime = getNowPlus(1, Calendar.HOUR);

    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(WIDGET_GEO_GROUPS)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(TEST_PROPERTY, "\"" + VAL_1 + "\"", VAL_1)
        .createValue(TEST_PROPERTY, "\"" + VAL_2 + "\"", VAL_2)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();

    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + TEST_PAGE_URI + "/")
        .checkPageTitleAfter(startActiveTime, VAL_1)
        .checkPageTitleAfter(startActiveTime, VAL_2, "vladimir");
    killDriver();
  }

  @Test(
      description = "Тест отсутствия виджета на странице - 'Видимый == false'",
      priority = 1)
  public void widgetInvisibilityTest() throws InterruptedException {

    final Calendar startActiveTime = getNowPlus(-1, Calendar.HOUR);
    final Calendar endActiveTime = getNowPlus(1, Calendar.HOUR);

    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(false)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(WIDGET_GEO_GROUPS)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(TEST_PROPERTY, "\"" + VAL_1 + "\"", VAL_1)
        .createValue(TEST_PROPERTY, "\"" + VAL_2 + "\"", VAL_2)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();

    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + TEST_PAGE_URI + "/")
        .checkPageTitleAfter(startActiveTime, "")
        .checkPageTitleAfter(startActiveTime, "", "vladimir");
    killDriver();
  }

  @Test(
      description = "Тест изменения активности виджета и проперти для разных городов",
      priority = 3,
      enabled = false)
  public void widgetActivePeriodTest() throws InterruptedException {

    final Calendar startActiveTime = getNowPlus(0, Calendar.SECOND);
    final Calendar endActiveTime = getNowPlus(5, Calendar.MINUTE);

    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(WIDGET_GEO_GROUPS)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(TEST_PROPERTY, "\"" + VAL_1 + "\"", VAL_1)
        .createValue(TEST_PROPERTY, "\"" + VAL_2 + "\"", VAL_2)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();

    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + TEST_PAGE_URI + "/")
        .checkPageTitleBefore()
        .checkPageTitleAfter(startActiveTime, VAL_1)
        .checkPageTitleAfter(startActiveTime, VAL_2, "vladimir")
        .checkPageTitleAfter(endActiveTime, "")
        .checkPageTitleAfter(endActiveTime, "", "vladimir");
    killDriver();
  }

  /**
   * Deleting created property value.
   */
  @AfterMethod(alwaysRun = true)
  public void removeCreatedPropertyValue() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(TEST_PAGE_URI)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(false)
        .collapseWidgetMetaInfoAnd()
        .deletePropertyValue(TEST_PROPERTY)
        .submitChanges()
        .saveDraft()
        .publishDraft();
  }
}
