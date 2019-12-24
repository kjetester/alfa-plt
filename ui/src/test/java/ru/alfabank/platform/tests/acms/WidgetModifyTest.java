package ru.alfabank.platform.tests.acms;

import static ru.alfabank.platform.buisenessobjects.DateHelper.getNowPlus;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.util.Calendar;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import ru.alfabank.platform.pages.MainPage;
import ru.alfabank.platform.tests.BaseTest;

public class WidgetModifyTest extends BaseTest {

  private static final String   WIDGET_NAME = "MetaTitle";
  private static final String[] WIDGET_GEO_GROUPS = new String[]{"ru"};
  private static final String   PROPERTY_NAME = "title";
  private static final String   PROPERTY_VALUE_1_VALUE =
      "\"Удобный банк для малого бизнеса  — «Альфа-Банк»\"";
  private static final String[] PROPERTY_VALUE_1_GEO_GROUPS = new String[]{"mskmo"};
  private static final String   PROPERTY_VALUE_2_VALUE =
      "\"«Альфа-Банк» - удобный банк для малого бизнеса \"";
  private static final String[] PROPERTY_VALUE_2_GEO_GROUPS = new String[]{"bez_msk_mo_spb"};

  @Test(description = "Widget visibility test")
  public void widgetVisibilityTest() throws InterruptedException {
    final Calendar startActiveTime = getNowPlus(3, Calendar.MINUTE);
    final Calendar endActiveTime = getNowPlus(5, Calendar.MINUTE);

    PageFactory.initElements(getDriver(), MainPage.class)
        .openWidgetSidebar(WIDGET_NAME)
        .expandWidgetMetaInfo()
        .setVisibilityTo(true)
        .openStartDatePicker().setDateTo(startActiveTime)
        .openEndDatePicker().setDateTo(endActiveTime)
        .setGeoGroupsToWidget(WIDGET_GEO_GROUPS)
        .collapseWidgetMetaInfoAnd()
        .modifyPropertyValue(PROPERTY_NAME,
                             PROPERTY_VALUE_1_VALUE,
                             PROPERTY_VALUE_1_GEO_GROUPS)
        .createPropertyValue(PROPERTY_NAME,
                             PROPERTY_VALUE_2_VALUE,
                             PROPERTY_VALUE_2_GEO_GROUPS)
        .submitChanges()
        .saveDraft()
        .publishDraft();
    Thread.sleep(150000);
  }
}
