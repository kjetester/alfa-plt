package ru.alfabank.platform.experiment.involvements;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Option;

public class InvolvementsBaseTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(InvolvementsBaseTest.class);

  protected Option defaultDesktopOption;
  protected Option abTestDesktopOption1;
  protected Option abTestDesktopOption2;
  protected Option defaultMobileOption;
  protected Option abTestMobileOption1;
  protected Option abTestMobileOption2;
  protected static int defaultDesktopOptionCounter;
  protected static int abTestDesktopOption1counter;
  protected static int abTestDesktopOption2counter;
  protected static int defaultMobileOptionCounter;
  protected static int abTestMobileOption1counter;
  protected static int abTestMobileOption2counter;

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  protected static Object[][] dataProvider() {
    return new Object[][]{
        {desktop, List.of("ru")},
        {mobile, List.of("ru")},
        {desktop, List.of("msk")},
        {mobile, List.of("msk")}
    };
  }

  /**
   * After class.
   */
  @AfterClass(description = "Посчет количества выпадений каждого из вариантов")
  public void afterClass() {
    LOGGER.info(String.format(
        "Количество выпадений desktop вариата по-умолчанию - '%d'",
        defaultDesktopOptionCounter));
    LOGGER.info(String.format(
        "Количество выпадений desktop вариата АБ-теста №1 - '%d'",
        abTestDesktopOption1counter));
    LOGGER.info(String.format(
        "Количество выпадений desktop вариата АБ-теста №2 - '%d'",
        abTestDesktopOption2counter));
    LOGGER.info(String.format(
        "Количество выпадений mobile вариата по-умолчанию - '%d'",
        defaultMobileOptionCounter));
    LOGGER.info(String.format(
        "Количество выпадений mobile вариата АБ-теста №1 - '%d'",
        abTestMobileOption1counter));
    LOGGER.info(String.format(
        "Количество выпадений mobile вариата АБ-теста №2 - '%d'",
        abTestMobileOption2counter));
  }
}
