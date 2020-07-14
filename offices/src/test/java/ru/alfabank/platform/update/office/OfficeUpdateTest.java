package ru.alfabank.platform.update.office;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class OfficeUpdateTest extends OfficeUpdateBaseTest {

  @Test(dataProvider = "officeUpdatePositiveTestDataProvider")
  public void officeUpdatePositiveTest(@ParameterKey("Test case") final String testCase,
                                       final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkOfficeMapping(offices);
  }

  @Test(dataProvider = "officeUpdateNegativeTestDataProvider", priority = 1)
  public void officeUpdateNegativeTest(@ParameterKey("Test case") final String testCase,
                                       final Offices offices,
                                       final List<String> expectedErrorMessagesList) {
    final var expectedOffice =
        STEP.getOfficeFromDataBase(BASE_OFFICE);
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.checkOfficeWasNotChanged(expectedOffice);
  }
}
