package ru.alfabank.platform.update.kinds;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class KindsUpdateTest extends KindsUpdateBaseTest {

  @Test(dataProvider = "kindsUpdatePositiveTestDataProvider")
  public void kindsUpdatePositiveTest(@ParameterKey("Test case") final String testCase,
                                      final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkOfficesKindsMapping(offices);
  }

  @Test(dataProvider = "kindsUpdateNegativeTestDataProvider", priority = 1)
  public void kindsUpdateNegativeTest(@ParameterKey("Test case") final String testCase,
                                      final Offices offices,
                                      final List<String> expectedErrorMessagesList) {
    final var expectedKindsCodes =
        STEP.getOfficeKindsCodesFromDataBase(BASE_OFFICE);
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.checkKindsWereNotChanged(BASE_OFFICE, expectedKindsCodes);
  }
}
