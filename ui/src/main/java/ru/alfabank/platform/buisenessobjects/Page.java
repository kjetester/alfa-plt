package ru.alfabank.platform.buisenessobjects;

import org.apache.log4j.*;

import java.time.*;
import java.util.*;

public class Page {

  private static final Logger LOGGER = LogManager.getLogger(Page.class);

  private int id;
  private String path;
  private String url;
  private String title;
  private String description;
  private String keywords;
  private LocalDateTime dateFrom;
  private LocalDateTime dateTo;
  private boolean isEnable;
  private List<Widget> widgetList;

  /**
   * Class constructor.
   * @param builder builder
   */
  public Page(final PageBuilder builder) {
    this.id = builder.id;
    this.path = builder.path;
    this.url = builder.url;
    this.title = builder.title;
    this.description = builder.description;
    this.keywords = builder.keywords;
    this.dateFrom = builder.dateFrom;
    this.dateTo = builder.dateTo;
    this.isEnable = builder.isEnable;
  }

  public Page(String path) {
    this.path = path;
  }

  public int getId() {
    return id;
  }

  public String getPath() {
    return path;
  }

  public String getUrl() {
    return url;
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

  public LocalDateTime getDateFrom() {
    return dateFrom;
  }

  public LocalDateTime getDateTo() {
    return dateTo;
  }

  public boolean isEnable() {
    return isEnable;
  }

  public List<Widget> getWidgetList() {
    return widgetList;
  }

  public void setWidgetList(ArrayList<Widget> widgets) {
    this.widgetList = widgets;
  }

  @Override
  public String toString() {
    return String.format(
        "\n\tid: '%s'\n\tpath: '%s'\n\turl: '%s'\n\ttitle: '%s'\n\tdescription: '%s'\n\tkeywords:"
            + " '%s'\n\tdateFrom: '%s'\n\tdateTo: '%s'\n\tisEnable: '%b'\n\tWidgetList: '%s'",
        id,
        path,
        url,
        title,
        description,
        keywords,
        dateFrom,
        dateTo,
        isEnable,
        widgetList);
  }

  public static class PageBuilder {
    private int id;
    private String path;
    private String url;
    private String title;
    private String description;
    private String keywords;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private boolean isEnable;

    public PageBuilder setId(int id) {
      this.id = id;
      return this;
    }

    public PageBuilder setPath(String path) {
      this.path = path;
      return this;
    }

    public PageBuilder setUrl(String url) {
      this.url = url;
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

    public PageBuilder setEnable(boolean enable) {
      isEnable = enable;
      return this;
    }

    /**
     * Reusing spec.
     * @param page page
     * @return page builder
     */
    public PageBuilder using(Page page) {
      this.id = page.id;
      this.path = page.path;
      this.url = page.url;
      this.title = page.title;
      this.description = page.description;
      this.keywords = page.keywords;
      this.dateFrom = page.dateFrom;
      this.dateTo = page.dateTo;
      this.isEnable = page.isEnable;
      return this;
    }

    public Page build() {
      return new Page(this);
    }
  }
}
