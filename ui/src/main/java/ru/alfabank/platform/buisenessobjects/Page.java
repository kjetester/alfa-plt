package ru.alfabank.platform.buisenessobjects;

import com.fasterxml.jackson.annotation.*;
import com.sun.org.apache.xalan.internal.xsltc.dom.*;

import java.time.*;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Page {

  private Integer id;
  private String uri;
  private String title;
  private String description;
  private String keywords;
  private String dateFrom;
  private String dateTo;
  private Boolean enable;
  @JsonIgnore
  private List<Widget> widgetList;

  /**
   * Class constructor.
   * @param builder builder
   */
  public Page(final PageBuilder builder) {
    this.id = builder.id;
    this.uri = builder.uri;
    this.title = builder.title;
    this.description = builder.description;
    this.keywords = builder.keywords;
    if (builder.dateFrom == null) {
      this.dateFrom = null;
    } else {
      this.dateFrom = builder.dateFrom.toString();
    }
    if (builder.dateTo == null) {
      this.dateTo = null;
    } else {
      this.dateTo = builder.dateTo.toString();
    }
    this.enable = builder.enable;
  }

  public Page(String uri) {
    this.uri = uri;
  }

  public Integer getId() {
    return id;
  }

  public String getUri() {
    return uri;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getKeywords() {
    return keywords;
  }

  public LocalDateTime getLocalDateTimeFrom() {
    return LocalDateTime.parse(dateFrom);
  }

  public String getDateFrom() {
    return dateFrom;
  }

  public LocalDateTime getLocalDateTimeTo() {
    return LocalDateTime.parse(dateTo);
  }

  public String getDateTo() {
    return dateTo;
  }

  public Boolean isEnable() {
    return enable;
  }

  public List<Widget> getWidgetList() {
    return widgetList;
  }

  public void setWidgetList(ArrayList<Widget> widgets) {
    this.widgetList = widgets;
  }

  @JsonIgnoreType
  public static class PageBuilder {
    private Integer id;
    private String uri;
    private String title;
    private String description;
    private String keywords;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean enable;

    public PageBuilder setId(Integer id) {
      this.id = id;
      return this;
    }

    public PageBuilder setUri(String uri) {
      if (!uri.startsWith("/")) {
        uri = "/" + uri;
      }
      if (!uri.endsWith("/")) {
        uri = uri + "/";
      }
      this.uri = uri;
      return this;
    }

    public PageBuilder setTitle(String title) {
      this.title = title;
      return this;
    }

    public PageBuilder setDescription(String description) {
      this.description = description;
      return this;
    }

    public PageBuilder setKeywords(String keywords) {
      this.keywords = keywords;
      return this;
    }

    public PageBuilder setDateFrom(LocalDateTime dateFrom) {
      this.dateFrom = dateFrom;
      return this;
    }

    public PageBuilder setDateTo(LocalDateTime dateTo) {
      this.dateTo = dateTo;
      return this;
    }

    public PageBuilder setEnable(Boolean enable) {
      this.enable = enable;
      return this;
    }

    /**
     * Reusing spec.
     * @param page page
     * @return page builder
     */
    public PageBuilder using(Page page) {
      this.id = page.id;
      this.uri = page.uri;
      this.title = page.title;
      this.description = page.description;
      this.keywords = page.keywords;
      if (page.dateFrom == null) {
        this.dateFrom = null;
      } else {
        this.dateFrom = LocalDateTime.parse(page.dateFrom);
      }
      if (page.dateTo == null) {
        this.dateTo = null;
      } else {
        this.dateTo = LocalDateTime.parse(page.dateTo);
      }
      this.enable = page.enable;
      return this;
    }

    public Page build() {
      return new Page(this);
    }
  }
}
