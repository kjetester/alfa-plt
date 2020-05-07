package ru.alfabank.platform.buisenessobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.annotations.NonNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import ru.alfabank.platform.buisenessobjects.enums.Team;

@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Page {

  @JsonIgnore
  private static final Logger LOGGER = LogManager.getLogger(Page.class);

  private Integer id;
  private String uri;
  private String title;
  private String description;
  private String dateFrom;
  private String dateTo;
  private List<Team> teamList;
  private Boolean enable;
  @JsonIgnore private List<Widget> widgetList;
  private List<String> childUids;

  @JsonCreator
  private Page(
      @JsonProperty ("id") Integer id,
      @JsonProperty ("uri") String uri,
      @JsonProperty ("title") String title,
      @JsonProperty ("description") String description,
      @JsonProperty ("dateFrom") String dateFrom,
      @JsonProperty ("dateTo") String dateTo,
      @JsonProperty ("teams") List<Team> teamList,
      @JsonProperty ("enable") Boolean enable) {
    this.id = id;
    this.uri = uri;
    this.title = title;
    this.description = description;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.teamList = teamList;
    this.enable = enable;
  }

  /**
   * Class constructor.
   * @param builder builder
   */
  public Page(final PageBuilder builder) {
    this.id = builder.id;
    this.uri = builder.uri;
    this.title = builder.title;
    this.description = builder.description;
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
    this.teamList = builder.teamList;
    this.enable = builder.enable;
    this.childUids = builder.childUids;
    this.widgetList = Objects.requireNonNullElseGet(builder.widgets, ArrayList::new);
    LOGGER.info(String.format("Создан (обновлен) объект 'страница':\n\t%s", this.toString()));
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

  @JsonIgnore
  public LocalDateTime getLocalDateTimeFrom() {
    return LocalDateTime.parse(dateFrom);
  }

  public String getDateFrom() {
    return dateFrom;
  }

  @JsonIgnore
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

  @JsonIgnore
  public void setWidgetList(List<Widget> widgets) {
    this.widgetList = widgets;
  }

  public List<Team> getTeamList() {
    return teamList;
  }

  private List<String> getChildUids() {
    return childUids;
  }

  @Override
  public String toString() {
    return String.format("Page{id='%s', uri='%s', title='%s', description='%s', "
            + "dateFrom='%s', dateTo='%s', enable='%s', widgetList= '%s'}",
        id, uri, title, description, dateFrom, dateTo, enable, widgetList);
  }

  /**
   * Compare this page against any page.
   * @param page a page
   */
  @JsonIgnore
  public void equals(@NonNull final Page page) {
    final SoftAssertions softly = new SoftAssertions();
    LOGGER.debug(String.format(
        "Сравнение PAGES:\nACTUAL.\t\t%s\nEXPECTED.\t%s",
        page.toString(),
        this.toString()));
    softly
        .assertThat(this.getId())
        .as("Проверка ID страницы")
        .isEqualTo(page.getId());
    softly
        .assertThat(this.getUri())
        .as("Проверка URI страницы")
        .isEqualTo(page.getUri());
    softly
        .assertThat(this.getTitle())
        .as("Проверка Title страницы")
        .isEqualTo(page.getTitle());
    softly
        .assertThat(this.getDescription())
        .as("Проверка Description страницы")
        .isEqualTo(page.getDescription());
    softly
        .assertThat(this.isEnable())
        .as("Проверка isEnable страницы")
        .isEqualTo(page.isEnable());
    softly
        .assertThat(this.getDateFrom())
        .as("Проверка DateFrom страницы")
        .isEqualTo(page.getDateFrom());
    softly
        .assertThat(this.getDateTo())
        .as("Проверка DateTo страницы")
        .isEqualTo(page.getDateTo());
    softly
        .assertThat(this.getTeamList())
        .as("Проверка Teams страницы")
        .isEqualTo(page.getTeamList());
  }

  @JsonIgnoreType
  public static class PageBuilder {
    private Integer id;
    private String uri;
    private String title;
    private String description;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private List<Team> teamList;
    private Boolean enable;
    private List<String> childUids;
    private List<Widget> widgets;

    public PageBuilder setId(Integer id) {
      this.id = id;
      return this;
    }

    /**
     * Set URI.
     * @param uri uri
     * @return PageBuilder
     */
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

    public PageBuilder setDateFrom(LocalDateTime dateFrom) {
      this.dateFrom = dateFrom;
      return this;
    }

    public PageBuilder setDateTo(LocalDateTime dateTo) {
      this.dateTo = dateTo;
      return this;
    }

    public PageBuilder setTeamList(Team... teamList) {
      this.teamList = Arrays.asList(teamList);
      return this;
    }

    public PageBuilder setEnable(Boolean enable) {
      this.enable = enable;
      return this;
    }

    public PageBuilder setChildUids(List<String> childUids) {
      this.childUids = childUids;
      return this;
    }

    public PageBuilder setWidgets(List<Widget> widget) {
      this.widgets = widget;
      return this;
    }

    /**
     * Reusing page.
     * @param page page
     * @return page builder
     */
    public PageBuilder using(Page page) {
      this.id = page.id;
      this.uri = page.uri;
      this.title = page.title;
      this.description = page.description;
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
      this.teamList = page.teamList;
      this.enable = page.enable;
      this.widgets = page.widgetList;
      return this;
    }

    public Page build() {
      return new Page(this);
    }
  }
}
