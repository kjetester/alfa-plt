package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.annotations.NonNull;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties ("default")
public class Option extends AbstractBusinessObject {

  @JsonIgnore
  private static final Logger LOGGER = LogManager.getLogger(Experiment.class);

  private final String uuid;
  private final String description;
  private final String experimentUuid;
  private final Boolean isDefault;
  private final String name;
  private final Double trafficRate;
  private final List<String> widgetUids;

  @JsonCreator
  private Option(
      @JsonProperty("uuid") String uuid,
      @JsonProperty("name") String name,
      @JsonProperty("description") String description,
      @JsonProperty("experimentUuid") String experimentUuid,
      @JsonProperty("isDefault") Boolean isDefault,
      @JsonProperty("trafficRate") Double trafficRate,
      @JsonProperty("widgetUids") List<String> widgetUids) {
    this.uuid = uuid;
    this.description = description;
    this.experimentUuid = experimentUuid;
    this.isDefault = isDefault;
    this.name = name;
    this.trafficRate = trafficRate;
    this.widgetUids = widgetUids;
    LOGGER.debug("Создан / обновлен экземпляр варианта:\n" + describeBusinessObject(this));
  }

  private Option(Builder builder) {
    this.uuid = builder.uuid;
    this.description = builder.description;
    this.experimentUuid = builder.experimentUuid;
    this.isDefault = builder.isDefault;
    this.name = builder.name;
    this.trafficRate = builder.trafficRate;
    this.widgetUids = builder.widgetUids;
    LOGGER.debug("Создан / обновлен экземпляр варианта:\n"
        + AbstractBusinessObject.describeBusinessObject(this));
  }

  public String getUuid() {
    return uuid;
  }

  public String getDescription() {
    return description;
  }

  public String getExperimentUuid() {
    return experimentUuid;
  }

  public Boolean getDefault() {
    return isDefault;
  }

  public String getName() {
    return name;
  }

  public Double getTrafficRate() {
    return trafficRate;
  }

  public List<String> getWidgetUids() {
    return widgetUids;
  }

  /**
   * Check option properties.
   * @param expected expected option
   */
  public void equals(@NonNull final Option expected) {
    LOGGER.info(String.format(
        "Сравнение вариантов:\nACTUAL:\n%s\n\nEXPECTED:\n%s",
        describeBusinessObject(this),
        describeBusinessObject(expected)));
    final var softly = new SoftAssertions();
    softly.assertThat(this.getUuid())
        .as("Проверка UUID варианта")
        .isEqualTo(expected.getUuid());
    softly.assertThat(this.getDescription())
        .as("Проверка Описания варианта")
        .isEqualTo(expected.getDescription());
    softly.assertThat(this.getExperimentUuid())
        .as("Проверка UUID эксперимента ассоциированного с вариантом")
        .isEqualTo(expected.getExperimentUuid());
    softly.assertThat(this.getDefault())
        .as("Проверка, признака варианта по-умолчанию")
        .isEqualTo(expected.getDefault());
    softly.assertThat(this.getName())
        .as("Проверка наименования варианта")
        .isEqualTo(expected.getName());
    softly.assertThat(this.getTrafficRate())
        .as("Проверка значения доли трафика варианта")
        .isEqualTo(expected.getTrafficRate());
    softly.assertThat(this.getWidgetUids())
        .as("Проверка списка UUID'ов виджетов ассоциированных с вариантом")
        .isEqualTo(expected.getWidgetUids());
    softly.assertAll();
  }

  @JsonIgnoreType
  public static class Builder {

    private String uuid;
    private String description;
    private String experimentUuid;
    private Boolean isDefault;
    private String name;
    private Double trafficRate;
    private List<String> widgetUids;

    public Builder setUuid(String uuid) {
      this.uuid = uuid;
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

    public Builder setDefault(Boolean isDefault) {
      this.isDefault = isDefault;
      return this;
    }

    public Builder setExperimentUuid(String experimentUuid) {
      this.experimentUuid = experimentUuid;
      return this;
    }

    public Builder setTrafficRate(Double trafficRate) {
      this.trafficRate = trafficRate;
      return this;
    }

    public Builder setWidgetUids(List<String> widgetUids) {
      this.widgetUids = widgetUids;
      return this;
    }

    /**
     * Reusing option instance.
     * @param option option
     * @return this
     */
    public Builder using(Option option) {
      this.uuid = option.uuid;
      this.description = option.description;
      this.experimentUuid = option.experimentUuid;
      this.isDefault = option.isDefault;
      this.name = option.name;
      this.trafficRate = option.trafficRate;
      this.widgetUids = option.widgetUids;
      return this;
    }

    public Option build() {
      return new Option(this);
    }
  }
}
