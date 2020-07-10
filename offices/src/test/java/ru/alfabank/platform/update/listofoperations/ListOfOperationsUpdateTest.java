package ru.alfabank.platform.update.listofoperations;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class ListOfOperationsUpdateTest extends ListOfOperationsUpdateBaseTest {

  @Test(dataProvider = "listOfOperationsUpdatePositiveTestDataProvider")
  public void listOfOperationsUpdatePositiveTest(@ParameterKey("Test case")
                                                   final String testCase,
                                                 final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkListOfOperationsMapping(offices);
  }

  @Test(dataProvider = "listOfOperationsUpdateNegativeTestDataProvider", priority = 1)
  public void listOfOperationsUpdateNegativeTest(@ParameterKey("Test case")
                                                   final String testCase,
                                                 final Offices offices,
                                                 final List<String> expectedErrorMessagesList) {
  }
}
