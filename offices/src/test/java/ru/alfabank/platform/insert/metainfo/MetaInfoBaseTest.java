package ru.alfabank.platform.insert.metainfo;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static ru.alfabank.platform.businessobjects.offices.Operations.UPDATE;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.MetaInfo;
import ru.alfabank.platform.businessobjects.offices.Operations;

public class MetaInfoBaseTest extends BaseTest {

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public Object[][] metaInfoPositiveTestDataProvider() {
    return new Object[][]{
        {
            "Валидная операция и текущее время",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                UPDATE.getValue(),
                                LocalDateTime
                                    .now()
                                    .atOffset(ZoneOffset.of(TIME_ZONE_OFFSET))
                                    .toString()))
                        .build()
                )
            )
        },
        {
            "Валидная операция и текущее время плюс 1 час",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                UPDATE.getValue(),
                                LocalDateTime
                                    .now()
                                    .plusHours(1)
                                    .atOffset(ZoneOffset.of(TIME_ZONE_OFFSET))
                                    .toString()))
                        .build()
                )
            )
        },
        {
            "Валидная операция и текущее время минус 1 час",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                UPDATE.getValue(),
                                LocalDateTime
                                    .now()
                                    .minusHours(1)
                                    .atOffset(ZoneOffset.of(TIME_ZONE_OFFSET))
                                    .toString()))
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
  public Object[][] metaInfoNegativeTestDataProvider() {
    return new Object[][]{
        {
            "metainfo.operation == err",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                Operations.ERR.getValue(),
                                LocalDateTime
                                    .now()
                                    .minusHours(1)
                                    .atOffset(ZoneOffset.of(TIME_ZONE_OFFSET))
                                    .toString()))
                        .build()
                )
            ),
            List.of("Cannot deserialize value of type `ru.alfabank.offices.enums.OperationType`")
        },
        {
            "metainfo.operation.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                "",
                                LocalDateTime
                                    .now()
                                    .minusHours(1)
                                    .atOffset(ZoneOffset.of(TIME_ZONE_OFFSET))
                                    .toString()))
                        .build()
                )
            ),
            List.of("Cannot deserialize value of type `ru.alfabank.offices.enums.OperationType`")
        },
        {
            "metainfo.operation == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                null,
                                LocalDateTime
                                    .now()
                                    .minusHours(1)
                                    .atOffset(ZoneOffset.of(TIME_ZONE_OFFSET))
                                    .toString()))
                        .build()
                )
            ),
            List.of("metaInfo.operation", "null must not be null")
        },
        {
            "metainfo.time == format",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                Operations.UPDATE.getValue(),
                                LocalDateTime.now().toString()))
                        .build()
                )
            ),
            List.of("Cannot deserialize value of type `java.time.OffsetDateTime`")
        },
        {
            "metainfo.time == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                Operations.UPDATE.getValue(),
                                null))
                        .build()
                )
            ),
            List.of("metaInfo.changeDatetime", "null must not be null")
        },
        {
            "metainfo.time.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(
                            new MetaInfo(
                                Operations.UPDATE.getValue(),
                                ""))
                        .build()
                )
            ),
            List.of("metaInfo.changeDatetime", "null must not be null")
        },
    };
  }
}
