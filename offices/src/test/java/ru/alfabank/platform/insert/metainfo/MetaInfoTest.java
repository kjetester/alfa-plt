package ru.alfabank.platform.insert.metainfo;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class MetaInfoTest extends MetaInfoBaseTest {

  /**
   * Clean up the DataBase.
   */
  @BeforeMethod
  public void cleanUpDataBase() {
    STEP.cleanUpDataBase();
  }

  @Test(dataProvider = "metaInfoPositiveTestDataProvider")
  public void metaInfoPositiveTest(@ParameterKey("Test case") final String testCase,
                                   final Offices offices) {
    STEP.sendMessageToInQueueAssumingSuccess(offices);
    STEP.checkChangeDateTimeMapping(offices);
  }

  @Test(dataProvider = "metaInfoNegativeTestDataProvider", priority = 1)
  public void metaInfoNegativeTest(@ParameterKey("Test case") final String testCase,
                                   final Offices offices,
                                   final List<String> expectedErrorMessagesList) {
    STEP.sendMessageToInQueue(offices);
    STEP.ensureErrMessageExistsAndCheckItsContent(expectedErrorMessagesList);
    STEP.ensureIfOfficesWereNotSaved(offices);
  }
}
