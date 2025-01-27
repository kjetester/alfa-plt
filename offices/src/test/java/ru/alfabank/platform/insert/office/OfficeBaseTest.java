package ru.alfabank.platform.insert.office;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static ru.alfabank.platform.businessobjects.offices.CbCodeName.EMPTY;
import static ru.alfabank.platform.businessobjects.offices.CloseReason.MOVING;
import static ru.alfabank.platform.businessobjects.offices.Kind.RETAIL_ACLUB_KIND;
import static ru.alfabank.platform.businessobjects.offices.ServiceCodeName.PIL_SERVICE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.offices.Offices;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Location;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.MetaInfo;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Operation;
import ru.alfabank.platform.businessobjects.offices.Offices.Office.Service;

public class OfficeBaseTest extends BaseTest {

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public Object[][] officePositiveTestDataProvider() {
    return new Object[][]{
        {
            "max количество полей с их max значениями",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setIdMasterSystem(Integer.valueOf(randomNumeric(9)))
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setPathUrl(randomAlphanumeric(31))
                        .setTitle(randomAlphanumeric(300))
                        .setDescription(randomAlphanumeric(300))
                        .setClose(true)
                        .setCloseFromDate(LocalDate.now().minusDays(30).toString())
                        .setCloseToDate(LocalDate.now().plusDays(30).toString())
                        .setCloseReason(MOVING.getValue())
                        .setRegNumberCb(randomAlphanumeric(50))
                        .setFullNameCB(randomAlphanumeric(300))
                        .setShortNameCB(randomAlphanumeric(300))
                        .setRegDateCB(LocalDate.now().minusYears(10).toString())
                        .setOpenDate(LocalDate.now().minusYears(10).toString())
                        .setCloseDate(LocalDate.now().minusDays(30).toString())
                        .setCloseDateCB(LocalDate.now().minusDays(30).toString())
                        .setOpenDateActual(LocalDate.now().minusYears(10).toString())
                        .setPhoneCB(randomAlphanumeric(100))
                        .setProfitRus(List.of(
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50)
                        ))
                        .setProfitEng(List.of(
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50)
                        ))
                        .setCode5(randomAlphanumeric(5))
                        .setLinkBalance(randomAlphanumeric(300))
                        .setLinkHeadOffice(randomAlphanumeric(300))
                        .setVspCount(5)
                        .setVisibleSite(true)
                        .setKinds(ALL_OF_KINDS_LIST)
                        .setAddressOfficial(randomAlphanumeric(300))
                        .setAddressSms(randomAlphanumeric(300))
                        .setLocations(List.of(
                            new Location.Builder()
                                .setFiasId("dacd4c67-1562-48da-83f5-d93154ff4a58")
                                .setKladrId(randomNumeric(19))
                                .setLat(55.6917333)
                                .setLon(37.660531)
                                .setPostcode(randomNumeric(6))
                                .setFederalDistrict("Центральный")
                                .setSubjectOfFederation("Москва")
                                .setCity("г Москва")
                                .setStreet("пр-кт Андропова")
                                .setHouse("18")
                                .setBlock("1")
                                .setBuilding("1")
                                .setLiter("А")
                                .setRoom("22/2-Н")
                                .setPlaceComment(randomAlphanumeric(300))
                                .build(),
                            new Location.Builder()
                                .setFiasId("fdaffeb4-e5c1-4e54-bd46-c555543ad706")
                                .setKladrId("7700000000007500277")
                                .setLat(55.6917421)
                                .setLon(37.6627433)
                                .setPostcode(randomNumeric(6))
                                .setFederalDistrict("Центральный")
                                .setSubjectOfFederation("Москва")
                                .setCity("г Москва")
                                .setStreet("пр-кт Андропова")
                                .setHouse("18")
                                .setBlock("7")
                                .setRoom("202")
                                .setPlaceComment(randomAlphanumeric(300))
                                .build())
                        )
                        .setServices(ALL_OF_SERVICES_LIST)
                        .setListOfOperations(ALL_OF_OPERATIONS_LIST)
                        .build()
                )
            )
        },
        {
            "max количество полей с их min значениями",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setIdMasterSystem(Integer.valueOf(randomNumeric(1)))
                        .setPid("0000")
                        .setMnemonic("aa00")
                        .setPathUrl(randomAlphanumeric(1))
                        .setTitle(randomAlphanumeric(1))
                        .setDescription(randomAlphanumeric(1))
                        .setClose(true)
                        .setCloseFromDate(LocalDate.now().minusDays(4).toString())
                        .setCloseToDate(LocalDate.now().plusDays(5).toString())
                        .setCloseReason(MOVING.getValue())
                        .setRegNumberCb(randomAlphanumeric(1))
                        .setFullNameCB(randomAlphanumeric(1))
                        .setShortNameCB(randomAlphanumeric(1))
                        .setRegDateCB(LocalDate.now().minusYears(3).toString())
                        .setOpenDate(LocalDate.now().minusYears(4).toString())
                        .setCloseDate(LocalDate.now().minusDays(52).toString())
                        .setCloseDateCB(LocalDate.now().minusDays(11).toString())
                        .setOpenDateActual(LocalDate.now().minusYears(3).toString())
                        .setPhoneCB(randomAlphanumeric(1))
                        .setProfitRus(List.of("setProfitRus"))
                        .setProfitEng(List.of("setProfitEng"))
                        .setCode5(randomAlphanumeric(1))
                        .setLinkBalance(randomAlphanumeric(1))
                        .setLinkHeadOffice(randomAlphanumeric(1))
                        .setVspCount(Integer.valueOf(randomNumeric(1)))
                        .setVisibleSite(false)
                        .setKinds(List.of(RETAIL_ACLUB_KIND))
                        .setAddressOfficial("Пермский край, г Кизел, пер Водопьянова, д 25")
                        .setAddressSms("Пермский кр, Кизел, Водопьянова, 25")
                        .setLocations(List.of(
                            new Location.Builder()
                                .setFiasId("8a3f8666-9121-419b-9bf7-55b5d3381349")
                                .setKladrId(randomNumeric(19))
                                .setLat(59.0512783)
                                .setLon(57.6471028)
                                .setPostcode(randomNumeric(6))
                                .setFederalDistrict("Приволжский")
                                .setSubjectOfFederation("Пермский край")
                                .setCity("Кизел")
                                .setStreet("пер Водопьянова")
                                .setHouse("25")
                                .setBlock("1")
                                .setBuilding("1")
                                .setLiter("А")
                                .setRoom("22/2-Н")
                                .setPlaceComment(randomAlphanumeric(1))
                                .build()))
                        .setServices(List.of(new Service(PIL_SERVICE.getCode(), PIL_SERVICE.getName())))
                        .setListOfOperations(List.of(new Operation(
                            randomAlphanumeric(5),
                            randomAlphanumeric(5),
                            randomAlphanumeric(5),
                            randomAlphanumeric(5))))
                        .build()))
        }
    };
  }

  /**
   * Data Provider.
   *
   * @return test data
   */
  @DataProvider
  public Object[][] officeNegativeTestDataProvider() {
    return new Object[][]{
        {
            "office.idMasterSystem == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setIdMasterSystem(null)
                        .build()
                )
            ),
            List.of("idMasterSystem")
        },
        {
            "office.pid == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(null)
                        .setMnemonic(randomAlphanumeric(4))
                        .build()
                )
            ),
            List.of("pid")
        },
        {
            "office.pid.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid("")
                        .setMnemonic(randomAlphanumeric(4))
                        .build()
                )
            ),
            List.of("pid")
        },
        {
            "office.pid.length > 4",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(5))
                        .setMnemonic(randomAlphanumeric(4))
                        .build()
                )
            ),
            List.of("pid")
        },
        {
            "office.pid.length < 4",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(3))
                        .setMnemonic(randomAlphanumeric(4))
                        .build()
                )
            ),
            List.of("pid")
        },
        {
            "office.pid. == non digits",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomAlphabetic(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .build()
                )
            ),
            List.of("pid")
        },
        {
            "office.mnemonic == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(null)
                        .build()
                )
            ),
            List.of("mnemonic")
        },
        {
            "office.mnemonic.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic("")
                        .build()
                )
            ),
            List.of("mnemonic")
        },
        {
            "office.mnemonic.length > 4",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(5))
                        .build()
                )
            ),
            List.of("mnemonic")
        },
        {
            "office.mnemonic.length < 4",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(3))
                        .build()
                )
            ),
            List.of("mnemonic")
        },
        {
            "office.pathUrl == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setPathUrl(null)
                        .build()
                )
            ),
            List.of("pathUrl")
        },
        {
            "office.pathUrl.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setPathUrl("")
                        .build()
                )
            ),
            List.of("pathUrl")
        },
        {
            "office.pathUrl.length > 31",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setPathUrl(randomAlphanumeric(32))
                        .build()
                )
            ),
            List.of("pathUrl")
        },
        {
            "office.title == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setTitle(null)
                        .build()
                )
            ),
            List.of("title")
        },
        {
            "office.title.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setTitle("")
                        .build()
                )
            ),
            List.of("title")
        },
        {
            "office.close == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setClose(null)
                        .build()
                )
            ),
            List.of("close")
        },
        {
            "office.shortNameCB == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setShortNameCB(null)
                        .build()
                )
            ),
            List.of("shortNameCB")
        },
        {
            "office.shortNameCB.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setShortNameCB("")
                        .build()
                )
            ),
            List.of("shortNameCB")
        },
        {
            "office.shortNameCB.length > 300",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setShortNameCB(randomAlphanumeric(301))
                        .build()
                )
            ),
            List.of("shortNameCB")
        },
        {
            "office.openDate == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setOpenDate(null)
                        .build()
                )
            ),
            List.of("openDate")
        },
        {
            "office.openDate.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setOpenDate("")
                        .build()
                )
            ),
            List.of("openDate")
        },
        {
            "office.openDate invalid format",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setOpenDate(randomAlphanumeric(5))
                        .build()
                )
            ),
            List.of("openDate")
        },
        {
            "office.visibleSite == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setVisibleSite(null)
                        .build()
                )
            ),
            List.of("visibleSite")
        },
        {
            "office.statusCB == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setStatusCB(null)
                        .build()
                )
            ),
            List.of("statusCB")
        },
        {
            "office.statusCB == empty object",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setStatusCB(EMPTY)
                        .build()
                )
            ),
            List.of("statusCB")
        },
        {
            "office.kinds == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(null)
                        .build()
                )
            ),
            List.of("kinds")
        },
        {
            "office.kinds == empty array",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setKinds(List.of())
                        .build()
                )
            ),
            List.of("kinds")
        },
        {
            "office.addressOfficial == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setAddressOfficial(null)
                        .build()
                )
            ),
            List.of("addressOfficial")
        },
        {
            "office.addressOfficial.length == 0",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setAddressOfficial("")
                        .build()
                )
            ),
            List.of("addressOfficial")
        },
        {
            "office.locations == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(null)
                        .build()
                )
            ),
            List.of("locations")
        },
        {
            "office.locations == empty array",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setLocations(List.of())
                        .build()
                )
            ),
            List.of("locations")
        },
        {
            "office.metaInfo == null",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(null)
                        .build()
                )
            ),
            List.of("metaInfo")
        },
        {
            "office.metaInfo == empty object",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setMetaInfo(new MetaInfo())
                        .build()
                )
            ),
            List.of("metaInfo")
        },
        {
            "max количество полей с превышением max значений",
            new Offices(
                LocalDateTime.now().atOffset(ZoneOffset.of(TIME_ZONE_OFFSET)).toString(),
                List.of(
                    new Office.Builder()
                        .using(BASE_OFFICE)
                        .setIdMasterSystem(Integer.valueOf(randomNumeric(9)))
                        .setPid(randomNumeric(4))
                        .setMnemonic(randomAlphanumeric(4))
                        .setPathUrl(randomAlphanumeric(31))
                        .setTitle(randomAlphanumeric(301))
                        .setDescription(randomAlphanumeric(301))
                        .setClose(true)
                        .setCloseFromDate(LocalDate.now().minusDays(30).toString())
                        .setCloseToDate(LocalDate.now().plusDays(30).toString())
                        .setCloseReason(MOVING.getValue())
                        .setRegNumberCb(randomAlphanumeric(50))
                        .setFullNameCB(randomAlphanumeric(300))
                        .setShortNameCB(randomAlphanumeric(301))
                        .setRegDateCB(LocalDate.now().minusYears(10).toString())
                        .setOpenDate(LocalDate.now().minusYears(10).toString())
                        .setCloseDate(LocalDate.now().minusDays(30).toString())
                        .setCloseDateCB(LocalDate.now().minusDays(30).toString())
                        .setOpenDateActual(LocalDate.now().minusYears(10).toString())
                        .setPhoneCB(randomAlphanumeric(101))
                        .setProfitRus(List.of(
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50)
                        ))
                        .setProfitEng(List.of(
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50),
                            randomAlphanumeric(50)
                        ))
                        .setCode5(randomAlphanumeric(6))
                        .setLinkBalance(randomAlphanumeric(301))
                        .setLinkHeadOffice(randomAlphanumeric(301))
                        .setVspCount(Integer.valueOf(randomNumeric(6)))
                        .setVisibleSite(true)
                        .setKinds(ALL_OF_KINDS_LIST)
                        .setAddressOfficial(randomAlphanumeric(301))
                        .setAddressSms(randomAlphanumeric(301))
                        .setLocations(List.of(
                            new Location.Builder()
                                .setFiasId("dacd4c67-1562-48da-83f5-d93154ff4a58")
                                .setKladrId(randomNumeric(19))
                                .setLat(55.6917333)
                                .setLon(37.660531)
                                .setPostcode(randomNumeric(6))
                                .setFederalDistrict("Центральный")
                                .setSubjectOfFederation("Москва")
                                .setCity("г Москва")
                                .setStreet("пр-кт Андропова")
                                .setHouse("18")
                                .setBlock("1")
                                .setBuilding("1")
                                .setLiter("А")
                                .setRoom("22/2-Н")
                                .setPlaceComment(randomAlphanumeric(300))
                                .build()
                        ))
                        .setServices(ALL_OF_SERVICES_LIST)
                        .setListOfOperations(ALL_OF_OPERATIONS_LIST)
                        .build()
                )
            ),
            List.of("title", "description", "shortNameCB", "phoneCB", "code5", "linkBalance", "linkHeadOffice", "vspCount", "addressOfficial", "addressSms")
        },
    };
  }
}
