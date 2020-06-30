package ru.alfabank.platform.insert.kinds;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Kind.Code;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class KindsTest extends ru.alfabank.platform.insert.kinds.KindsBaseTest {

  @Test(dataProvider = "kindsPositiveTestDataProvider")
  public void kindsPositiveTest(@ParameterKey("Test case")
                                   final String testCase,
                                final Offices offices,
                                final List<Code> expectedCodesList) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkKindsMapping(offices, expectedCodesList);
  }

  @Test(dataProvider = "kindsNegativeTestDataProvider", priority = 1)
  public void kindsNegativeTest(@ParameterKey("Test case")
                                  final String testCase,
                                final Offices offices,
                                final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
