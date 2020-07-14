package ru.alfabank.platform.update.locations;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class LocationsUpdateTest extends LocationsUpdateBaseTest {

  @Test(dataProvider = "locationsUpdatePositiveTestDataProvider")
  public void locationsUpdatePositiveTest(@ParameterKey("Test case") final String testCase,
                                          final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkIfOfficesWereSaved(offices);
    STEP.checkLocationMapping(offices);
  }

  @Test(dataProvider = "locationsUpdateNegativeTestDataProvider", priority = 1)
  public void locationsUpdateNegativeTest(@ParameterKey("Test case") final String testCase,
                                          final Offices offices,
                                          final List<String> expectedErrorMessagesList) {
    final var expectedLocation =
        STEP.getMetroNameCityIdLocationFromDataBase(BASE_OFFICE);
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.checkLocationWasNotChanged(BASE_OFFICE, expectedLocation);
  }
}
