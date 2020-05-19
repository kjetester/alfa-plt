package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;

public class Property implements Comparable<Property> {

  @JsonIgnore
  private static final Logger LOGGER = LogManager.getLogger(Property.class);

  private String uid;
  private String name;
  private String device;
  private List<Value> values;

  /**
   * Class constructor.
   *
   * @param uid    uid
   * @param name   name
   * @param device device
   * @param values values
   */
  @JsonCreator
  public Property(
      @JsonProperty("uid") String uid,
      @JsonProperty("name") String name,
      @JsonProperty("device") String device,
      @JsonProperty("values") List<Value> values) {
    this.uid = uid;
    this.name = name;
    this.device = device;
    this.values = values;
    Collections.sort(values);
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

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public List<Value> getValues() {
    return values;
  }

  public void setValues(List<Value> values) {
    this.values = values;
  }

  @Override
  @JsonIgnore
  public String toString() {
    return String.format(
        "Property{uid='%s', name='%s', device='%s', values='%s',}",
        uid,
        name,
        device,
        values);
  }

  /**
   * Compare property.
   *
   * @param property comparing property
   * @param method   copying method
   */
  public void equals(
      @NotNull final Property property,
      final CopyMethod method,
      final boolean isReused) {
    LOGGER.debug(String.format(
        "Сравнение PROPERTIES:\nACTUAL.\t\t%s\nEXPECTED.\t%s",
        property.toString(),
        this.toString()));
    final var softly = new SoftAssertions();
    final var expectedValuesCount = this.getValues().size();
    softly
        .assertThat(property.getValues().size())
        .as("Проверка количества значений")
        .isEqualTo(expectedValuesCount);
    softly
        .assertThat(property.getName())
        .as("Проверка наименований")
        .isEqualTo(this.getName());
    softly
        .assertThat(property.getDevice())
        .as("Проверка девайса")
        .isEqualTo(this.getDevice());
    if (method.equals(CopyMethod.SHARE) || (method.equals(CopyMethod.CURRENT) && isReused)) {
      softly
          .assertThat(property.getUid())
          .as("Проверка UID при 'SHARE' или 'CURRENT' и признаке переиспользования")
          .isEqualTo(this.getUid());
    } else {
      softly
          .assertThat(property.getUid())
          .as("Проверка UID при 'COPY'")
          .isNotEqualTo(this.getUid());
    }
    softly.assertAll();
    IntStream.range(0, expectedValuesCount).forEach(i -> values.get(i)
        .equals(property.getValues().get(i), method, isReused));
  }

  @Override
  public int compareTo(@NotNull Property property) {
    return getName().compareTo(property.getName());
  }
}
