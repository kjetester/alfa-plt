package ru.alfabank.platform;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static ru.alfabank.platform.businessobjects.offices.Kind.CIB_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.MMB_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.NEW_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_ACLUB_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_CIK_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_STANDARD_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_VIP_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.SB_KIND;
import static ru.alfabank.platform.businessobjects.offices.Kind.VIP_KIND;
import static ru.alfabank.platform.businessobjects.offices.Operations.UPDATE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CASH_CHF_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CASH_GBP_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CASH_MAS_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CASH_OP_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CC_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.CLIENT_OFFICE_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.COURIER_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.DC_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.DISABLED_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.INVESTMENT_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.MOMENT_CARD_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.MORTGAGE_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.OFFICE_MB_IP_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.OVERDRAFT_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.PARTNER_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.PIL_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.POINT_ONE_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.POINT_TWO_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.SAFE_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.SERVICE_OFFICE_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.SHARE_SERVICE;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.WIFI_SERVICE;

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
          RETAIL_STANDARD_KIND,
          RETAIL_VIP_KIND,
          VIP_KIND,
          RETAIL_CIK_KIND,
          MMB_KIND,
          SB_KIND,
          CIB_KIND,
          NEW_KIND,
          RETAIL_ACLUB_KIND);
      ALL_OF_SERVICES_LIST = List.of(
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
          new Service(COURIER_SERVICE.getCode(), COURIER_SERVICE.getName())
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
        .setKladrId(randomNumeric(19))
        .setLat(55.686764)
        .setLon(37.612727)
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
        .setKinds(List.of(MMB_KIND))
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
