package ru.alfabank.platform.businessobjects.feedback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;
import ru.alfabank.platform.businessobjects.Pagination;

public class FeedbackResponseWrapper extends AbstractBusinessObject {

  private final List<Feedback> data;
  private final Pagination pagination;

  @JsonCreator
  public FeedbackResponseWrapper(@JsonProperty("data") final List<Feedback> data,
                                 @JsonProperty("pagination") final Pagination pagination) {
    this.data = data;
    this.pagination = pagination;
  }

  public List<Feedback> getData() {
    return data;
  }

  public Pagination getPagination() {
    return pagination;
  }
}
