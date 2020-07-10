package ru.alfabank.platform.update;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.BeforeClass;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.offices.Offices;

public class UpdateBaseTest extends BaseTest {

  /**
   * Create base office.
   */
  @BeforeClass
  public static void createBaseOffice() {
    STEP.cleanUpErrMessageQueue();
    STEP.cleanUpDataBase();
    STEP.sendMessageToInQueueAssumingSuccess(
        new Offices(
            LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
            List.of(BASE_OFFICE)));
  }
}
