package ru.alfabank.platform.update.service;

import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Service;
import ru.alfabank.platform.update.UpdateBaseTest;

public class ServiceUpdateBaseTest extends UpdateBaseTest {

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public Object[][] serviceUpdatePositiveTestDataProvider() {
    return new Object[][]{
        {
            "services == ALL + ERR",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setServices(List.of(
                            new Service(POINT_ONE.getCode(), POINT_ONE.getName()),
                            new Service(POINT_TWO.getCode(), POINT_TWO.getName()),
                            new Service(SERVICE_OFFICE.getCode(), SERVICE_OFFICE.getName()),
                            new Service(CLIENT_OFFICE.getCode(), CLIENT_OFFICE.getName()),
                            new Service(SAFE.getCode(), SAFE.getName()),
                            new Service(DISABLED.getCode(), DISABLED.getName()),
                            new Service(CASH_CHF.getCode(), CASH_CHF.getName()),
                            new Service(CASH_GBP.getCode(), CASH_GBP.getName()),
                            new Service(CASH_MAS.getCode(), CASH_MAS.getName()),
                            new Service(CASH_OP.getCode(), CASH_OP.getName()),
                            new Service(MOMENT_CARD.getCode(), MOMENT_CARD.getName()),
                            new Service(WIFI.getCode(), WIFI.getName()),
                            new Service(PARTNER.getCode(), PARTNER.getName()),
                            new Service(OVERDRAFT.getCode(), OVERDRAFT.getName()),
                            new Service(OFFICE_MB_IP.getCode(), OFFICE_MB_IP.getName()),
                            new Service(PILOT.getCode(), PILOT.getName()),
                            new Service(DC.getCode(), DC.getName()),
                            new Service(CC.getCode(), CC.getName()),
                            new Service(PIL.getCode(), PIL.getName()),
                            new Service(ERR.getCode(), DISABLED.getName())))
                        .build()
                )
            )
        },
        {
            "services == one - DISABLED",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setServices(List.of(
                            new Service(DISABLED.getCode(), DISABLED.getName())))
                        .build()
                )
            )
        },
        {
            "services.code == ERR",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setServices(List.of(
                            new Service(ERR.getCode(), DISABLED.getName())))
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
  public Object[][] serviceUpdateNegativeTestDataProvider() {
    return new Object[][]{
        {
            "services == empty object",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setServices(List.of(
                            new Service()))
                        .build()
                )
            ),
            List.of("code", "name", "must not be blank")
        },
        {
            "services.code == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setServices(List.of(
                            new Service(null, DISABLED.getName())))
                        .build()
                )
            ),
            List.of("code", "must not be blank")
        },
        {
            "services.code.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setServices(List.of(
                            new Service("", DISABLED.getName())))
                        .build()
                )
            ),
            List.of("code", "must not be blank")
        },
        {
            "services.name == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setServices(List.of(
                            new Service(DISABLED.getCode(), null)))
                        .build()
                )
            ),
            List.of("name", "must not be blank")
        },
        {
            "services.name.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setServices(List.of(
                            new Service(DISABLED.getCode(), "")))
                        .build()
                )
            ),
            List.of("name", "must not be blank")
        }
    };
  }
}
