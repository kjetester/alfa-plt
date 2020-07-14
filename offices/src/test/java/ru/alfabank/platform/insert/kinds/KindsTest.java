package ru.alfabank.platform.insert.kinds;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class KindsTest extends KindsBaseTest {

  /**
   * Clean up the DataBase.
   */
  @BeforeMethod
  public void cleanUpDataBase() {
    STEP.cleanUpDataBase();
  }

  @Test(dataProvider = "kindsPositiveTestDataProvider")
  public void kindsPositiveTest(@ParameterKey("Test case") final String testCase,
                                final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkKindsMapping(offices);
  }

  @Test(dataProvider = "kindsNegativeTestDataProvider", priority = 1)
  public void kindsNegativeTest(@ParameterKey("Test case") final String testCase,
                                final Offices offices,
                                final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
