package ru.alfabank.platform.update.statuscb;

import static ru.alfabank.platform.businessobjects.offices.CbCodeName.BRANCH;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.DO;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.ERR;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.KKO;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.NO_CODE;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.NO_NAME;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.OKVKU;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.OO;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.RO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.update.UpdateBaseTest;

public class StatusCbUpdateBaseTest extends UpdateBaseTest {

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] statusCbUpdatePositiveTestDataProvider() {
    return new Object[][]{
        {
            "statusCb == DO",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(DO)
                        .build()
                )
            )
        },
        {
            "statusCb == KKO",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(KKO)
                        .build()
                )
            )
        },
        {
            "statusCb == RO",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(RO)
                        .build()
                )
            )
        },
        {
            "statusCb == OO",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(OO)
                        .build()
                )
            )
        },
        {
            "statusCb == BRANCH",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(BRANCH)
                        .build()
                )
            )
        }
    };
  }

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] statusCbUpdateNegativeTestDataProvider() {
    return new Object[][]{
        {
            "statusCb == OKVKU",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(OKVKU)
                        .build()
                )
            ),
            List.of("statusCB", "OKVKU")
        },
        {
            "statusCb == ERR",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(ERR)
                        .build()
                )
            ),
            List.of("statusCB", "ERR")
        },
        {
            "statusCb == NO_CODE",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(NO_CODE)
                        .build()
                )
            ),
            List.of("statusCB.code")
        },
        {
            "statusCb == NO_NAME",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setStatusCB(NO_NAME)
                        .build()
                )
            ),
            List.of("statusCB.name")
        }
    };
  }
}
