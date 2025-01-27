package ru.alfabank.platform.insert.kinds;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static ru.alfabank.platform.businessobjects.offices.Kind.CIB_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.ERR_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.MMB_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.NEW_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_ACLUB_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_CIK_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_STANDARD_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_VIP_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.SB_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.VIP_KIND;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;

public class KindsBaseTest extends BaseTest {

  /**
   * Data Provider.
   *
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
                            RETAIL_STANDARD_KIND,
                            RETAIL_VIP_KIND,
                            VIP_KIND,
                            RETAIL_CIK_KIND,
                            MMB_KIND,
                            SB_KIND,
                            CIB_KIND,
                            NEW_KIND,
                            RETAIL_ACLUB_KIND,
                            ERR_KIND))
                        .build()
                )
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
                        .setKinds(List.of(RETAIL_STANDARD_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(RETAIL_VIP_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(VIP_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(RETAIL_CIK_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(MMB_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(SB_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(CIB_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(NEW_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(RETAIL_ACLUB_KIND))
                        .build()
                )
            )
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
                        .setKinds(List.of(ERR_KIND))
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
  public static Object[][] kindsNegativeTestDataProvider() {
    return new Object[0][];
  }
}
