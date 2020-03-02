package ru.alfabank.platform.tests.acms;

import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.pages.alfasite.*;
import ru.alfabank.platform.reporting.*;
import ru.alfabank.platform.tests.*;

import java.time.*;
import java.time.temporal.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

@Listeners({TestFailureListener.class})
public class EndToEndTest extends BaseTest {

  private static final String RU = "ru";
  private static final String MSK_MO = "mskmo";
  private static final String BEZ_MSK_MO = "bez_msk_mo";

  /**
   * Opening acms.
   */
  @BeforeMethod(alwaysRun = true)
  public void openBrowser() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUri + "acms/", USER)
        .openPagesTree();
  }

  @Test(description = "Тест создания страницы")
  public void pageCreationTest() throws InterruptedException {
    basePage = PageFactory.initElements(getDriver(), PagesSliderPage.class)
        .createNewPageWithinPage(null)
        .fillAndSubmitCreationForm(basePage);
    PageFactory.initElements(getDriver(), MainPage.class)
        .checkPageOpened(basePage.getUri());
    killDriver();
  }

  @Test(description = "Тест копирования виджета",
      dependsOnMethods = "pageCreationTest")
  public void widgetCopyTest() throws InterruptedException {
    String sourcePagePath = "about";
    PageFactory.initElements(getDriver(), PagesSliderPage.class)
        .openPage(sourcePagePath)
        .copyWidgetOnPage(TEST_WIDGET, basePage.getUri())
        .checkPageOpened(basePage.getUri())
        .checkIfWidgetIsPresent(TEST_WIDGET)
        .publishDraft();
    testPropertyValue = PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebar(TEST_WIDGET)
        .readSingleValueFromProperty(TEST_PROPERTY);
    PageFactory.initElements(getDriver(), WidgetSidebarPage.class)
        .closeWidgetSidebarAndGoToMainPage()
        .navigateToAlfaSite()
        .compareUrl(basePage.getUri())
        .checkPageTitleAfter(basePage.getLocalDateTimeFrom(), testPropertyValue);
    killDriver();
  }

  @Test(description = "Тест удаления виджета",
      dependsOnMethods = "pageCreationTest")
  public void widgetDeletionTest() {

  }

  @Test(description = "Тест значений проперти виджета для двух разных гео-зон: '"
      + MSK_MO + "' и '" + BEZ_MSK_MO + "'",
      groups = "need restore",
      enabled = false)
  public void valuesGeoModifyTest() throws InterruptedException {
    LocalDateTime dateFrom = LocalDateTime.now().minus(0, ChronoUnit.HOURS);
    LocalDateTime dateTo = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(dateFrom)
        .openEndDatePicker().setDateTo(dateTo)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft()
        .navigateToAlfaSite()
        .compareUrl(basePage.getUri())
        .checkPageTitleAfter(dateFrom, MSK_MO)
        .checkPageTitleAfter(dateTo, BEZ_MSK_MO, "vladimir");
    killDriver();
  }

  @Test(description = "Тест изменения и отображение виджета для " + MSK_MO,
      priority = 1,
      groups = "need restore",
      enabled = false)
  public void widgetsGeoModifyTest() throws InterruptedException {
    LocalDateTime dateFrom = LocalDateTime.now().minus(0, ChronoUnit.HOURS);
    LocalDateTime dateTo = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(dateFrom)
        .openEndDatePicker().setDateTo(dateTo)
        .setGeoGroupsToWidget(MSK_MO)
        .collapseWidgetMetaInfoAnd()
        .modifyValue(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleAfter(dateFrom, MSK_MO)
        .checkPageTitleAfter(dateFrom, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест скрытия виджета",
      priority = 2,
      groups = "need restore",
      enabled = false)
  public void widgetInvisibilityTest() throws InterruptedException {
    LocalDateTime dateFrom = LocalDateTime.now().minus(0, ChronoUnit.HOURS);
    LocalDateTime dateTo = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(false)
        .openStartDatePicker().setDateTo(dateFrom)
        .openEndDatePicker().setDateTo(dateTo)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleAfter(dateFrom, "")
        .checkPageTitleAfter(dateFrom, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест удаления проперти",
      priority = 3,
      enabled = false)
  public void propertyDeletionTest() throws InterruptedException {
    LocalDateTime dateFrom = LocalDateTime.now().minus(0, ChronoUnit.HOURS);
    LocalDateTime dateTo = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .deleteProperty(testProperty)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleAfter(dateFrom, "")
        .checkPageTitleAfter(dateFrom, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест добавления проперти",
      dependsOnMethods = "propertyDeletionTest",
      enabled = false)
  public void propertyAdditionTest() throws InterruptedException {
    LocalDateTime dateFrom = LocalDateTime.now().minus(0, ChronoUnit.HOURS);
    LocalDateTime dateTo = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .createNewPropertyToWorkWith(testProperty)
        .modifyValue(testProperty, "\"" + RU + "\"", RU)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleAfter(dateFrom, RU)
        .checkPageTitleAfter(dateFrom, RU, "vladimir");
    killDriver();
  }

  @Test(
      description = "Тест изменения активности виджета и проперти для разных городов",
      priority = 3,
      enabled = false)
  //TODO: включить, когда будет реализована инвалидации кеша при наступлении "от - до"
  public void widgetActivePeriodTest() throws InterruptedException {
    final LocalDateTime startActiveTime = LocalDateTime.now().plus(30, ChronoUnit.SECONDS);
    final LocalDateTime endActiveTime = LocalDateTime.now().plus(60, ChronoUnit.SECONDS);
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleBefore()
        .checkPageTitleAfter(startActiveTime, MSK_MO)
        .checkPageTitleAfter(startActiveTime, BEZ_MSK_MO, "vladimir")
        .checkPageTitleAfter(endActiveTime, "")
        .checkPageTitleAfter(endActiveTime, "", "vladimir");
    killDriver();
  }

  /**
   * Deleting created property value.
   */
  @AfterMethod(alwaysRun = true,
      onlyForGroups = "need restore")
  @Parameters({"baseUrl"})
  public void restore(String baseUrl) throws InterruptedException {
    LocalDateTime dateFrom = LocalDateTime.now().minus(0, ChronoUnit.HOURS);
    LocalDateTime dateTo = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUrl + "acms/", USER)
        .openPagesTree()
        .openPage(basePage.getUri())
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(dateFrom)
        .openEndDatePicker().setDateTo(dateTo)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .restorePropertyAndValues(testProperty,
            "\"Удобный банк для малого бизнеса — «Альфа-Банк» - Москва\"",
            RU)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
  }
}
