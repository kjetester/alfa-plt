package ru.alfabank.platform.update.kinds;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.businessobjects.offices.Kind;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.update.UpdateBaseTest;

public class KindsUpdateBaseTest extends UpdateBaseTest {

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] kindsUpdatePositiveTestDataProvider() {
    return new Object[][]{
        {
            "Kinds == ALL",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
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
            )
        },
        {
            "Kinds == retailStandart",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setKinds(List.of(Kind.RETAIL_STANDARD))
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
                        .setKinds(List.of(Kind.RETAIL_VIP))
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
                        .setKinds(List.of(Kind.VIP))
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
                        .setKinds(List.of(Kind.RETAIL_CIK))
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
                        .setKinds(List.of(Kind.MMB))
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
                        .setKinds(List.of(Kind.SB))
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
                        .setKinds(List.of(Kind.CIB))
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
                        .setKinds(List.of(Kind.NEW))
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
                        .setKinds(List.of(Kind.RETAIL_ACLUB))
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
                        .setKinds(List.of(Kind.ERR_KIND))
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
  public static Object[][] kindsUpdateNegativeTestDataProvider() {
    return new Object[0][];
  }
}
