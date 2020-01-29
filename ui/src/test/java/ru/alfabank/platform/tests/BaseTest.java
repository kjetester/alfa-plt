package ru.alfabank.platform.tests;

import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.helpers.*;
import ru.alfabank.platform.reporting.*;

import static ru.alfabank.platform.reporting.BasicLogger.*;

@Listeners({CustomListener.class})
public class BaseTest {

  protected User user = new User();
  protected TestDataHelper testData;

  @BeforeTest(alwaysRun = true)
  public void beforeTest(final ITestContext context) {
    testData = new TestDataHelper();
    info(String.format("Starting up test '%s'", context.getName()));
  }

  @AfterTest(alwaysRun = true)
  public void afterTest(ITestContext context) {
    info("Ending test '" + context.getName() + "'.");
  }
}
