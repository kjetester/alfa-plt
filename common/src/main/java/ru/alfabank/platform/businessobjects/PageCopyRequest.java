package ru.alfabank.platform.businessobjects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;

public class PageCopyRequest extends AbstractBusinessObject {

  private static final Logger LOGGER = LogManager.getLogger(PageCopyRequest.class);

  private final CopyMethod mode;
  private final Page page;

  public PageCopyRequest(Builder builder) {
    this.mode = builder.mode;
    this.page = builder.page;
  }

  public CopyMethod getMode() {
    return mode;
  }

  public Page getPage() {
    return page;
  }

  public static class Builder {

    private CopyMethod mode;
    private Page page;

    public Builder setMode(CopyMethod mode) {
      this.mode = mode;
      return this;
    }

    public Builder setPage(Page page) {
      this.page = page;
      return this;
    }

    public PageCopyRequest build() {
      return new PageCopyRequest(this);
    }
  }
}
