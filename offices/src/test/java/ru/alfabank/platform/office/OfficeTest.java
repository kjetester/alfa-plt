package ru.alfabank.platform.office;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class OfficeTest extends OfficeBaseTest {

  @Test(dataProvider = "officePositiveTestDataProvider")
  public void officePositiveTest(@ParameterKey("Test case")
                                   final String testCase,
                                 final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkOfficeMapping(offices);
  }

  @Test(dataProvider = "officeNegativeTestDataProvider", priority = 1)
  public void officeNegativeTest(@ParameterKey("Test case")
                                   final String testCase,
                                 final Offices offices,
                                 final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
