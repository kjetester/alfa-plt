package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pagination extends AbstractBusinessObject {

  private final Integer limit;
  private final Integer offset;
  private final String sort;
  private final Integer totalCount;

  /**
   * Class constructor.
   * @param limit limit
   * @param offset offset
   * @param sort sort
   * @param totalCount total count
   */
  @JsonCreator
  public Pagination(@JsonProperty("limit") final Integer limit,
                    @JsonProperty("offset") final Integer offset,
                    @JsonProperty("sort") final String sort,
                    @JsonProperty("totalCount") final Integer totalCount) {
    this.limit = limit;
    this.offset = offset;
    this.sort = sort;
    this.totalCount = totalCount;
  }

  public Integer getLimit() {
    return limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public String getSort() {
    return sort;
  }

  public Integer getTotalCount() {
    return totalCount;
  }
}
