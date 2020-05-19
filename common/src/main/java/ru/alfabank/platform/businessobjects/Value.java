package ru.alfabank.platform.businessobjects;

import static ru.alfabank.platform.businessobjects.enums.Geo.RU;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;

public class Value implements Comparable<Value> {

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
      @JsonProperty("uid") String uid,
      @JsonProperty("value") JsonNode value,
      @JsonProperty("geo") List<String> geo) {
    this.uid = uid;
    this.value = value;
    this.geo = geo;
  }

  /**
   * Class constructor.
   */
  public Value() throws IOException {
    this.uid = "uid";
    this.value = new ObjectMapper().readTree("value");
    this.geo = Collections.singletonList(RU.toString());
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

  @Override
  public String toString() {
    return String.format("Value{uid='%s', value='%s', geo='%s',}", uid, value, geo);
  }

  /**
   * Compare value.
   *
   * @param value  comparing value
   * @param method copying method
   */
  public void equals(Value value, CopyMethod method, boolean isReused) {
    LOGGER.debug(String.format(
        "Сравнение PROPERTY_VALUES:\nACTUAL.\t\t%s\nEXPECTED.\t%s",
        value.toString(),
        this.toString()));
    final var softly = new SoftAssertions();
    softly
        .assertThat(value.getValue())
        .as("Проверка значения")
        .isEqualTo(this.getValue());
    softly
        .assertThat(value.getGeo())
        .as("Проверка гео")
        .isEqualTo(this.getGeo());
    if (method.equals(CopyMethod.SHARE) || (method.equals(CopyMethod.CURRENT) && isReused)) {
      softly
          .assertThat(value.getUid())
          .as("Проверка UID при 'SHARE' или 'CURRENT' и признаке переиспользования")
          .isEqualTo(this.getUid());
    } else {
      softly
          .assertThat(value.getUid())
          .as("Проверка UID при 'COPY'")
          .isNotEqualTo(this.getUid());
      softly.assertAll();
    }
  }

  @Override
  public int compareTo(@NotNull Value value) {
    return getValue().toString().compareTo(value.getValue().toString());
  }
}
