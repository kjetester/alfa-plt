package ru.alfabank.platform.businessobjects.geofacade;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;
import ru.alfabank.platform.businessobjects.cities.Cities.City;

public class GeoGroupWithCities extends AbstractBusinessObject implements Groupable {

  private final Integer id;
  private final String code;
  private final String name;
  private final String description;
  private final String changedDate;
  private final String type;
  private final List<City> cities;

  @JsonCreator
  private GeoGroupWithCities(Builder builder) {
    this.id = builder.id;
    this.code = builder.code;
    this.name = builder.name;
    this.description = builder.description;
    this.changedDate = builder.changedDate;
    this.type = builder.type;
    this.cities = builder.cities;
  }

  public Integer getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getChangedDate() {
    return changedDate;
  }

  public String getType() {
    return type;
  }

  public List<City> getCities() {
    return cities;
  }

  /**
   * Compare geo groups.
   *
   * @param expectedGeoGroup expected Geo Group
   */
  public void equals(final GeoGroupWithCities expectedGeoGroup) {
    final var softly = new SoftAssertions();
    softly.assertThat(this.getId()).isEqualTo(expectedGeoGroup.getId());
    softly.assertThat(this.getCode()).isEqualTo(expectedGeoGroup.getCode());
    softly.assertThat(this.getName()).isEqualTo(expectedGeoGroup.getName());
    softly.assertThat(this.getDescription()).isEqualTo(expectedGeoGroup.getDescription());
    softly.assertThat(this.getChangedDate()).isEqualTo(expectedGeoGroup.getChangedDate());
    softly.assertThat(this.getType()).isEqualTo(expectedGeoGroup.getType());
    softly.assertThat(this.getCities()).isEqualTo(expectedGeoGroup.getCities());
    softly.assertAll();
  }

  public static class Builder {

    private Integer id;
    private String code;
    private String name;
    private String description;
    private String changedDate;
    private String type;
    private List<City> cities;

    public Builder setId(Integer id) {
      this.id = id;
      return this;
    }

    public Builder setCode(String code) {
      this.code = code;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setChangedDate(String changedDate) {
      this.changedDate = changedDate;
      return this;
    }

    public Builder setType(String type) {
      this.type = type;
      return this;
    }

    public Builder setCities(List<City> cityList) {
      this.cities = cityList;
      return this;
    }

    /**
     * Reusing given Geo-group.
     *
     * @param geoGroup Geo-group
     * @return builder
     */
    public Builder using(Groupable geoGroup) {
      this.id = geoGroup.id;
      this.code = geoGroup.code;
      this.name = geoGroup.name;
      this.description = geoGroup.description;
      this.changedDate = geoGroup.changedDate;
      this.type = geoGroup.type;
      return this;
    }

    public GeoGroupWithCities build() {
      return new GeoGroupWithCities(this);
    }
  }
}
