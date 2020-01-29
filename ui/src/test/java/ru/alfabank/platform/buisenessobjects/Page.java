package ru.alfabank.platform.buisenessobjects;

import java.time.*;
import java.util.*;

public class Page {

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

  public Page(
      String path,
      String url,
      String title,
      String description,
      String keywords,
      LocalDateTime dateFrom,
      LocalDateTime dateTo) {
    this.path = path;
    this.url = url;
    this.title = title;
    this.description = description;
    this.keywords = keywords;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  public Page(String path, String url) {
    this.path = path;
    this.id = url.substring(description.lastIndexOf("/") + 1);
  }

  public String getId() {
    return url.substring(description.lastIndexOf("/") + 1);
  }

  public String getPath() {
    return path;
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
}
