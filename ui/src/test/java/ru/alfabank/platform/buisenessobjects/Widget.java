package ru.alfabank.platform.buisenessobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public class Widget {

  private String uid;
  private String name;
  private int orderNumber;
  private String dateFrom;
  private String dateTo;
  private String device;
  private boolean isEnable;
  private String localization;
  private String[] widgetGeo;
  private Widget[] children;
  private Property[] properties;
  private boolean isReused;

  /**
   * Class constructor.
   * @param uid uid
   * @param name name
   * @param orderNumber orderNumber
   * @param dateFrom dateFrom
   * @param dateTo dateTo
   * @param device device
   * @param isEnable isEnable
   * @param localization localization
   * @param widgetGeo widgetGeo
   * @param children children
   * @param properties properties
   * @param isReused isReused
   */
  @JsonCreator
  public Widget(
      @JsonProperty("uid") String uid,
      @JsonProperty("name") String name,
      @JsonProperty("orderNumber") int orderNumber,
      @JsonProperty("dateFrom") String dateFrom,
      @JsonProperty("dateTo") String dateTo,
      @JsonProperty("device") String device,
      @JsonProperty("enable") boolean isEnable,
      @JsonProperty("localization") String localization,
      @JsonProperty("widgetGeo") String[] widgetGeo,
      @JsonProperty("children") Widget[] children,
      @JsonProperty("properties") Property[] properties,
      @JsonProperty("reused") boolean isReused) {
    this.uid = uid;
    this.name = name;
    this.orderNumber = orderNumber;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.device = device;
    this.isEnable = isEnable;
    this.localization = localization;
    this.widgetGeo = widgetGeo;
    this.children = children;
    this.properties = properties;
    this.isReused = isReused;
  }

  public String getUid() {
    return uid;
  }

  public String getName() {
    return name;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public String getDateFrom() {
    return dateFrom;
  }

  public String getDateTo() {
    return dateTo;
  }

  public String getDevice() {
    return device;
  }

  public boolean isEnable() {
    return isEnable;
  }

  public String getLocalization() {
    return localization;
  }

  public String[] getWidgetGeo() {
    return widgetGeo;
  }

  public Widget[] getChildren() {
    return children;
  }

  public Property[] getProperties() {
    return properties;
  }

  public boolean isReused() {
    return isReused;
  }

  public static class Property {

    private String uid;
    private String name;
    private String device;
    private Value[] values;

    /**
     * Class constructor.
     * @param uid uid
     * @param name name
     * @param device device
     * @param values values
     */
    @JsonCreator
    public Property(
        @JsonProperty("uid") String uid,
        @JsonProperty("name") String name,
        @JsonProperty("device") String device,
        @JsonProperty("values") Value[] values) {
      this.uid = uid;
      this.name = name;
      this.device = device;
      this.values = values;
    }

    public String getUid() {
      return uid;
    }

    public String getName() {
      return name;
    }

    public String getDevice() {
      return device;
    }

    public Value[] getValues() {
      return values;
    }

    public static class Value {

      private String uid;
      @JsonRawValue
      private JsonNode value;
      private String[] geo;

      /**
       * Class constructor.
       * @param uid uid
       * @param value value
       * @param geo geo
       */
      @JsonCreator
      public Value(
          @JsonProperty("uid") String uid,
          @JsonProperty("value") JsonNode value,
          @JsonProperty("geo") String[] geo) {
        this.uid = uid;
        this.value = value;
        this.geo = geo;
      }

      public String getUid() {
        return uid;
      }

      public JsonNode getValue() {
        return value;
      }

      public String[] getGeo() {
        return geo;
      }
    }
  }
}
