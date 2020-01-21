package ru.alfabank.platform.buisenessobjects;

import java.time.LocalDateTime;
import java.util.List;

public class Page {

  private String id;
  private String uri;
  private String title;
  private String description;
  private String keywords;
  private LocalDateTime dateFrom;
  private LocalDateTime dateTo;
  private boolean isEnable;
  private List<Widget> widgetList;

  /**
   * Class constructor.
   * @param uri uri
   */
  public Page(String uri, String url) {
    this.uri = uri;
    this.id = url.substring(url.lastIndexOf("/") + 1);
  }

  public String getId() {
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

  public void setWidgetList(List<Widget> widgetList) {
    this.widgetList = widgetList;
  }
}
