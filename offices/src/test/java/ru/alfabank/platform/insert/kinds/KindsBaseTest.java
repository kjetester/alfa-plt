package ru.alfabank.platform.insert.kinds;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.offices.Kind;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;

public class KindsBaseTest extends BaseTest {

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  public static Object[][] kindsPositiveTestDataProvider() {
    return new Object[][]{
        {
            "Kinds == ALL",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(
                            Kind.RETAIL_STANDARD,
                            Kind.RETAIL_VIP,
                            Kind.VIP,
                            Kind.RETAIL_CIK,
                            Kind.MMB,
                            Kind.SB,
                            Kind.CIB,
                            Kind.NEW,
                            Kind.RETAIL_ACLUB,
                            Kind.ERR_KIND))
                        .build()
                )
            ),
            List.of(
                Kind.Code.RETAIL,
                Kind.Code.VIP,
                Kind.Code.VIPMNGR,
                Kind.Code.VIP_CLIENT,
                Kind.Code.MORTGAGE,
                Kind.Code.SME,
                Kind.Code.CORPORATE,
                Kind.Code.ACLUB
            )
        },
        {
            "Kinds == retailStandart",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.RETAIL_STANDARD))
                        .build()
                )
            ),
            List.of(Kind.Code.RETAIL)
        },
        {
            "Kinds == retailVip",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.RETAIL_VIP))
                        .build()
                )
            ),
            List.of(Kind.Code.RETAIL, Kind.Code.VIP)
        },
        {
            "Kinds == vip",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.VIP))
                        .build()
                )
            ),
            List.of(Kind.Code.RETAIL, Kind.Code.VIP, Kind.Code.VIPMNGR, Kind.Code.VIP_CLIENT)
        },
        {
            "Kinds == retailCIK",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.RETAIL_CIK))
                        .build()
                )
            ),
            List.of(Kind.Code.RETAIL, Kind.Code.MORTGAGE)
        },
        {
            "Kinds == MMB",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.MMB))
                        .build()
                )
            ),
            List.of(Kind.Code.SME)
        },
        {
            "Kinds == SB",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.SB))
                        .build()
                )
            ),
            List.of(Kind.Code.CORPORATE)
        },
        {
            "Kinds == CIB",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.CIB))
                        .build()
                )
            ),
            List.of(Kind.Code.CORPORATE)
        },
        {
            "Kinds == new",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.NEW))
                        .build()
                )
            ),
            List.of()
        },
        {
            "Kinds == retailAclub",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.RETAIL_ACLUB))
                        .build()
                )
            ),
            List.of(Kind.Code.ACLUB, Kind.Code.VIP)
        },
        {
            "Kinds == undefined value",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of(Kind.ERR_KIND))
                        .build()
                )
            ),
            List.of()
        }
    };
  }

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  public static Object[][] kindsNegativeTestDataProvider() {
    return new Object[0][];
  }
}
