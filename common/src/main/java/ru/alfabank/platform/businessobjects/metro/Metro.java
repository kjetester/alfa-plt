package ru.alfabank.platform.businessobjects.metro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class Metro extends AbstractBusinessObject {

  private String cityFiasId;
  private String name;
  private String cityName;
  private String id;
  private String lineColor;
  private String lineName;
  private Integer cityId;
  private Integer distance;
  private Integer lat;
  private Integer lineId;
  private Integer lon;

  @JsonCreator
  private Metro(@JsonProperty("cityFiasId") final String cityFiasId,
                @JsonProperty("name") final String name,
                @JsonProperty("cityName") final String cityName,
                @JsonProperty("id") final String id,
                @JsonProperty("lineColor") final String lineColor,
                @JsonProperty("lineName") final String lineName,
                @JsonProperty("cityId") final Integer cityId,
                @JsonProperty("distance") final Integer distance,
                @JsonProperty("lat") final Integer lat,
                @JsonProperty("lineId") final Integer lineId,
                @JsonProperty("lon") final Integer lon) {
    this.cityFiasId = cityFiasId;
    this.name = name;
    this.cityName = cityName;
    this.id = id;
    this.lineColor = lineColor;
    this.lineName = lineName;
    this.cityId = cityId;
    this.distance = distance;
    this.lat = lat;
    this.lineId = lineId;
    this.lon = lon;
  }

  /**
   * Class constructor.
   *
   * @param builder builder
   */
  public Metro(final Builder builder) {
    this.cityFiasId = builder.cityFiasId;
    this.name = builder.name;
    this.cityName = builder.cityName;
    this.id = builder.id;
    this.lineColor = builder.lineColor;
    this.lineName = builder.lineName;
    this.cityId = builder.cityId;
    this.distance = builder.distance;
    this.lat = builder.lat;
    this.lineId = builder.lineId;
    this.lon = builder.lon;
  }

  public String getCityFiasId() {
    return cityFiasId;
  }

  public String getName() {
    return name;
  }

  public String getCityName() {
    return cityName;
  }

  public String getId() {
    return id;
  }

  public String getLineColor() {
    return lineColor;
  }

  public String getLineName() {
    return lineName;
  }

  public Integer getCityId() {
    return cityId;
  }

  public Integer getDistance() {
    return distance;
  }

  public Integer getLat() {
    return lat;
  }

  public Integer getLineId() {
    return lineId;
  }

  public Integer getLon() {
    return lon;
  }

  public static class Builder {
    private String cityFiasId;
    private String name;
    private String cityName;
    private String id;
    private String lineColor;
    private String lineName;
    private Integer cityId;
    private Integer distance;
    private Integer lat;
    private Integer lineId;
    private Integer lon;

    public Builder setCityFiasId(String cityFiasId) {
      this.cityFiasId = cityFiasId;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setCityName(String cityName) {
      this.cityName = cityName;
      return this;
    }

    public Builder setId(String id) {
      this.id = id;
      return this;
    }

    public Builder setLineColor(String lineColor) {
      this.lineColor = lineColor;
      return this;
    }

    public Builder setLineName(String lineName) {
      this.lineName = lineName;
      return this;
    }

    public Builder setCityId(Integer cityId) {
      this.cityId = cityId;
      return this;
    }

    public Builder setDistance(Integer distance) {
      this.distance = distance;
      return this;
    }

    public Builder setLat(Integer lat) {
      this.lat = lat;
      return this;
    }

    public Builder setLineId(Integer lineId) {
      this.lineId = lineId;
      return this;
    }

    public Builder setLon(Integer lon) {
      this.lon = lon;
      return this;
    }

    public Metro build() {
      return new Metro(this);
    }
  }
}
