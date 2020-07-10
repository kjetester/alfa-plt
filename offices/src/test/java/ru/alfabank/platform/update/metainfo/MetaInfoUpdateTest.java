package ru.alfabank.platform.update.metainfo;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class MetaInfoUpdateTest extends MetaInfoUpdateBaseTest {

  @Test(dataProvider = "metaInfoUpdatePositiveTestDataProvider")
  public void metaInfoUpdatePositiveTest(@ParameterKey("Test case")
                                           final String testCase,
                                         final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkChangeDateTimeMapping(offices);
  }

  @Test(dataProvider = "metaInfoUpdateNegativeTestDataProvider", priority = 1)
  public void metaInfoUpdateNegativeTest(@ParameterKey("Test case")
                                           final String testCase,
                                         final Offices offices,
                                         final List<String> expectedErrorMessagesList) {
    final var expectedDateTime =
        STEP.getChangedDateTimeFromDataBase(BASE_OFFICE);
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.checkChangeDateTimeWasNotChanged(BASE_OFFICE, expectedDateTime);
  }
}
