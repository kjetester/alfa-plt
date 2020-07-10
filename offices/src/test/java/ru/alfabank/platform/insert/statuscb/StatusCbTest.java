package ru.alfabank.platform.insert.statuscb;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class StatusCbTest extends StatusCbBaseTest {

  /**
   * Clean up the DataBase.
   */
  @BeforeMethod
  public void cleanUpDataBase() {
    STEP.cleanUpDataBase();
  }

  @Test(dataProvider = "statusCbPositiveTestDataProvider")
  public void statusCbPositiveTest(@ParameterKey("Test case")
                                 final String testCase,
                                 final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkOfficeMapping(offices);
  }

  @Test(dataProvider = "statusCbNegativeTestDataProvider", priority = 1)
  public void statusCbNegativeTest(@ParameterKey("Test case")
                                 final String testCase,
                                 final Offices offices,
                                   final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
