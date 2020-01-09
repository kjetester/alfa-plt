package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Widget {

  private String uid;
  private String name;
  private int orderNum;
  private Object dateFrom;
  private Object dateTo;
  private Device device;
  private boolean isEnabled;
  private String localization;
  private List<String> widgetGeo;
  private Children[] children;
  private List<Property> properties;
  private boolean reused;

  /**
   * Class constructor.
   * @param uid uid
   * @param name name
   * @param orderNumber orderNumber
   * @param dateFrom dateFrom
   * @param dateTo dateTo
   * @param device device
   * @param isEnabled isEnabled
   * @param localization localization
   * @param widgetGeo widgetGeo
   * @param children children
   * @param properties properties
   * @param reused reused
   */
  @JsonCreator
  public Widget(@JsonProperty("uid") String uid,
                @JsonProperty("name") String name,
                @JsonProperty("orderNumber") int orderNumber,
                @JsonProperty("dateFrom") Object dateFrom,
                @JsonProperty("dateTo") Object dateTo,
                @JsonProperty("device") Device device,
                @JsonProperty("enable") boolean isEnabled,
                @JsonProperty("localization") String localization,
                @JsonProperty("widgetGeo") List<String> widgetGeo,
                @JsonProperty("children") Children[] children,
                @JsonProperty("properties") List<Property> properties,
                @JsonProperty("reused") boolean reused) {
    this.uid = uid;
    this.name = name;
    this.orderNum = orderNumber;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.device = device;
    this.isEnabled = isEnabled;
    this.localization = localization;
    this.widgetGeo = widgetGeo;
    this.children = children;
    this.properties = properties;
    this.reused = reused;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public Object getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Object dateFrom) {
    this.dateFrom = dateFrom;
  }

  public Object getDateTo() {
    return dateTo;
  }

  public void setDateTo(Object dateTo) {
    this.dateTo = dateTo;
  }

  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }

  public String getLocalization() {
    return localization;
  }

  public void setLocalization(String localization) {
    this.localization = localization;
  }

  public List<String> getWidgetGeo() {
    return widgetGeo;
  }

  public void setWidgetGeo(List<String> widgetGeo) {
    this.widgetGeo = widgetGeo;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(List<Property> properties) {
    this.properties = properties;
  }

  public boolean isReused() {
    return reused;
  }

  public void setReused(boolean reused) {
    this.reused = reused;
  }

  public Children[] getChildren() {
    return children;
  }

  public void setChildren(Children[] children) {
    this.children = children;
  }

  @JsonIgnoreProperties(
      {
          "name",
          "dateFrom",
          "dateTo",
          "device",
          "enable",
          "localization",
          "widgetGeo",
          "children",
          "properties",
          "reused"
      })
  public static class Children {

    private String uid;
    private int orderNumber;

    @JsonCreator
    public Children(@JsonProperty("uid") String uid,
                    @JsonProperty("orderNumber") int orderNumber) {
      this.uid = uid;
      this.orderNumber = orderNumber;
    }

    public String getUid() {
      return uid;
    }

    public void setUid(String uid) {
      this.uid = uid;
    }

    public int getOrderNumber() {
      return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
      this.orderNumber = orderNumber;
    }
  }
}
