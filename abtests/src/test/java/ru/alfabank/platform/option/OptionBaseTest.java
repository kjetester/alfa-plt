package ru.alfabank.platform.option;

import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;

public class OptionBaseTest extends BaseTest {

  /**
   * Data provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] dataProvider() {
    return new Object[][]{
        {true},
        {false}
    };
  }
}
