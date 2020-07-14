package ru.alfabank.platform.insert.service;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class ServiceTest extends ServiceBaseTest {

  /**
   * Clean up the DataBase.
   */
  @BeforeMethod
  public void cleanUpDataBase() {
    STEP.cleanUpDataBase();
  }

  @Test(dataProvider = "servicePositiveTestDataProvider")
  public void servicePositiveTest(@ParameterKey("Test case") final String testCase,
                                  final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkServicesMapping(offices);
  }

  @Test(dataProvider = "serviceNegativeTestDataProvider", priority = 1)
  public void serviceNegativeTest(@ParameterKey("Test case") final String testCase,
                                  final Offices offices,
                                  final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
