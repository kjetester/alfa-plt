package ru.alfabank.platform.businessobjects.feedback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class DataWrapper extends AbstractBusinessObject {

  private final Feedback data;

  @JsonCreator
  public DataWrapper(@JsonProperty("data") final Feedback data) {
    this.data = data;
  }

  public Feedback getData() {
    return data;
  }
}
