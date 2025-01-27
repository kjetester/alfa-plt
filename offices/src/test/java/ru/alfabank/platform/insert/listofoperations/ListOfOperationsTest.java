package ru.alfabank.platform.insert.listofoperations;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class ListOfOperationsTest extends ListOfOperationsBaseTest {

  /**
   * Clean up the DataBase.
   */
  @BeforeMethod
  public void cleanUpDataBase() {
    STEP.cleanUpDataBase();
  }

  @Test(dataProvider = "listOfOperationsPositiveTestDataProvider")
  public void listOfOperationsPositiveTest(@ParameterKey("Test case") final String testCase,
                                           final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkOfficesListOfOperationsMapping(offices);
  }

  @Test(dataProvider = "listOfOperationsNegativeTestDataProvider", priority = 1)
  public void listOfOperationsNegativeTest(@ParameterKey("Test case") final String testCase,
                                           final Offices offices,
                                           final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
