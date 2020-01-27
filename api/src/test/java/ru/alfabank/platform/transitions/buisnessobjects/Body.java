package ru.alfabank.platform.transitions.buisnessobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Body {

  private String businessUid;
  private String clientDate;
  private String recipient;
  private String status;
  private Object feedBackData;
  private Object clientData;
  private Object data;

  /**
   * Class constructor.
   * @param builder builder
   */
  @JsonCreator
  public Body(final BodyBuilder builder) {
    this.businessUid = builder.businessUid;
    this.clientDate = builder.clientDate;
    this.recipient = builder.recipient;
    this.status = builder.status;
    this.feedBackData = builder.feedBackData;
    this.clientData = builder.clientData;
    this.data = builder.data;
  }

  @JsonIgnoreType
  public static class BodyBuilder {
    private String businessUid;
    private String clientDate;
    private String recipient;
    private String status;
    private Object feedBackData;
    private Object clientData;
    private Object data;

    /**
     * Modifying existing Body.
     * @param body body
     * @return this
     */
    public BodyBuilder using(Body body) {
      this.businessUid = body.businessUid;
      this.clientDate = body.clientDate;
      this.recipient = body.recipient;
      this.status = body.status;
      this.feedBackData = body.feedBackData;
      this.clientData = body.clientData;
      this.data = body.data;
      return this;
    }

    public BodyBuilder setBusinessUid(String businessUid) {
      this.businessUid = businessUid;
      return this;
    }

    public BodyBuilder setClientDate(String clientDate) {
      this.clientDate = clientDate;
      return this;
    }

    public BodyBuilder setRecipient(String recipient) {
      this.recipient = recipient;
      return this;
    }

    public BodyBuilder setStatus(String status) {
      this.status = status;
      return this;
    }

    public BodyBuilder setFeedBackData(FeedBackData feedBackData) {
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

    public Body build() {
      return new Body(this);
    }
  }

  @JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class FeedBackData {

    private String statusCode;
    private String message;

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

  @JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class ClientData {
    private String platformId;
    private String bannerId;

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

  @JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class EmptyClientData {

    @JsonCreator
    public EmptyClientData() {
    }
  }

  @JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Data {
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

    /**
     * Class constructor.
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

  @JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class EmptyData {

    @JsonCreator
    public EmptyData() {
    }
  }
}
