package ru.alfabank.platform;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.DO;
import static ru.alfabank.platform.businessobjects.offices.Kind.MMB;
import static ru.alfabank.platform.businessobjects.offices.Operations.UPDATE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CASH_CHF;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CASH_GBP;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CASH_MAS;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CASH_OP;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CC;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CLIENT_OFFICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.DC;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.DISABLED;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.MOMENT_CARD;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.OFFICE_MB_IP;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.OVERDRAFT;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.PARTNER;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.PIL;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.PILOT;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.POINT_ONE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.POINT_TWO;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.SAFE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.SERVICE_OFFICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.WIFI;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.TestNGException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.alfabank.platform.businessobjects.offices.CbCodeName;
import ru.alfabank.platform.businessobjects.offices.Kind;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.MetaInfo;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Operation;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Service;
import ru.alfabank.platform.steps.offices.OfficesSteps;

public class BaseTest {

  protected static final String TIME_ZONE_OFFSET = "+03:00:00";
  protected static final Location BASE_LOCATION;
  protected static final MetaInfo BASE_META_INFO;
  protected static final Office BASE_OFFICE;
  protected static final OfficesSteps STEP = new OfficesSteps();

  protected static final List<Kind> ALL_OF_KINDS_LIST;
  protected static final List<Operation> ALL_OF_OPERATIONS_LIST;
  protected static final List<Service> ALL_OF_SERVICES_LIST;

  static {
    try {
      ALL_OF_KINDS_LIST = List.of(
          Kind.RETAIL_STANDARD,
          Kind.RETAIL_VIP,
          Kind.VIP,
          Kind.RETAIL_CIK,
          Kind.MMB,
          Kind.SB,
          Kind.CIB,
          Kind.NEW,
          Kind.RETAIL_ACLUB);
      ALL_OF_SERVICES_LIST = List.of(
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
          new Service(PIL.getCode(), PIL.getName())
      );
      MappingIterator<Operation> mi = new CsvMapper()
          .readerFor(Operation.class)
          .with(CsvSchema.emptySchema().withHeader().withColumnSeparator('\t'))
          .readValues(new FileReader("src/test/resources/cb_operations.csv"));
      ALL_OF_OPERATIONS_LIST = mi.readAll();
      ALL_OF_OPERATIONS_LIST.add(new Operation(
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
        .setPathUrl(randomAlphanumeric(4))
        .setTitle(randomAlphanumeric(4))
        .setClose(false)
        .setShortNameCB(randomAlphanumeric(4))
        .setOpenDate(LocalDate.now().toString())
        .setVisibleSite(true)
        .setStatusCB(CbCodeName.DO)
        .setKinds(List.of(MMB))
        .setAddressOfficial(randomAlphanumeric(4))
        .setLocations(List.of(BASE_LOCATION))
        .setListOfOperations(ALL_OF_OPERATIONS_LIST)
        .setMetaInfo(BASE_META_INFO)
        .setBranchID(0)
        .setUseInCosmo(0)
        .setIsOnReconstruction(0)
        .setParking(0)
        .build();
  }

  /**
   * Clean up the DLQ.
   */
  @BeforeSuite
  @BeforeMethod
  public void cleanUpErrQueue() {
    STEP.cleanUpErrMessageQueue();
  }
}
