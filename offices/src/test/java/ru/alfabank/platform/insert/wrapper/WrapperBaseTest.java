package ru.alfabank.platform.insert.wrapper;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;

public class WrapperBaseTest extends BaseTest {

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  public Object[][] wrapperPositiveTestDataProvider() {
    String pid = randomNumeric(4);
    return new Object[][] {
        {   "Текущее время и два разных отделения",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4)
                        ).build(),
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .build()))
        },
        {   "Текущее время и два отделения с одинаковыми pid и разными mnemonic",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(pid)
                        .setMnemonic(randomAlphanumeric(4))
                        .build(),
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(pid)
                        .setMnemonic(randomAlphanumeric(4))
                        .build()))
        },
        {
            "Текущее время и одно отделение",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .build()))
        },
        {   "Текущее время плюс 1 час и два разных отделения",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET))
                    .plus(1, HOURS).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .build(),
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .build()))
        },
        {   "Текущее время минус 1 час и одно отделение",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET))
                    .minus(1, HOURS).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .build()))
        }
    };
  }

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  public Object[][] wrapperNegativeTestDataProvider() {
    return new Object[][] {
        {
            "wrapper.timestamp == no timeZone",
            new Offices(
                LocalDateTime.now().toString(),
                List.of(BASE_OFFICE)),
            List.of("Cannot deserialize value of type `java.time.OffsetDateTime`")
        },
        {
            "wrapper.timestamp == format",
            new Offices(
                randomAlphanumeric(5),
                List.of(BASE_OFFICE)),
            List.of("Cannot deserialize value of type `java.time.OffsetDateTime`")
        },
        {
            "wrapper.timestamp == null",
            new Offices(
                null,
                List.of(BASE_OFFICE)),
            List.of("timestamp : null must not be null")
        },
        {
            "wrapper.timestamp.length == 0",
            new Offices(
                "",
                List.of(BASE_OFFICE)),
            List.of("timestamp : null must not be null")
        },
        {
            "wrapper.offices == []",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of()),
            List.of("offices", "must not be empty")
        },
        {
            "wrapper.offices == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                null),
            List.of("offices", "must not be empty")
        },
        {
            "wrapper.offices == [{}]",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(new Office())),
            List.of("null must not be null")
        },
        {
            "wrapper.offices == [{},{}]",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(new Office(), new Office())),
            List.of("null must not be null")
        }
    };
  }
}
