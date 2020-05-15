package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.annotations.NonNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import ru.alfabank.platform.businessobjects.enums.Team;

@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Page extends AbstractBusinessObject {

  @JsonIgnore
  private static final Logger LOGGER = LogManager.getLogger(Page.class);

  private Integer id;
  private final String uri;
  private String title;
  private String description;
  private String dateFrom;
  private String dateTo;
  private Boolean enable;
  private List<Team> teams;
  @JsonIgnore private List<Widget> widgetList;
  private List<String> childUids;

  @JsonCreator
  private Page(
      @JsonProperty ("id") Integer id,
      @JsonProperty ("uri") String uri,
      @JsonProperty ("title") String title,
      @JsonProperty ("teams") List<Team> teams,
      @JsonProperty ("description") String description,
      @JsonProperty ("dateFrom") String dateFrom,
      @JsonProperty ("dateTo") String dateTo,
      @JsonProperty ("enable") Boolean enable) {
    this.id = id;
    this.uri = uri;
    this.title = title;
    this.description = description;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.teams = teams;
    this.enable = enable;
  }

  /**
   * Class constructor.
   * @param builder builder
   */
  public Page(final Builder builder) {
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
    this.teams = builder.teams;
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

  public List<Team> getTeams() {
    return teams;
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
        .assertThat(this.getTeams())
        .as("Проверка Teams страницы")
        .isEqualTo(page.getTeams());
  }

  @JsonIgnoreType
  public static class Builder {
    private Integer id;
    private String uri;
    private String title;
    private String description;
    private String dateFrom;
    private String dateTo;
    private Boolean enable;
    private List<Team> teams;
    private List<String> childUids;
    private List<Widget> widgets;

    public Builder setId(Integer id) {
      this.id = id;
      return this;
    }

    /**
     * Set URI.
     * @param uri uri
     * @return PageBuilder
     */
    public Builder setUri(String uri) {
      if (!uri.startsWith("/")) {
        uri = "/" + uri;
      }
      if (!uri.endsWith("/")) {
        uri = uri + "/";
      }
      this.uri = uri;
      return this;
    }

    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setDateFrom(String dateFrom) {
      this.dateFrom = dateFrom;
      return this;
    }

    public Builder setDateTo(String dateTo) {
      this.dateTo = dateTo;
      return this;
    }

    public Builder setTeamsList(List<Team> teams) {
      this.teams = teams;
      return this;
    }

    public Builder setEnable(Boolean enable) {
      this.enable = enable;
      return this;
    }

    public Builder setChildUids(List<String> childUids) {
      this.childUids = childUids;
      return this;
    }

    public Builder setWidgets(List<Widget> widget) {
      this.widgets = widget;
      return this;
    }

    /**
     * Reusing page.
     * @param page page
     * @return page builder
     */
    public Builder using(Page page) {
      this.id = page.id;
      this.uri = page.uri;
      this.title = page.title;
      this.description = page.description;
      if (page.dateFrom == null) {
        this.dateFrom = null;
      } else {
        this.dateFrom = page.dateFrom;
      }
      if (page.dateTo == null) {
        this.dateTo = null;
      } else {
        this.dateTo = page.dateTo;
      }
      this.teams = page.teams;
      this.enable = page.enable;
      this.widgets = page.widgetList;
      return this;
    }

    public Page build() {
      return new Page(this);
    }
  }
}
