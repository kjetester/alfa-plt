package ru.alfabank.platform.insert.office;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class OfficeTest extends OfficeBaseTest {

  /**
   * Clean up the DataBase.
   */
  @BeforeMethod
  public void cleanUpDataBase() {
    STEP.cleanUpDataBase();
  }

  @Test(dataProvider = "officePositiveTestDataProvider")
  public void officePositiveTest(@ParameterKey("Test case") final String testCase,
                                 final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkOfficesMapping(offices);
  }

  @Test(dataProvider = "officeNegativeTestDataProvider", priority = 1)
  public void officeNegativeTest(@ParameterKey("Test case") final String testCase,
                                 final Offices offices,
                                 final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
