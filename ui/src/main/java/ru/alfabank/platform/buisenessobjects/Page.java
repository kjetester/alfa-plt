package ru.alfabank.platform.buisenessobjects;

import org.apache.log4j.*;

import java.time.*;
import java.util.*;

public class Page {

  private static final Logger LOGGER = LogManager.getLogger(Page.class);

  private String id;
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
   * @param path path
   * @param url url
   * @param title title
   * @param description description
   * @param keywords keywords
   * @param dateFrom date from
   * @param dateTo date to
   */
  public Page(
      String path,
      String url,
      String title,
      String description,
      String keywords,
      LocalDateTime dateFrom,
      LocalDateTime dateTo) {
    this.id = url.substring(url.lastIndexOf("/") + 1);
    this.path = path;
    this.url = url;
    this.title = title;
    this.description = description;
    this.keywords = keywords;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    LOGGER.debug("Создан новый бизнес-объект 'Страница'");
  }

  /**
   * Class constructor.
   * @param path path
   * @param url url
   */
  public Page(String path, String url) {
    this.path = path;
    this.id = url.substring(url.lastIndexOf("/") + 1);
  }

  public String getId() {
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

  public void getWidgetList(List<Widget> widgetList) {
    this.widgetList = widgetList;
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
}
