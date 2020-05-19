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

  protected Option option2;
  protected Option option3;
  protected static int option2counter;
  protected static int option3counter;

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
        {desktop, List.of("ru", "msk")},
        {mobile, List.of("ru", "msk")}
    };
  }

  /**
   * After class.
   */
  @AfterClass(description = "Посчет количества выпадений каждого из вариантов")
  public void afterClass() {
    LOGGER.info(String.format("Количество выпадений вариата '%s' - '%d'",
        option2, option2counter));
    LOGGER.info(String.format("Количество выпадений вариата '%s' - '%d'",
        option3, option3counter));
  }
}
