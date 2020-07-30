package ru.alfabank.platform.businessobjects.offices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class Offices extends AbstractBusinessObject {

  private static final Logger LOGGER = LogManager.getLogger(Offices.class);

  private final String timestamp;
  private final List<Office> offices;

  @JsonCreator
  public Offices(@JsonProperty("timestamp") final String timestamp,
                 @JsonProperty("offices") final List<Office> offices) {
    this.timestamp = timestamp;
    this.offices = offices;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public List<Office> getOffices() {
    return offices;
  }

  public static class Office extends AbstractBusinessObject {

    @Getter private final Integer idMasterSystem;
    @Getter private final String pid;
    @Getter private final String mnemonic;
    @Getter private final String pathUrl;
    @Getter private final String title;
    @Getter private final String description;
    @Getter private final Boolean close;
    @Getter private final String closeFromDate;
    @Getter private final String closeToDate;
    @Getter private final String closeReason;
    @Getter private final String regNumberCb;
    @Getter private final String fullNameCB;
    @Getter private final String shortNameCB;
    @Getter private final String regDateCB;
    @Getter private final String openDate;
    @Getter private final String closeDate;
    @Getter private final String closeDateCB;
    @Getter private final String openDateActual;
    @Getter private final String phoneCB;
    @Getter private final List<String> profitRus;
    @Getter private final List<String> profitEng;
    @Getter private final String code5;
    @Getter private final String linkBalance;
    @JsonProperty("linkHeadoffice") @Getter
    private final String linkHeadOffice;
    @Getter private final Integer vspCount;
    @Getter private final Boolean visibleSite;
    @Getter private final CbCodeName statusCB;
    @Getter private final List<Kind> kinds;
    @Getter private final String addressOfficial;
    @Getter private final String addressSms;
    @Getter private final List<Location> locations;
    @Getter private final List<Service> services;
    @Getter private final List<Operation> listOfOperations;
    @Getter private final MetaInfo metaInfo;
    @Getter private List<String> messages;
    @JsonIgnore @Getter
    private Integer branchID;
    @JsonIgnore @Getter
    private Integer useInCosmo;
    @JsonIgnore @Getter
    private Integer isOnReconstruction;
    @JsonIgnore @Getter
    private Integer parking;
    @JsonIgnore @Getter
    private Integer cityId;
    @JsonIgnore @Getter
    private Integer metroId;

    /**
     * Class constructor.
     *
     * @param builder builder
     */
    public Office(Builder builder) {
      this.idMasterSystem = builder.idMasterSystem;
      this.pid = builder.pid;
      this.mnemonic = builder.mnemonic;
      this.cityId = builder.cityId;
      this.metroId = builder.metroId;
      this.pathUrl = builder.pathUrl;
      this.title = builder.title;
      this.description = builder.description;
      this.close = builder.close;
      this.closeFromDate = builder.closeFromDate;
      this.closeToDate = builder.closeToDate;
      this.closeReason = builder.closeReason;
      this.regNumberCb = builder.regNumberCb;
      this.fullNameCB = builder.fullNameCB;
      this.shortNameCB = builder.shortNameCB;
      this.regDateCB = builder.regDateCB;
      this.openDate = builder.openDate;
      this.closeDate = builder.closeDate;
      this.closeDateCB = builder.closeDateCB;
      this.openDateActual = builder.openDateActual;
      this.phoneCB = builder.phoneCB;
      this.profitRus = builder.profitRus;
      this.profitEng = builder.profitEng;
      this.code5 = builder.code5;
      this.linkBalance = builder.linkBalance;
      this.linkHeadOffice = builder.linkHeadOffice;
      this.vspCount = builder.vspCount;
      this.visibleSite = builder.visibleSite;
      this.statusCB = builder.statusCB;
      this.kinds = builder.kinds;
      this.addressOfficial = builder.addressOfficial;
      this.addressSms = builder.addressSms;
      this.locations = builder.locations;
      this.services = builder.services;
      this.listOfOperations = builder.listOfOperations;
      this.metaInfo = builder.metaInfo;
      this.branchID = builder.branchID;
      this.useInCosmo = builder.useInCosmo;
      this.isOnReconstruction = builder.isOnReconstruction;
      this.parking = builder.parking;
    }

    /**
     * Class constructor.
     *
     * @param idMasterSystem   idMasterSystem
     * @param pid              pid
     * @param mnemonic         mnemonic
     * @param pathUrl          pathUrl
     * @param title            title
     * @param description      description
     * @param close            close
     * @param closeFromDate    closeFromDate
     * @param closeToDate      closeToDate
     * @param closeReason      closeReason
     * @param regNumberCb      regNumberCb
     * @param fullNameCB       fullNameCB
     * @param shortNameCB      shortNameCB
     * @param regDateCB        regDateCB
     * @param openDate         openDate
     * @param closeDate        closeDate
     * @param closeDateCB      closeDateCB
     * @param openDateActual   openDateActual
     * @param phoneCB          phoneCB
     * @param profitRus        profitRus
     * @param profitEng        profitEng
     * @param code5            code5
     * @param linkBalance      linkBalance
     * @param linkHeadOffice   linkHeadOffice
     * @param vspCount         vspCount
     * @param visibleSite      visibleSite
     * @param statusCB         statusCB
     * @param kinds            kinds
     * @param addressOfficial  addressOfficial
     * @param addressSms       addressSms
     * @param locations        locations
     * @param services         services
     * @param listOfOperations listOfOperations
     * @param metaInfo         metaInfo
     */
    @JsonCreator
    private Office(@JsonProperty("idMasterSystem") final Integer idMasterSystem,
                   @JsonProperty("pid") final String pid,
                   @JsonProperty("mnemonic") final String mnemonic,
                   @JsonProperty("pathUrl") final String pathUrl,
                   @JsonProperty("title") final String title,
                   @JsonProperty("description") final String description,
                   @JsonProperty("close") final Boolean close,
                   @JsonProperty("closeFromDate") final String closeFromDate,
                   @JsonProperty("closeToDate") final String closeToDate,
                   @JsonProperty("closeReason") final String closeReason,
                   @JsonProperty("regnumberСB") final String regNumberCb,
                   @JsonProperty("fullNameCB") final String fullNameCB,
                   @JsonProperty("shortNameCB") final String shortNameCB,
                   @JsonProperty("regDateCB") final String regDateCB,
                   @JsonProperty("openDate") final String openDate,
                   @JsonProperty("closeDate") final String closeDate,
                   @JsonProperty("closeDateCB") final String closeDateCB,
                   @JsonProperty("openDateActual") final String openDateActual,
                   @JsonProperty("phoneCB") final String phoneCB,
                   @JsonProperty("profitrus") final List<String> profitRus,
                   @JsonProperty("profiteng") final List<String> profitEng,
                   @JsonProperty("code5") final String code5,
                   @JsonProperty("linkBalance") final String linkBalance,
                   @JsonProperty("linkHeadoffice") final String linkHeadOffice,
                   @JsonProperty("vspCount") final Integer vspCount,
                   @JsonProperty("visibleSite") final Boolean visibleSite,
                   @JsonProperty("statusCB") final CbCodeName statusCB,
                   @JsonProperty("kinds") final List<Kind> kinds,
                   @JsonProperty("addressOfficial") final String addressOfficial,
                   @JsonProperty("addressSms") final String addressSms,
                   @JsonProperty("locations") final List<Location> locations,
                   @JsonProperty("services") final List<Service> services,
                   @JsonProperty("listOfOperations") final List<Operation> listOfOperations,
                   @JsonProperty("metaInfo") final MetaInfo metaInfo,
                   @JsonProperty("messages") final List<String> messages) {
      this.idMasterSystem = idMasterSystem;
      this.pid = pid;
      this.mnemonic = mnemonic;
      this.pathUrl = pathUrl;
      this.title = title;
      this.description = description;
      this.close = close;
      this.closeFromDate = closeFromDate;
      this.closeToDate = closeToDate;
      this.closeReason = closeReason;
      this.regNumberCb = regNumberCb;
      this.fullNameCB = fullNameCB;
      this.shortNameCB = shortNameCB;
      this.regDateCB = regDateCB;
      this.openDate = openDate;
      this.closeDate = closeDate;
      this.closeDateCB = closeDateCB;
      this.openDateActual = openDateActual;
      this.phoneCB = phoneCB;
      this.profitRus = profitRus;
      this.profitEng = profitEng;
      this.code5 = code5;
      this.linkBalance = linkBalance;
      this.linkHeadOffice = linkHeadOffice;
      this.vspCount = vspCount;
      this.visibleSite = visibleSite;
      this.statusCB = statusCB;
      this.kinds = kinds;
      this.addressOfficial = addressOfficial;
      this.addressSms = addressSms;
      this.locations = locations;
      this.services = services;
      this.listOfOperations = listOfOperations;
      this.metaInfo = metaInfo;
      this.messages = messages;
    }

    /**
     * Compare offices.
     *
     * @param expectedOffice expected
     */
    public void equals(final Office expectedOffice) {
      logComparingObjects(LOGGER, this, expectedOffice);
      final var softly = new SoftAssertions();
      softly.assertThat(this.getPid())
          .as("Проверка 'Pid'")
          .isEqualTo(expectedOffice.getPid());
      softly.assertThat(this.getMnemonic())
          .as("Проверка 'Mnemonic'")
          .isEqualTo(expectedOffice.getMnemonic());
      softly.assertThat(this.getPathUrl())
          .as("Проверка 'PathUrl'")
          .isEqualTo(trimNullable(expectedOffice.getPathUrl()));
      softly.assertThat(this.getTitle())
          .as("Проверка 'Title'")
          .isEqualTo(trimNullable(expectedOffice.getTitle()));
      softly.assertThat(this.getDescription())
          .as("Проверка 'Description'")
          .isEqualTo(trimNullable(expectedOffice.getDescription()));
      softly.assertThat(this.getClose())
          .as("Проверка 'Close'")
          .isEqualTo(expectedOffice.getClose());
      softly.assertThat(this.getShortNameCB())
          .as("Проверка 'ShortNameCB'")
          .isEqualTo(trimNullable(expectedOffice.getShortNameCB()));
      softly.assertThat(this.getOpenDate())
          .as("Проверка 'OpenDate'")
          .isEqualTo(expectedOffice.getOpenDate());
      softly.assertThat(this.getPhoneCB())
          .as("Проверка 'PhoneCB'")
          .isEqualTo(trimNullable(expectedOffice.getPhoneCB()));
      softly.assertThat(this.getStatusCB())
          .as("Проверка 'StatusCB'")
          .isEqualTo(expectedOffice.getStatusCB());
      softly.assertThat(this.getBranchID())
          .as("Проверка 'branchID'")
          .isEqualTo(0);
      softly.assertThat(this.getUseInCosmo())
          .as("Проверка 'useInCosmo'")
          .isEqualTo(0);
      softly.assertThat(this.getIsOnReconstruction())
          .as("Проверка 'isOnReconstruction'")
          .isEqualTo(0);
      softly.assertThat(this.getParking())
          .as("Проверка 'parking'")
          .isEqualTo(0);
      softly.assertAll();
      logComparingResult(LOGGER, String.format("pid=%s' & 'mnemonic=%s",
          this.getPid(), this.getMnemonic()));
    }

    public static class Builder {

      private Integer idMasterSystem;
      private String pid;
      private String mnemonic;
      private String pathUrl;
      private String title;
      private String description;
      private Boolean close;
      private String closeFromDate;
      private String closeToDate;
      private String closeReason;
      private String regNumberCb;
      private String fullNameCB;
      private String shortNameCB;
      private String regDateCB;
      private String openDate;
      private String closeDate;
      private String closeDateCB;
      private String openDateActual;
      private String phoneCB;
      private List<String> profitRus;
      private List<String> profitEng;
      private String code5;
      private String linkBalance;
      private String linkHeadOffice;
      private Integer vspCount;
      private Boolean visibleSite;
      private CbCodeName statusCB;
      private List<Kind> kinds;
      private String addressOfficial;
      private String addressSms;
      private List<Location> locations;
      private List<Service> services;
      private List<Operation> listOfOperations;
      private MetaInfo metaInfo;
      private Integer cityId;
      private Integer metroId;
      private Integer branchID;
      private Integer useInCosmo;
      private Integer isOnReconstruction;
      private Integer parking;

      public Builder setIdMasterSystem(Integer idMasterSystem) {
        this.idMasterSystem = idMasterSystem;
        return this;
      }

      public Builder setPid(String pid) {
        this.pid = pid;
        return this;
      }

      public Builder setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
        return this;
      }

      public Builder setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
        return this;
      }

      public Builder setTitle(String title) {
        this.title = title;
        return this;
      }

      public Builder setDescription(String description) {
        this.description = description;
        return this;
      }

      public Builder setClose(Boolean close) {
        this.close = close;
        return this;
      }

      public Builder setCloseFromDate(String closeFromDate) {
        this.closeFromDate = closeFromDate;
        return this;
      }

      public Builder setCloseToDate(String closeToDate) {
        this.closeToDate = closeToDate;
        return this;
      }

      public Builder setCloseReason(String closeReason) {
        this.closeReason = closeReason;
        return this;
      }

      public Builder setRegNumberCb(String regNumberCb) {
        this.regNumberCb = regNumberCb;
        return this;
      }

      public Builder setFullNameCB(String fullNameCB) {
        this.fullNameCB = fullNameCB;
        return this;
      }

      public Builder setShortNameCB(String shortNameCB) {
        this.shortNameCB = shortNameCB;
        return this;
      }

      public Builder setRegDateCB(String regDateCB) {
        this.regDateCB = regDateCB;
        return this;
      }

      public Builder setOpenDate(String openDate) {
        this.openDate = openDate;
        return this;
      }

      public Builder setCloseDate(String closeDate) {
        this.closeDate = closeDate;
        return this;
      }

      public Builder setCloseDateCB(String closeDateCB) {
        this.closeDateCB = closeDateCB;
        return this;
      }

      public Builder setOpenDateActual(String openDateActual) {
        this.openDateActual = openDateActual;
        return this;
      }

      public Builder setPhoneCB(String phoneCB) {
        this.phoneCB = phoneCB;
        return this;
      }

      public Builder setProfitRus(List<String> profitRus) {
        this.profitRus = profitRus;
        return this;
      }

      public Builder setProfitEng(List<String> profitEng) {
        this.profitEng = profitEng;
        return this;
      }

      public Builder setCode5(String code5) {
        this.code5 = code5;
        return this;
      }

      public Builder setLinkBalance(String linkBalance) {
        this.linkBalance = linkBalance;
        return this;
      }

      public Builder setLinkHeadOffice(String linkHeadOffice) {
        this.linkHeadOffice = linkHeadOffice;
        return this;
      }

      public Builder setVspCount(Integer vspCount) {
        this.vspCount = vspCount;
        return this;
      }

      public Builder setVisibleSite(Boolean visibleSite) {
        this.visibleSite = visibleSite;
        return this;
      }

      public Builder setStatusCB(CbCodeName statusCB) {
        this.statusCB = statusCB;
        return this;
      }

      public Builder setKinds(List<Kind> kinds) {
        this.kinds = kinds;
        return this;
      }

      public Builder setAddressOfficial(String addressOfficial) {
        this.addressOfficial = addressOfficial;
        return this;
      }

      public Builder setAddressSms(String addressSms) {
        this.addressSms = addressSms;
        return this;
      }

      public Builder setLocations(List<Location> locations) {
        this.locations = locations;
        return this;
      }

      public Builder setServices(List<Service> services) {
        this.services = services;
        return this;
      }

      public Builder setListOfOperations(List<Operation> listOfOperations) {
        this.listOfOperations = listOfOperations;
        return this;
      }

      public Builder setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
        return this;
      }

      public Builder setCityId(Integer cityId) {
        this.cityId = cityId;
        return this;
      }

      public Builder setMetroId(Integer metroId) {
        this.metroId = metroId;
        return this;
      }

      public Builder setBranchID(Integer branchID) {
        this.branchID = branchID;
        return this;
      }

      public Builder setUseInCosmo(Integer useInCosmo) {
        this.useInCosmo = useInCosmo;
        return this;
      }

      public Builder setIsOnReconstruction(Integer isOnReconstruction) {
        this.isOnReconstruction = isOnReconstruction;
        return this;
      }

      public Builder setParking(Integer parking) {
        this.parking = parking;
        return this;
      }

      /**
       * Reusing existed office.
       *
       * @param office office
       * @return this
       */
      public Builder using(Office office) {
        this.idMasterSystem = office.idMasterSystem;
        this.pid = office.pid;
        this.mnemonic = office.mnemonic;
        this.pathUrl = office.pathUrl;
        this.title = office.title;
        this.description = office.description;
        this.close = office.close;
        this.closeFromDate = office.closeFromDate;
        this.closeToDate = office.closeToDate;
        this.closeReason = office.closeReason;
        this.regNumberCb = office.regNumberCb;
        this.fullNameCB = office.fullNameCB;
        this.shortNameCB = office.shortNameCB;
        this.regDateCB = office.regDateCB;
        this.openDate = office.openDate;
        this.closeDate = office.closeDate;
        this.closeDateCB = office.closeDateCB;
        this.openDateActual = office.openDateActual;
        this.phoneCB = office.phoneCB;
        this.profitRus = office.profitRus;
        this.profitEng = office.profitEng;
        this.code5 = office.code5;
        this.linkBalance = office.linkBalance;
        this.linkHeadOffice = office.linkHeadOffice;
        this.vspCount = office.vspCount;
        this.visibleSite = office.visibleSite;
        this.statusCB = office.statusCB;
        this.kinds = office.kinds;
        this.addressOfficial = office.addressOfficial;
        this.addressSms = office.addressSms;
        this.locations = office.locations;
        this.services = office.services;
        this.listOfOperations = office.listOfOperations;
        this.metaInfo = office.metaInfo;
        this.cityId = office.cityId;
        this.metroId = office.metroId;
        this.branchID = office.branchID;
        this.useInCosmo = office.useInCosmo;
        this.isOnReconstruction = office.isOnReconstruction;
        this.parking = office.parking;
        return this;
      }

      public Office build() {
        return new Office(this);
      }
    }

    public static class Location extends AbstractBusinessObject {

      private static final Logger LOGGER = LogManager.getLogger(LocalDateTime.class);

      @Getter private final String fiasId;
      @Getter private final String kladrId;
      @Getter private final Double lat;
      @Getter private final Double lon;
      @Getter private final String postcode;
      @Getter private final String federalDistrict;
      @Getter private final String subjectOfFederation;
      @Getter private final String city;
      @Getter private final String street;
      @Getter private final String house;
      @Getter private final String block;
      @Getter private final String building;
      @Getter private final String liter;
      @Getter private final String room;
      @Getter private final String placeComment;
      @JsonIgnore
      @Getter private final String address;

      /**
       * Class constructor.
       *
       * @param fiasId              fiasId
       * @param kladrId             kladrId
       * @param lat                 lat
       * @param lon                 lon
       * @param postcode            postcode
       * @param federalDistrict     federalDistrict
       * @param subjectOfFederation subjectOfFederation
       * @param city                city
       * @param street              street
       * @param house               house
       * @param block               block
       * @param building            building
       * @param liter               liter
       * @param room                room
       * @param placeComment        placeComment
       */
      @JsonCreator
      public Location(@JsonProperty("fiasId") final String fiasId,
                      @JsonProperty("kladrId") final String kladrId,
                      @JsonProperty("lat") final Double lat,
                      @JsonProperty("lon") final Double lon,
                      @JsonProperty("postcode") final String postcode,
                      @JsonProperty("federalDistrict") final String federalDistrict,
                      @JsonProperty("subjectOfFederation") final String subjectOfFederation,
                      @JsonProperty("city") final String city,
                      @JsonProperty("street") final String street,
                      @JsonProperty("house") final String house,
                      @JsonProperty("block") final String block,
                      @JsonProperty("building") final String building,
                      @JsonProperty("liter") final String liter,
                      @JsonProperty("room") final String room,
                      @JsonProperty("placeComment") final String placeComment,
                      @JsonProperty("address") final String address) {
        this.fiasId = fiasId;
        this.kladrId = kladrId;
        this.lat = lat;
        this.lon = lon;
        this.postcode = postcode;
        this.federalDistrict = federalDistrict;
        this.subjectOfFederation = subjectOfFederation;
        this.city = city;
        this.street = street;
        this.house = house;
        this.block = block;
        this.building = building;
        this.liter = liter;
        this.room = room;
        this.placeComment = placeComment;
        this.address = address;
      }

      /**
       * Class constructor.
       *
       * @param builder builder
       */
      public Location(Builder builder) {
        this.fiasId = builder.fiasId;
        this.kladrId = builder.kladrId;
        this.lat = builder.lat;
        this.lon = builder.lon;
        this.postcode = builder.postcode;
        this.federalDistrict = builder.federalDistrict;
        this.subjectOfFederation = builder.subjectOfFederation;
        this.city = builder.city;
        this.street = builder.street;
        this.house = builder.house;
        this.block = builder.block;
        this.building = builder.building;
        this.liter = builder.liter;
        this.room = builder.room;
        this.placeComment = builder.placeComment;
        this.address = builder.address;
      }

      /**
       * Compare locations.
       *
       * @param expectedLocation expectedLocation
       * @param softly           soft assertions
       */
      public void equals(final Location expectedLocation, SoftAssertions softly) {
        LOGGER.info(String.format("Проверяю маппинг широты: actual '%s' и expected '%s'",
            this.getLat(), expectedLocation.getLat()));
        softly.assertThat(this.getLat()).as("Проверка широты")
            .isEqualTo(expectedLocation.getLat());
        LOGGER.info(String.format("Проверяю маппинг долготы: actual '%s' и expected '%s'",
            this.getLon(), expectedLocation.getLon()));
        softly.assertThat(this.getLon()).as("Проверка долготы")
            .isEqualTo(expectedLocation.getLon());
        LOGGER.info(String.format("Проверяю маппинг почтового индекса: actual '%s' и expected '%s'",
            this.getPostcode(), expectedLocation.getPostcode()));
        softly.assertThat(this.getPostcode()).as("Проверка почтового индекса")
            .isEqualTo(expectedLocation.getPostcode());
        LOGGER.info(String.format("Проверяю маппинг региона: actual '%s' и expected '%s'",
            this.getSubjectOfFederation(), expectedLocation.getSubjectOfFederation()));
        softly.assertThat(this.getSubjectOfFederation()).as("Проверка региона")
            .isEqualTo(expectedLocation.getSubjectOfFederation());
        LOGGER.info(String.format("Проверяю маппинг адреса: actual '%s' и expected '%s'",
            this.getAddress(), expectedLocation.getAddress()));
        softly.assertThat(this.getAddress()).as("Проверка адреса")
            .isEqualTo(expectedLocation.getAddress());
        LOGGER.info(String.format("Проверяю маппинг комментария: actual '%s' и expected '%s'",
            this.getPlaceComment(), expectedLocation.getPlaceComment()));
        softly.assertThat(this.getPlaceComment()).as("Проверка комментария")
            .isEqualTo(expectedLocation.getPlaceComment() != null
                ? expectedLocation.getPlaceComment() : "");
      }

      public static class Builder {

        private String fiasId;
        private String kladrId;
        private Double lat;
        private Double lon;
        private String postcode;
        private String federalDistrict;
        private String subjectOfFederation;
        private String city;
        private String street;
        private String house;
        private String block;
        private String building;
        private String liter;
        private String room;
        private String placeComment;
        public String address;

        public Builder setFiasId(String fiasId) {
          this.fiasId = fiasId;
          return this;
        }

        public Builder setKladrId(String kladrId) {
          this.kladrId = kladrId;
          return this;
        }

        public Builder setLat(Double lat) {
          this.lat = lat;
          return this;
        }

        public Builder setLon(Double lon) {
          this.lon = lon;
          return this;
        }

        public Builder setPostcode(String postcode) {
          this.postcode = postcode;
          return this;
        }

        public Builder setFederalDistrict(String federalDistrict) {
          this.federalDistrict = federalDistrict;
          return this;
        }

        public Builder setSubjectOfFederation(String subjectOfFederation) {
          this.subjectOfFederation = subjectOfFederation;
          return this;
        }

        public Builder setCity(String city) {
          this.city = city;
          return this;
        }

        public Builder setStreet(String street) {
          this.street = street;
          return this;
        }

        public Builder setHouse(String house) {
          this.house = house;
          return this;
        }

        public Builder setBlock(String block) {
          this.block = block;
          return this;
        }

        public Builder setBuilding(String building) {
          this.building = building;
          return this;
        }

        public Builder setLiter(String liter) {
          this.liter = liter;
          return this;
        }

        public Builder setRoom(String room) {
          this.room = room;
          return this;
        }

        public Builder setPlaceComment(String placeComment) {
          this.placeComment = placeComment;
          return this;
        }

        public Builder setAddress(String address) {
          this.address = address;
          return this;
        }

        public Location build() {
          return new Location(this);
        }

        /**
         * Reusing location.
         *
         * @param location location
         * @return builder
         */
        public Location.Builder using(Location location) {
          this.fiasId = location.fiasId;
          this.kladrId = location.kladrId;
          this.lat = location.lat;
          this.lon = location.lon;
          this.postcode = location.postcode;
          this.federalDistrict = location.federalDistrict;
          this.subjectOfFederation = location.subjectOfFederation;
          this.city = location.city;
          this.street = location.street;
          this.house = location.house;
          this.block = location.block;
          this.building = location.building;
          this.liter = location.liter;
          this.room = location.room;
          this.placeComment = location.placeComment;
          this.address = location.address;
          return this;
        }
      }
    }

    public static class Service extends AbstractBusinessObject {

      @Getter private String code;
      @Getter private String name;

      @JsonCreator
      public Service(@JsonProperty("code") final String code,
                     @JsonProperty("name") final String name) {
        this.code = code;
        this.name = name;
      }

      public Service() {

      }
    }

    public static class Operation extends AbstractBusinessObject {

      @Getter private final String code;
      @Getter private final String name;
      @Getter private final String codeCB;
      @Getter private final String categoryCB;

      /**
       * Class constructor.
       *
       * @param code       code
       * @param name       name
       * @param codeCB     codeCB
       * @param categoryCB categoryCB
       */
      @JsonCreator
      public Operation(@JsonProperty("code") final String code,
                       @JsonProperty("name") final String name,
                       @JsonProperty("codeCB") final String codeCB,
                       @JsonProperty("categoryCB") final String categoryCB) {
        this.code = code;
        this.name = name;
        this.codeCB = codeCB;
        this.categoryCB = categoryCB;
      }
    }

    public static class MetaInfo extends AbstractBusinessObject {

      @Getter private String operation;
      @Getter private String changeDatetime;

      @JsonCreator
      public MetaInfo(@JsonProperty("operation") final String operation,
                      @JsonProperty("changeDatetime") final String changeDatetime) {
        this.operation = operation;
        this.changeDatetime = changeDatetime;
      }

      @JsonCreator
      public MetaInfo() {

      }
    }
  }
}
