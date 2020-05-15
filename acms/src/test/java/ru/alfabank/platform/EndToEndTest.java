package ru.alfabank.platform;

import static ru.alfabank.platform.businessobjects.enums.Geo.BEZ_MSK_MO;
import static ru.alfabank.platform.businessobjects.enums.Geo.MSK_MO;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.Geo.VLADIMIR;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.killDriver;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.acms.PagesSliderPage;
import ru.alfabank.platform.pages.acms.WidgetSidebarPage;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;
import ru.alfabank.platform.utils.TestFailureListener;

@Listeners({TestFailureListener.class})
public class EndToEndTest extends BaseTest {

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
  public void pageCreationTest() {
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

  @Test(description = "Тест значений проперти виджета для двух разных гео-зон",
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
        .setGeoGroupsToWidget(List.of(RU))
        .collapseWidgetMetaInfoAnd()
        .modifyValueContinueWithPropertyPage(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft()
        .navigateToAlfaSite()
        .compareUrl(basePage.getUri())
        .checkPageTitleAfter(dateFrom, MSK_MO.toString())
        .checkPageTitleAfter(dateTo, BEZ_MSK_MO.toString(), VLADIMIR);
    killDriver();
  }

  @Test(description = "Тест изменения и отображение виджета",
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
        .setGeoGroupsToWidget(List.of(MSK_MO))
        .collapseWidgetMetaInfoAnd()
        .modifyValueAndContinueWithWidgetSidebarPage(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleAfter(dateFrom, MSK_MO.toString())
        .checkPageTitleAfter(dateFrom, "", VLADIMIR);
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
        .setGeoGroupsToWidget(List.of(RU))
        .collapseWidgetMetaInfoAnd()
        .modifyValueContinueWithPropertyPage(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleAfter(dateFrom, "")
        .checkPageTitleAfter(dateFrom, "", VLADIMIR);
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
        .checkPageTitleAfter(dateFrom, "", VLADIMIR);
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
        .modifyValueAndContinueWithWidgetSidebarPage(testProperty, "\"" + RU + "\"", RU)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleAfter(dateFrom, RU.toString())
        .checkPageTitleAfter(dateFrom, RU.toString(), VLADIMIR);
    killDriver();
  }

  @Test(
      description = "Тест изменения активности виджета и проперти для разных городов",
      priority = 3,
      enabled = false)
  // FIXME: 09.05.2020 включить, когда будет реализована инвалидации кеша при наступлении "от - до"
  public void widgetActivePeriodTest() throws InterruptedException {
    final LocalDateTime startActiveTime = LocalDateTime.now().plus(30, ChronoUnit.SECONDS);
    final LocalDateTime endActiveTime = LocalDateTime.now().plus(60, ChronoUnit.SECONDS);
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(List.of(RU))
        .collapseWidgetMetaInfoAnd()
        .modifyValueContinueWithPropertyPage(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUri + basePage.getUri())
        .checkPageTitleBefore()
        .checkPageTitleAfter(startActiveTime, MSK_MO.toString())
        .checkPageTitleAfter(startActiveTime, BEZ_MSK_MO.toString(), VLADIMIR)
        .checkPageTitleAfter(endActiveTime, "")
        .checkPageTitleAfter(endActiveTime, "", VLADIMIR);
    killDriver();
  }

  /**
   * Deleting created property value.
   */
  @AfterMethod(alwaysRun = true,
      onlyForGroups = "need restore")
  @Parameters({"baseUrl"})
  public void restore(String baseUrl) {
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
        .setGeoGroupsToWidget(List.of(RU))
        .collapseWidgetMetaInfoAnd()
        .restorePropertyAndValues(testProperty,
            "\"Удобный банк для малого бизнеса — «Альфа-Банк» - Москва\"", RU)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
  }
}
