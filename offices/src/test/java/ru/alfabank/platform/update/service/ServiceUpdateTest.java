package ru.alfabank.platform.update.service;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class ServiceUpdateTest extends ServiceUpdateBaseTest {

  @Test(dataProvider = "serviceUpdatePositiveTestDataProvider")
  public void serviceUpdatePositiveTest(@ParameterKey("Test case") final String testCase,
                                        final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkServicesMapping(offices);
  }

  @Test(dataProvider = "serviceUpdateNegativeTestDataProvider", priority = 1)
  public void serviceUpdateNegativeTest(@ParameterKey("Test case") final String testCase,
                                        final Offices offices,
                                        final List<String> expectedErrorMessagesList) {
    final var expectedServiceCodesList =
        STEP.getServiceCodesListFromDataBase(BASE_OFFICE);
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.checkServiceCodesListWasNotChanged(BASE_OFFICE, expectedServiceCodesList);
  }
}
