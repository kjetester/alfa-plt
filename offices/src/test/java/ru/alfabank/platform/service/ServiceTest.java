package ru.alfabank.platform.service;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Kind.Code;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.ServiceCodeName;

public class ServiceTest extends ServiceBaseTest {

  @Test(dataProvider = "servicePositiveTestDataProvider")
  public void servicePositiveTest(@ParameterKey("Test case")
                                    final String testCase,
                                  final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkServicesMapping(offices);
  }

  @Test(dataProvider = "serviceNegativeTestDataProvider", priority = 1)
  public void serviceNegativeTest(@ParameterKey("Test case")
                                    final String testCase,
                                  final Offices offices,
                                  final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
