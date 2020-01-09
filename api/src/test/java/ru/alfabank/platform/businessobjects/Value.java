package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.json.JSONObject;

public class Value {

  private String uid;
  @JsonRawValue
  private Object value;
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
      @JsonProperty("value") Object value,
      @JsonProperty("geo") String[] geo) {
    this.uid = uid;
    this.value = value;
    this.geo = geo;
  }

  /**
   * Class constructor.
   */
  public Value() {
    this.uid = "uid";
    this.value = "value";
    this.geo = new String[]{"1","2"};
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(JSONObject value) {
    this.value = value;
  }

  public String[] getGeo() {
    return geo;
  }

  public void setGeo(String[] geo) {
    this.geo = geo;
  }

}
