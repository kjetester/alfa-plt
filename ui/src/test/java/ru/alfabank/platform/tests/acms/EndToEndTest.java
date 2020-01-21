package ru.alfabank.platform.tests.acms;

import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.pages.acms.*;
import ru.alfabank.platform.pages.alfasite.*;
import ru.alfabank.platform.tests.*;

import java.time.*;
import java.time.temporal.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class EndToEndTest extends BaseTest {

  private static final User   user = new User();
  private String testPageUri;
  private String testWidget;
  private String testProperty;
  private static final String RU = "ru";
  private static final String MSKMO = "mskmo";
  private static final String BEZ_MSK_MO = "bez_msk_mo";

  //TODO: http://jira.moscow.alfaintra.net/browse/ALFABANKRU-18153
  private final LocalDateTime startActiveTime = LocalDateTime.now().minus(3, ChronoUnit.HOURS);
  private final LocalDateTime endActiveTime = LocalDateTime.now().plus(1, ChronoUnit.HOURS);

  /**
   * Opening acms.
   */
  @BeforeMethod(alwaysRun = true)
  @Parameters({"testPageUri", "testWidget", "testProperty"})
  public void openBrowser(String testPageUri, String testWidget, String testProperty) {
    this.testPageUri = testPageUri;
    this.testWidget = testWidget;
    this.testProperty = testProperty;
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(testPageUri);
  }

  @Test(description = "Тест значений проперти виджета для двух разных гео-зон: '"
      + MSKMO + "' и '" + BEZ_MSK_MO + "'",
      groups = "need restore")
  public void valuesGeoModifyTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(testWidget)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(testProperty, "\"" + MSKMO + "\"", MSKMO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + testPageUri)
        .checkPageTitleAfter(startActiveTime, MSKMO)
        .checkPageTitleAfter(startActiveTime, BEZ_MSK_MO, "vladimir");
    killDriver();
  }

  @Test(description = "Тест изменения и отображение виджета для " + MSKMO,
      priority = 1,
      groups = "need restore")
  public void widgetsGeoModifyTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(testWidget)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(MSKMO)
        .collapseWidgetMetaInfoAnd()
        .modifyValue(testProperty, "\"" + MSKMO + "\"", MSKMO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + testPageUri)
        .checkPageTitleAfter(startActiveTime, MSKMO)
        .checkPageTitleAfter(startActiveTime, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест скрытия виджета",
      priority = 2,
      groups = "need restore")
  public void widgetInvisibilityTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(testWidget)
        .expandWidgetMetaInfo()
        .setVisibilityTo(false)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(testProperty, "\"" + MSKMO + "\"", MSKMO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + testPageUri)
        .checkPageTitleAfter(startActiveTime, "")
        .checkPageTitleAfter(startActiveTime, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест удаления проперти",
      priority = 3)
  public void propertyDeletionTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(testWidget)
        .deleteProperty(testProperty)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + testPageUri)
        .checkPageTitleAfter(startActiveTime, "")
        .checkPageTitleAfter(startActiveTime, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест добавления проперти",
      dependsOnMethods = "propertyDeletionTest")
  public void propertyAdditionTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(testWidget)
        .createNewPropertyToWorkWith(testProperty)
        .modifyValue(testProperty, "\"" + RU + "\"", RU)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + testPageUri)
        .checkPageTitleAfter(startActiveTime, RU)
        .checkPageTitleAfter(startActiveTime, RU, "vladimir");
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
        .openWidgetSidebarToWorkWithWidgetMeta(testWidget)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(testProperty, "\"" + MSKMO + "\"", MSKMO)
        .createValue(testProperty, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + testPageUri)
        .checkPageTitleBefore()
        .checkPageTitleAfter(startActiveTime, MSKMO)
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
  public void restore() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(testPageUri)
        .openWidgetSidebarToWorkWithWidgetMeta(testWidget)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
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
