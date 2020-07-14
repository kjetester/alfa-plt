package ru.alfabank.platform.insert.wrapper;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class WrapperTest extends WrapperBaseTest {

  /**
   * Clean up the DataBase.
   */
  @BeforeMethod
  public void cleanUpDataBase() {
    STEP.cleanUpDataBase();
  }

  @Test(dataProvider = "wrapperPositiveTestDataProvider")
  public void wrapperPositiveTest(@ParameterKey("Test case") final String testCase,
                                  final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkIfOfficesWereSaved(offices);
  }

  @Test(dataProvider = "wrapperNegativeTestDataProvider", priority = 1)
  public void wrapperNegativeTest(@ParameterKey("Test case") final String testCase,
                                  final Offices offices,
                                  final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
