package ru.alfabank.platform.businessobjects.transitions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public final class Body extends AbstractBusinessObject {

  private final Object businessUid;
  private final Object clientDate;
  private final Object recipient;
  private final Object referer;
  private final String status;
  private final String pageUri;
  private final String userAgent;
  private final String ip;
  private final String serverTime;
  private final Object feedBackData;
  private final Object clientData;
  private final Object data;

  /**
   * Class constructor.
   *
   * @param builder builder
   */
  @JsonCreator
  public Body(final BodyBuilder builder) {
    this.businessUid = builder.businessUid;
    this.clientDate = builder.clientDate;
    this.recipient = builder.recipient;
    this.referer = builder.referer;
    this.status = builder.status;
    this.pageUri = builder.pageUri;
    this.userAgent = builder.userAgent;
    this.ip = builder.ip;
    this.serverTime = builder.serverTime;
    this.feedBackData = builder.feedBackData;
    this.clientData = builder.clientData;
    this.data = builder.data;
  }

  @JsonIgnoreType
  public static class BodyBuilder {
    private Object businessUid;
    private Object clientDate;
    private Object recipient;
    private Object referer;
    private String status;
    private String pageUri;
    private String userAgent;
    private String ip;
    private String serverTime;
    private Object feedBackData;
    private Object clientData;
    private Object data;

    /**
     * Modifying existing Body.
     *
     * @param body body
     * @return this
     */
    public BodyBuilder using(Body body) {
      this.businessUid = body.businessUid;
      this.clientDate = body.clientDate;
      this.recipient = body.recipient;
      this.referer = body.referer;
      this.status = body.status;
      this.pageUri = body.pageUri;
      this.userAgent = body.userAgent;
      this.ip = body.ip;
      this.serverTime = body.serverTime;
      this.feedBackData = body.feedBackData;
      this.clientData = body.clientData;
      this.data = body.data;
      return this;
    }

    public BodyBuilder setBusinessUid(Object businessUid) {
      this.businessUid = businessUid;
      return this;
    }

    public BodyBuilder setClientDate(Object clientDate) {
      this.clientDate = clientDate;
      return this;
    }

    public BodyBuilder setRecipient(Object recipient) {
      this.recipient = recipient;
      return this;
    }

    public BodyBuilder setStatus(String status) {
      this.status = status;
      return this;
    }

    public BodyBuilder setServerTime(LocalDateTime serverTime) {
      this.serverTime = serverTime.toString();
      return this;
    }

    public BodyBuilder setFeedBackData(Object feedBackData) {
      this.feedBackData = feedBackData;
      return this;
    }

    public BodyBuilder setClientData(Object clientData) {
      this.clientData = clientData;
      return this;
    }

    public BodyBuilder setData(Object data) {
      this.data = data;
      return this;
    }

    public BodyBuilder setReferer(Object referer) {
      this.referer = referer;
      return this;
    }

    public BodyBuilder setUserAgent(String userAgent) {
      this.userAgent = userAgent;
      return this;
    }

    public BodyBuilder setPageUri(String pageUri) {
      this.pageUri = pageUri;
      return this;
    }

    public BodyBuilder setIp(String ip) {
      this.ip = ip;
      return this;
    }

    public Body build() {
      return new Body(this);
    }

    public String getIp() {
      return ip;
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class FeedBackData {

    private final String statusCode;
    private final String message;

    @JsonCreator
    public FeedBackData(final FeedBackDataBuilder builder) {
      this.statusCode = builder.statusCode;
      this.message = builder.message;
    }

    @JsonIgnoreType
    public static class FeedBackDataBuilder {

      private String statusCode;
      private String message;

      public FeedBackDataBuilder setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
      }

      public FeedBackDataBuilder setMessage(String message) {
        this.message = message;
        return this;
      }

      public FeedBackData build() {
        return new FeedBackData(this);
      }
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class EmptyFeedBackData {

    @JsonCreator
    public EmptyFeedBackData() {
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class ClientData {
    private final String platformId;
    private final String bannerId;

    @JsonCreator
    public ClientData(final ClientDataBuilder builder) {
      this.platformId = builder.platformId;
      this.bannerId = builder.bannerId;
    }

    @JsonIgnoreType
    public static class ClientDataBuilder {
      private String platformId;
      private String bannerId;

      public ClientDataBuilder setPlatformId(String platformId) {
        this.platformId = platformId;
        return this;
      }

      public ClientDataBuilder setBannerId(String bannerId) {
        this.bannerId = bannerId;
        return this;
      }

      public ClientData build() {
        return new ClientData(this);
      }
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class EmptyClientData {

    @JsonCreator
    public EmptyClientData() {
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Data {
    private final String product;
    private final String productType;
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final String gender;
    private final String phone;
    private final String email;
    private final String region;
    private final String lendingAmount;
    private final String creditTerm;
    private final String cardID;
    private final String packetID;
    private final String prefilContractId;

    /**
     * Class constructor.
     *
     * @param builder builder
     */
    @JsonCreator
    public Data(final DataBuilder builder) {
      this.product = builder.product;
      this.productType = builder.productType;
      this.lastName = builder.lastName;
      this.firstName = builder.firstName;
      this.middleName = builder.middleName;
      this.gender = builder.gender;
      this.phone = builder.phone;
      this.email = builder.email;
      this.region = builder.region;
      this.lendingAmount = builder.lendingAmount;
      this.creditTerm = builder.creditTerm;
      this.cardID = builder.cardID;
      this.packetID = builder.packetID;
      this.prefilContractId = builder.prefilContractId;
    }

    @JsonIgnoreType
    public static class DataBuilder {
      private String product;
      private String productType;
      private String lastName;
      private String firstName;
      private String middleName;
      private String gender;
      private String phone;
      private String email;
      private String region;
      private String lendingAmount;
      private String creditTerm;
      private String cardID;
      private String packetID;
      private String prefilContractId;

      public DataBuilder setProduct(String product) {
        this.product = product;
        return this;
      }

      public DataBuilder setProductType(String productType) {
        this.productType = productType;
        return this;
      }

      public DataBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
      }

      public DataBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
      }

      public DataBuilder setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
      }

      public DataBuilder setGender(String gender) {
        this.gender = gender;
        return this;
      }

      public DataBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
      }

      public DataBuilder setEmail(String email) {
        this.email = email;
        return this;
      }

      public DataBuilder setRegion(String region) {
        this.region = region;
        return this;
      }

      public DataBuilder setLendingAmount(String lendingAmount) {
        this.lendingAmount = lendingAmount;
        return this;
      }

      public DataBuilder setCreditTerm(String creditTerm) {
        this.creditTerm = creditTerm;
        return this;
      }

      public DataBuilder setCardID(String cardID) {
        this.cardID = cardID;
        return this;
      }

      public DataBuilder setPacketID(String packetID) {
        this.packetID = packetID;
        return this;
      }

      public DataBuilder setPrefilContractId(String prefilContractId) {
        this.prefilContractId = prefilContractId;
        return this;
      }

      public Data build() {
        return new Data(this);
      }
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class EmptyData {

    @JsonCreator
    public EmptyData() {
    }
  }

  @JsonIgnore
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(Body.class);
  }
}
