package ru.alfabank.platform.tests.acms;

import org.apache.logging.log4j.*;
import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.pages.alfasite.*;
import ru.alfabank.platform.reporting.*;
import ru.alfabank.platform.tests.*;

import java.time.*;
import java.time.temporal.*;

import static org.apache.logging.log4j.LogManager.*;
import static ru.alfabank.platform.helpers.DriverHelper.*;

@Listeners({ScreenShotListener.class})
public class EndToEndTest extends BaseTest {

  private static final Logger LOGGER = getLogger(EndToEndTest.class);
  private static final User   USER = new User();
  private static final String RU = "ru";
  private static final String MSK_MO = "mskmo";
  private static final String BEZ_MSK_MO = "bez_msk_mo";
  //TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-18153
  private static final LocalDateTime START_TIME = LocalDateTime.now().minus(3, ChronoUnit.HOURS);
  private static final LocalDateTime END_TIME = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
  private static final String TEST_WIDGET = "MetaTitle";
  private static final String TEST_PROPERTY = "title";
  private static String testPropertyValue;
  private String testProperty;

  /**
   * Opening acms.
   */
  @BeforeMethod(alwaysRun = true)
  public void openBrowser() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUrl + "acms/", USER.getLogin(), USER.getPassword())
        .openPagesTree();
  }

  @Test(description = "Тест создания страницы")
  public void pageCreationTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .createNewPageFromRoot()
        .fillAndSubmitCreationForm(testData);
    PageFactory.initElements(getDriver(), MainPage.class)
        .checkPageOpened(testData.getCreatedPage().getPath());
    killDriver();
  }

  @Test(description = "Тест копирования виджета",
      dependsOnMethods = "pageCreationTest")
  public void widgetCopyTest() throws InterruptedException {
    Page page = testData.getCreatedPage();
    String sourcePagePath = "about";
    PageFactory.initElements(getDriver(), PagesSliderPage.class)
        .selectPage(sourcePagePath)
        .copyWidgetOnPage(TEST_WIDGET, page.getPath())
        .checkPageOpened(page.getPath())
        .checkIfWidgetIsPresent(TEST_WIDGET)
        .publishDraft();
    testPropertyValue = PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebar(TEST_WIDGET)
        .readSingleValueFromProperty(TEST_PROPERTY);
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUrl + page.getPath() + "/")
        .checkPageTitleAfter(START_TIME, testPropertyValue);
    killDriver();
  }

  @Test(description = "Тест значений проперти виджета для двух разных гео-зон: '"
      + MSK_MO + "' и '" + BEZ_MSK_MO + "'",
      groups = "need restore",
      enabled = false)
  public void valuesGeoModifyTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(START_TIME)
        .openEndDatePicker().setDateTo(END_TIME)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUrl + testData.getCreatedPage().getPath())
        .checkPageTitleAfter(START_TIME, MSK_MO)
        .checkPageTitleAfter(START_TIME, BEZ_MSK_MO, "vladimir");
    killDriver();
  }

  @Test(description = "Тест изменения и отображение виджета для " + MSK_MO,
      priority = 1,
      groups = "need restore",
      enabled = false)
  public void widgetsGeoModifyTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(START_TIME)
        .openEndDatePicker().setDateTo(END_TIME)
        .setGeoGroupsToWidget(MSK_MO)
        .collapseWidgetMetaInfoAnd()
        .modifyValue(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUrl + testData.getCreatedPage().getPath())
        .checkPageTitleAfter(START_TIME, MSK_MO)
        .checkPageTitleAfter(START_TIME, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест скрытия виджета",
      priority = 2,
      groups = "need restore",
      enabled = false)
  public void widgetInvisibilityTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(false)
        .openStartDatePicker().setDateTo(START_TIME)
        .openEndDatePicker().setDateTo(END_TIME)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(testProperty, "\"" + MSK_MO + "\"", MSK_MO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUrl + testData.getCreatedPage().getPath())
        .checkPageTitleAfter(START_TIME, "")
        .checkPageTitleAfter(START_TIME, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест удаления проперти",
      priority = 3,
      enabled = false)
  public void propertyDeletionTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .deleteProperty(testProperty)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUrl + testData.getCreatedPage().getPath())
        .checkPageTitleAfter(START_TIME, "")
        .checkPageTitleAfter(START_TIME, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест добавления проперти",
      dependsOnMethods = "propertyDeletionTest",
      enabled = false)
  public void propertyAdditionTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .createNewPropertyToWorkWith(testProperty)
        .modifyValue(testProperty, "\"" + RU + "\"", RU)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open(baseUrl + testData.getCreatedPage().getPath())
        .checkPageTitleAfter(START_TIME, RU)
        .checkPageTitleAfter(START_TIME, RU, "vladimir");
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
        .open(baseUrl + testData.getCreatedPage().getPath())
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
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(baseUrl + "acms/", USER.getLogin(), USER.getPassword())
        .openPagesTree()
        .selectPage(testData.getCreatedPage().getPath())
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(START_TIME)
        .openEndDatePicker().setDateTo(END_TIME)
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
