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
                            new Service(POINT_ONE_SERVICE.getCode(), POINT_ONE_SERVICE.getName()),
                            new Service(POINT_TWO_SERVICE.getCode(), POINT_TWO_SERVICE.getName()),
                            new Service(SERVICE_OFFICE_SERVICE.getCode(), SERVICE_OFFICE_SERVICE.getName()),
                            new Service(CLIENT_OFFICE_SERVICE.getCode(), CLIENT_OFFICE_SERVICE.getName()),
                            new Service(CASH_MAS_SERVICE.getCode(), CASH_MAS_SERVICE.getName()),
                            new Service(SAFE_SERVICE.getCode(), SAFE_SERVICE.getName()),
                            new Service(DISABLED_SERVICE.getCode(), DISABLED_SERVICE.getName()),
                            new Service(CASH_GBP_SERVICE.getCode(), CASH_GBP_SERVICE.getName()),
                            new Service(CASH_CHF_SERVICE.getCode(), CASH_CHF_SERVICE.getName()),
                            new Service(MOMENT_CARD_SERVICE.getCode(), MOMENT_CARD_SERVICE.getName()),
                            new Service(WIFI_SERVICE.getCode(), WIFI_SERVICE.getName()),
                            new Service(PARTNER_SERVICE.getCode(), PARTNER_SERVICE.getName()),
                            new Service(OVERDRAFT_SERVICE.getCode(), OVERDRAFT_SERVICE.getName()),
                            new Service(CASH_OP_SERVICE.getCode(), CASH_OP_SERVICE.getName()),
                            new Service(OFFICE_MB_IP_SERVICE.getCode(), OFFICE_MB_IP_SERVICE.getName()),
                            new Service(MORTGAGE_SERVICE.getCode(), MORTGAGE_SERVICE.getName()),
                            new Service(DC_SERVICE.getCode(), DC_SERVICE.getName()),
                            new Service(CC_SERVICE.getCode(), CC_SERVICE.getName()),
                            new Service(PIL_SERVICE.getCode(), PIL_SERVICE.getName()),
                            new Service(INVESTMENT_SERVICE.getCode(), INVESTMENT_SERVICE.getName()),
                            new Service(SHARE_SERVICE.getCode(), SHARE_SERVICE.getName()),
                            new Service(COURIER_SERVICE.getCode(), COURIER_SERVICE.getName()),
                            new Service(ERR_SERVICE.getCode(), DISABLED_SERVICE.getName())))
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
                            new Service(DISABLED_SERVICE.getCode(), DISABLED_SERVICE.getName())))
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
                            new Service(ERR_SERVICE.getCode(), DISABLED_SERVICE.getName())))
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
                            new Service(null, DISABLED_SERVICE.getName())))
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
                            new Service("", DISABLED_SERVICE.getName())))
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
                            new Service(DISABLED_SERVICE.getCode(), null)))
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
                            new Service(DISABLED_SERVICE.getCode(), "")))
                        .build()
                )
            ),
            List.of("name", "must not be blank")
        }
    };
  }
}
