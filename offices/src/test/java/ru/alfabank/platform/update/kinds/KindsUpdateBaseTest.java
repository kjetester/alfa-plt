package ru.alfabank.platform.update.kinds;

import static ru.alfabank.platform.businessobjects.offices.Kind.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
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
  public static Object[][] kindsUpdateNegativeTestDataProvider() {
    return new Object[0][];
  }
}
