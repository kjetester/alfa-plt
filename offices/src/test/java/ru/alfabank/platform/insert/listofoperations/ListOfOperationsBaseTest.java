package ru.alfabank.platform.insert.listofoperations;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Operation;

public class ListOfOperationsBaseTest extends BaseTest {

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  public Object[][] listOfOperationsPositiveTestDataProvider() {
    return new Object[][] {
        {
            "listOfOperations == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setListOfOperations(null)
                        .build()
                )
            )
        },
        {
            "listOfOperations == empty list",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setListOfOperations(List.of())
                        .build()
                )
            )
        },
        {
            "listOfOperations == ALL",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setListOfOperations(ALL_OF_OPERATIONS_LIST)
                        .build()
                )
            )
        },
        {
            "listOfOperations == random",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setListOfOperations(List.of(new Operation(
                            "random_code_1",
                            "random_name_1",
                            "random_codeCB_1",
                            "random_categoryCB_1")))
                        .build()
                )
            )
        },
        {
            "listOfOperations == different name",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setListOfOperations(
                            List.of(new Operation(
                                "10.1GiveSurety",
                                null,
                                "random_codeCB_1",
                                "random_categoryCB_1")
                            )
                        ).build()
                )
            ),
            List.of("name", "must not be blank")
        },
    };
  }

  /**
   * Data Provider.
   * @return test data
   */
  @DataProvider
  public Object[][] listOfOperationsNegativeTestDataProvider() {
    return new Object[][] {
      {
        "listOfOperations.name.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setListOfOperations(
                            List.of(new Operation(
                                "10.1GiveSurety",
                                "",
                                "random_codeCB_1",
                                "random_categoryCB_1")
                            )
                        ).build()
                )
            ),
          List.of("name", "must not be blank")
      }
    };
  }
}
