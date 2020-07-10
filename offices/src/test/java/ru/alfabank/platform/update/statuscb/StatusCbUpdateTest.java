package ru.alfabank.platform.update.statuscb;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class StatusCbUpdateTest extends StatusCbUpdateBaseTest {

  @Test(dataProvider = "statusCbUpdatePositiveTestDataProvider")
  public void statusCbUpdatePositiveTest(@ParameterKey("Test case")
                                           final String testCase,
                                         final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkOfficeMapping(offices);
  }

  @Test(dataProvider = "statusCbUpdateNegativeTestDataProvider", priority = 1)
  public void statusCbUpdateNegativeTest(@ParameterKey("Test case")
                                           final String testCase,
                                         final Offices offices,
                                         final List<String> expectedErrorMessagesList) {
    final var expectedOffice =
        STEP.getOfficeFromDataBase(BASE_OFFICE);
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.checkOfficeWasNotChanged(expectedOffice);
  }
}
