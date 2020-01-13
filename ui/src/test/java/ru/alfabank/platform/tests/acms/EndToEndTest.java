package ru.alfabank.platform.tests.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.killDriver;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.buisenessobjects.User;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;
import ru.alfabank.platform.tests.BaseTest;

public class EndToEndTest extends BaseTest {

  private static final User   user = new User();
  private static final String TEST_WIDGET = "MetaTitle";
  private static final String TEST_PROPERTY = "title";
  private static final String RU = "ru";
  private static final String MSKMO = "mskmo";
  private static final String BEZ_MSK_MO = "bez_msk_mo";

  private final LocalDateTime startActiveTime = LocalDateTime.now().minus(1, ChronoUnit.HOURS);
  private final LocalDateTime endActiveTime = LocalDateTime.now().plus(1, ChronoUnit.HOURS);

  /**
   * Opening acms.
   */
  @BeforeMethod(alwaysRun = true)
  public void openBrowser() {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(TEST_PAGE_URI);
  }

  @Test(description = "Тест значений проперти виджета для двух разных гео-зон",
      groups = "need restore")
  public void valuesGeoModifyTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(TEST_PROPERTY, "\"" + MSKMO + "\"", MSKMO)
        .createValue(TEST_PROPERTY, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + TEST_PAGE_URI + "/")
        .checkPageTitleAfter(startActiveTime, MSKMO)
        .checkPageTitleAfter(startActiveTime, BEZ_MSK_MO, "vladimir");
    killDriver();
  }

  @Test(description = "Тест изменения и отображение виджета для " + MSKMO, priority = 1,
      groups = "need restore")
  public void widgetsGeoModifyTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(MSKMO)
        .collapseWidgetMetaInfoAnd()
        .modifyValue(TEST_PROPERTY, "\"" + MSKMO + "\"", MSKMO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + TEST_PAGE_URI + "/")
        .checkPageTitleAfter(startActiveTime, MSKMO)
        .checkPageTitleAfter(startActiveTime, "", "vladimir");
    killDriver();
  }

  @Test(description = "Тест скрытия виджета", priority = 2, groups = "need restore")
  public void widgetInvisibilityTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(false)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(TEST_PROPERTY, "\"" + MSKMO + "\"", MSKMO)
        .createValue(TEST_PROPERTY, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
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

  @Test(description = "Тест удаления проперти", priority = 3)
  public void propertyDeletionTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .deleteProperty(TEST_PROPERTY)
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

  @Test(description = "Тест добавления проперти", dependsOnMethods = "propertyDeletionTest")
  public void propertyAdditionTest() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .createNewPropertyToWorkWith(TEST_PROPERTY)
        .modifyValue(TEST_PROPERTY, "\"" + RU + "\"", RU)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + TEST_PAGE_URI + "/")
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
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(TEST_PROPERTY, "\"" + MSKMO + "\"", MSKMO)
        .createValue(TEST_PROPERTY, "\"" + BEZ_MSK_MO + "\"", BEZ_MSK_MO)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
    PageFactory.initElements(getDriver(), AlfaSitePage.class)
        .open("/" + TEST_PAGE_URI + "/")
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
  @AfterMethod(alwaysRun = true, onlyForGroups = {"need restore"})
  public void restore() throws InterruptedException {
    PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(user.getLogin(), user.getPassword())
        .openPagesTree()
        .selectPage(TEST_PAGE_URI)
        .openWidgetSidebarToWorkWithWidgetMeta(TEST_WIDGET)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(RU)
        .collapseWidgetMetaInfoAnd()
        .restorePropertyAndValues(
            TEST_PROPERTY,
            "\"Удобный банк для малого бизнеса — «Альфа-Банк» - Москва\"",
            RU)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    killDriver();
  }
}
