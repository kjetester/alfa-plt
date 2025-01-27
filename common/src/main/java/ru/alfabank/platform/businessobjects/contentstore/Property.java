package ru.alfabank.platform.businessobjects.contentstore;

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
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;

public class Property extends AbstractBusinessObject implements Comparable<Property> {

  @JsonIgnore
  private static final Logger LOGGER = LogManager.getLogger(Property.class);

  private String uid;
  private String name;
  private List<Value> values;

  /**
   * Class constructor.
   *
   * @param uid    uid
   * @param name   name
   * @param values values
   */
  @JsonCreator
  public Property(
      @JsonProperty("uid") String uid,
      @JsonProperty("name") String name,
      @JsonProperty("values") List<Value> values) {
    this.uid = uid;
    this.name = name;
    this.values = values;
    Collections.sort(values);
  }

  /**
   * Class constructor.
   * @param builder builder
   */
  public Property(final Builder builder) {
    this.uid = builder.uid;
    this.name = builder.name;
    this.values = builder.values;
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

  public List<Value> getValues() {
    return values;
  }

  public void setValues(List<Value> values) {
    this.values = values;
  }

  /**
   * Compare property.
   *
   * @param expectedPropery comparing property
   */
  public void equals(@NotNull final Property expectedPropery) {
    logComparingObjects(LOGGER, this, expectedPropery);
    final var softly = new SoftAssertions();
    final var expectedValuesCount = expectedPropery.getValues().size();
    softly
        .assertThat(this.getValues().size())
        .as("Проверка количества значений")
        .isEqualTo(expectedValuesCount);
    softly
        .assertThat(this.getName())
        .as("Проверка наименований")
        .isEqualTo(expectedPropery.getName());
    softly
        .assertThat(this.getUid())
        .as("Проверка UID при 'SHARE' или 'CURRENT' и признаке переиспользования")
        .isEqualTo(expectedPropery.getUid());
    softly.assertAll();
    IntStream.range(0, expectedValuesCount).forEach(i -> values.get(i)
        .equals(expectedPropery.getValues().get(i)));
    logComparingResult(LOGGER, this.getUid());
  }

  /**
   * Compare property.
   *
   * @param expected comparing property
   * @param method   copying method
   */
  public void equals(@NotNull final Property expected,
                     @NotNull final CopyMethod method,
                     final boolean isReused) {
    logComparingObjects(LOGGER, this, expected);
    final var softly = new SoftAssertions();
    final var expectedValuesCount = this.getValues().size();
    softly
        .assertThat(expected.getValues().size())
        .as("Проверка количества значений")
        .isEqualTo(expectedValuesCount);
    softly
        .assertThat(expected.getName())
        .as("Проверка наименований")
        .isEqualTo(this.getName());
    if (method.equals(CopyMethod.SHARE) || (method.equals(CopyMethod.CURRENT) && isReused)) {
      softly
          .assertThat(expected.getUid())
          .as("Проверка UID при 'SHARE' или 'CURRENT' и признаке переиспользования")
          .isEqualTo(this.getUid());
    } else {
      softly
          .assertThat(expected.getUid())
          .as("Проверка UID при 'COPY'")
          .isNotEqualTo(this.getUid());
    }
    softly.assertAll();
    IntStream.range(0, expectedValuesCount).forEach(i -> values.get(i)
        .equals(expected.getValues().get(i), method, isReused));
  }

  @Override
  public int compareTo(@NotNull Property property) {
    return getName().compareTo(property.getName());
  }

  public static class Builder {

    private String uid;
    private String name;
    private List<Value> values;

    public Builder setUid(String uid) {
      this.uid = uid;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setValues(List<Value> values) {
      this.values = values;
      return this;
    }

    public Property build() {
      return new Property(this);
    }
  }
}
