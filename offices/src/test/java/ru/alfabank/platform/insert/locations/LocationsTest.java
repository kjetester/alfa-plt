package ru.alfabank.platform.insert.locations;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class LocationsTest extends LocationsBaseTest {

  /**
   * Clean up the DataBase.
   */
  @BeforeMethod
  public void cleanUpDataBase() {
    STEP.cleanUpDataBase();
  }

  @Test(dataProvider = "locationsPositiveTestDataProvider")
  public void locationsPositiveTest(@ParameterKey("Test case") final String testCase,
                                    final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkIfOfficesWereSaved(offices);
    STEP.checkLocationMapping(offices);
  }

  @Test(dataProvider = "locationsNegativeTestDataProvider", priority = 1)
  public void locationsNegativeTest(@ParameterKey("Test case") final String testCase,
                                    final Offices offices,
                                    final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
