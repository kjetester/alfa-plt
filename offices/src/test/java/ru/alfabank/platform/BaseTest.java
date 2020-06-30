package ru.alfabank.platform;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.DO;
import static ru.alfabank.platform.businessobjects.offices.Kind.MMB;
import static ru.alfabank.platform.businessobjects.offices.Operations.UPDATE;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.TestNGException;
import org.testng.annotations.BeforeMethod;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.MetaInfo;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Operation;
import ru.alfabank.platform.steps.offices.OfficesSteps;

public class BaseTest {

  protected static final String TIME_ZONE_OFFSET = "+03:00:00";
  protected static final Location BASE_LOCATION;
  protected static final MetaInfo BASE_META_INFO;
  protected static final Office BASE_OFFICE;
  protected static final OfficesSteps STEP = new OfficesSteps();
  protected static List<Operation> operationsList;

  static {
    try {
      MappingIterator<Operation> mi = new CsvMapper()
          .readerFor(Operation.class)
          .with(CsvSchema.emptySchema().withHeader().withColumnSeparator('\t'))
          .readValues(new FileReader("src/test/resources/cb_operations.csv"));
      operationsList = mi.readAll();
      operationsList.add(new Operation(
          "random_code_1",
          "random_name_1",
          "random_codeCB_1",
          "random_categoryCB_1"));
    } catch (IOException e) {
      e.printStackTrace();
      throw new TestNGException(e.toString());
    }
    BASE_LOCATION = new Location.Builder()
        .setFiasId("9b646403-4605-456a-9320-22d5425748c5")
        .setKladrId("7700000000071640057")
        .setLat(55.691273)
        .setLon(37.620186)
        .setPostcode("115035")
        .setFederalDistrict("Центральный")
        .setSubjectOfFederation("г Москва")
        .setCity("Москва")
        .setStreet("Варшавское шоссе")
        .setHouse("18")
        .setBlock("1")
        .setBuilding("2")
        .setLiter("А")
        .setRoom("3")
        .setPlaceComment("Офис")
        .build();
    BASE_META_INFO = new MetaInfo(
        UPDATE.getValue(),
        LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString());
    BASE_OFFICE = new Office.Builder()
        .setIdMasterSystem(Integer.valueOf(randomNumeric(4)))
        .setPid(randomNumeric(4))
        .setMnemonic(randomAlphanumeric(4))
        .setPathUrl("office_path_url")
        .setTitle("office_title")
        .setClose(false)
        .setShortNameCB("office_short_name_CB")
        .setOpenDate(LocalDate.now().toString())
        .setVisibleSite(true)
        .setStatusCB(DO)
        .setKinds(List.of(MMB))
        .setAddressOfficial("101000, г. Москва, ул. Мясницкая, д. 13, стр. 1")
        .setLocations(List.of(BASE_LOCATION))
        .setMetaInfo(BASE_META_INFO)
        .setBranchID(0)
        .setUseInCosmo(0)
        .setIsOnReconstruction(0)
        .setParking(0)
        .build();
  }

  /**
   * Clear up err queue.
   */
  @BeforeMethod
  public void clearErrQueueAndDataBase() {
    STEP.cleanUpDataBase();
    STEP.clearErrMessageQueue();
  }
}
