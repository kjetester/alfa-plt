package ru.alfabank.platform.businessobjects.cities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.IntStream;
import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class Cities extends AbstractBusinessObject {

  private final List<City> cities;

  @JsonCreator
  public Cities(@JsonProperty("cities") final List<City> cities) {
    this.cities = cities;
  }

  public List<City> getCities() {
    return cities;
  }

  public void equals(final Cities expected) {
    IntStream.range(0, expected.cities.size()).forEach(i ->
        this.getCities().get(i).equals(expected.getCities().get(i)));
  }

  public static class City extends AbstractBusinessObject implements Comparable<City> {

    private final String fiasId;
    private final String fiasName;
    private final Integer ajsonId;
    private final String cityNameRus;
    private final String path;
    private final String cityNameNorm;
    private final String genitiveName;
    private final Integer regionId;
    private final Double latitude;
    private final Double longitude;
    private final Integer parentCityId;
    private final String cityDepartment;

    /**
     * Class constructor.
     *
     * @param builder builder
     */
    @JsonCreator
    public City(Builder builder) {
      this.fiasId = builder.fiasId;
      this.fiasName = builder.fiasName;
      this.ajsonId = builder.ajsonId;
      this.cityNameRus = builder.cityNameRus;
      this.path = builder.path;
      this.cityNameNorm = builder.cityNameNorm;
      this.genitiveName = builder.genitiveName;
      this.regionId = builder.regionId;
      this.latitude = builder.latitude;
      this.longitude = builder.longitude;
      this.parentCityId = builder.parentCityId;
      this.cityDepartment = builder.cityDepartment;
    }

    public String getFiasId() {
      return fiasId;
    }

    public String getFiasName() {
      return fiasName;
    }

    public Integer getAjsonId() {
      return ajsonId;
    }

    public String getCityNameRus() {
      return cityNameRus;
    }

    public String getPath() {
      return path;
    }

    public String getCityNameNorm() {
      return cityNameNorm;
    }

    public String getGenitiveName() {
      return genitiveName;
    }

    public Integer getRegionId() {
      return regionId;
    }

    public Double getLatitude() {
      return latitude;
    }

    public Double getLongitude() {
      return longitude;
    }

    public Integer getParentCityId() {
      return parentCityId;
    }

    public String getCityDepartment() {
      return cityDepartment;
    }

    /**
     * Compare city.
     *
     * @param expected expected city
     */
    public void equals(City expected) {
      final var softly = new SoftAssertions();
      softly.assertThat(this.getFiasId()).isEqualTo(expected.getFiasId());
      softly.assertThat(this.getFiasName()).isEqualTo(expected.getFiasName());
      softly.assertThat(this.getAjsonId()).isEqualTo(expected.getAjsonId());
      softly.assertThat(this.getCityNameRus()).isEqualTo(expected.getCityNameRus());
      softly.assertThat(this.getPath()).isEqualTo(expected.getPath());
      softly.assertThat(this.getCityNameNorm()).isEqualTo(expected.getCityNameNorm());
      softly.assertThat(this.getGenitiveName()).isEqualTo(expected.getGenitiveName());
      softly.assertThat(this.getRegionId()).isEqualTo(expected.getRegionId());
      softly.assertThat(this.getLatitude()).isEqualTo(expected.getLatitude());
      softly.assertThat(this.getLongitude()).isEqualTo(expected.getLongitude());
      softly.assertThat(this.getParentCityId()).isEqualTo(expected.getParentCityId());
      softly.assertThat(this.getCityDepartment()).isEqualTo(expected.getCityDepartment());
      softly.assertAll();
    }

    @Override
    public int compareTo(@NotNull City city) {
      return this.getFiasId().compareTo(city.getFiasId());
    }

    public static class Builder {

      private String fiasId;
      private String fiasName;
      private Integer ajsonId;
      private String cityNameRus;
      private String path;
      private String cityNameNorm;
      private String genitiveName;
      private Integer regionId;
      private Double latitude;
      private Double longitude;
      private Integer parentCityId;
      private String cityDepartment;

      public Builder setFiasId(String fiasId) {
        this.fiasId = fiasId;
        return this;
      }

      public Builder setFiasName(String fiasName) {
        this.fiasName = fiasName;
        return this;
      }

      public Builder setAjsonId(Integer ajsonId) {
        this.ajsonId = ajsonId;
        return this;
      }

      public Builder setCityNameRus(String cityNameRus) {
        this.cityNameRus = cityNameRus;
        return this;
      }

      public Builder setPath(String path) {
        this.path = path;
        return this;
      }

      public Builder setCityNameNorm(String cityNameNorm) {
        this.cityNameNorm = cityNameNorm;
        return this;
      }

      public Builder setGenitiveName(String genitiveName) {
        this.genitiveName = genitiveName;
        return this;
      }

      public Builder setRegionId(Integer regionId) {
        this.regionId = regionId;
        return this;
      }

      public Builder setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
      }

      public Builder setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
      }

      public Builder setParentCityId(Integer parentCityId) {
        this.parentCityId = parentCityId;
        return this;
      }

      public Builder setCityDepartment(String cityDepartment) {
        this.cityDepartment = cityDepartment;
        return this;
      }

      public City build() {
        return new City(this);
      }
    }
  }
}
