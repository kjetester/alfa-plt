package ru.alfabank.platform.businessobjects.contentstore;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;

public class Value extends AbstractBusinessObject implements Comparable<Value> {

  @JsonIgnore
  private static final Logger LOGGER = LogManager.getLogger(Value.class);

  private String uid;
  @JsonRawValue
  private JsonNode value;
  private List<String> geo;

  /**
   * Class constructor.
   *
   * @param uid   uid
   * @param value value
   * @param geo   geo
   */
  @JsonCreator
  public Value(
      @JsonProperty("uid") final String uid,
      @JsonProperty("value") final JsonNode value,
      @JsonProperty("geo") final List<String> geo) {
    this.uid = uid;
    this.value = value;
    this.geo = geo;
  }

  /**
   * Class constructor.
   *
   * @param builder builder
   */
  @JsonIgnore
  public Value(Builder builder) {
    this.uid = builder.uid;
    this.value = builder.value;
    this.geo = builder.geo;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public JsonNode getValue() {
    return value;
  }

  public void setValue(JsonNode value) {
    this.value = value;
  }

  public List<String> getGeo() {
    return geo;
  }

  public void setGeo(List<String> geo) {
    this.geo = geo;
  }

  /**
   * Compare value.
   *
   * @param expected comparing value
   */
  public void equals(@NotNull final Value expected) {
    logComparingObjects(LOGGER, this, expected);
    final var softly = new SoftAssertions();
    softly
        .assertThat(expected.getValue())
        .as("Проверка значения")
        .isEqualTo(this.getValue());
    softly
        .assertThat(expected.getGeo())
        .as("Проверка гео")
        .isEqualTo(this.getGeo());
    softly
        .assertThat(expected.getUid())
        .as("Проверка UID")
        .isEqualTo(this.getUid());
    softly.assertAll();
    logComparingResult(LOGGER, this.getUid());
  }

  /**
   * Compare value.
   *
   * @param expected comparing value
   * @param method   copying method
   */
  public void equals(@NotNull final Value expected,
                     @NotNull final CopyMethod method,
                     final boolean isReused) {
    logComparingObjects(LOGGER, this, expected);
    final var softly = new SoftAssertions();
    softly
        .assertThat(expected.getValue())
        .as("Проверка значения")
        .isEqualTo(this.getValue());
    softly
        .assertThat(expected.getGeo())
        .as("Проверка гео")
        .isEqualTo(this.getGeo());
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
  }

  @Override
  public int compareTo(@NotNull Value value) {
    return getValue().toString().compareTo(value.getValue().toString());
  }

  public static class Builder {

    private String uid;
    private JsonNode value;
    private List<String> geo;

    public Builder setUid(String uid) {
      this.uid = uid;
      return this;
    }

    public Builder setValue(JsonNode value) {
      this.value = value;
      return this;
    }

    public Builder setGeo(List<String> geo) {
      this.geo = geo;
      return this;
    }

    /**
     * Reusing value.
     *
     * @param value value
     * @return builder
     */
    public Builder using(Value value) {
      this.uid = value.uid;
      this.value = value.value;
      this.geo = value.geo;
      return this;
    }

    public Value build() {
      return new Value(this);
    }
  }
}
