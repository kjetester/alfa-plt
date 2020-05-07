package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.annotations.NonNull;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Widget extends AbstractBusinessObject {

  @JsonIgnore
  private static final Logger LOGGER = LogManager.getLogger(Widget.class);

  private String uid;
  private String name;
  @JsonProperty("enable")
  private Boolean isEnabled;
  private String localization;
  private Device device;
  private String version;
  private String experimentOptionName;
  @JsonProperty("defaultWidget")
  private Boolean isDefaultWidget;
  private String dateFrom;
  private String dateTo;
  @JsonProperty("widgetGeo")
  private List<String> geo;
  private Integer orderNumber;
  private List<String> childUids;
  @JsonProperty("reused")
  private Boolean isReused;
  @JsonProperty("children")
  private List<Widget> childrenWidgetsList;

  /**
   * Class constructor.
   * @param builder builder
   */
  public Widget(final Builder builder) {
    this.uid = builder.uid;
    this.name = builder.name;
    this.orderNumber = builder.orderNumber;
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
    this.device = builder.device;
    this.version = builder.version;
    this.experimentOptionName = builder.experimentOptionName;
    this.isDefaultWidget = builder.defaultWidget;
    this.isEnabled = builder.isEnabled;
    this.localization = builder.localization;
    this.geo = builder.geo;
    this.childrenWidgetsList = builder.children;
    this.childUids = builder.childUids;
    this.isReused = builder.isReused;
    LOGGER.debug("Создан / обновлен экземпляр виджета:\n" + describeBusinessObject(this));
  }

  @JsonCreator
  private Widget(
      @JsonProperty("uid") String uid,
      @JsonProperty("name") String name,
      @JsonProperty("enable") Boolean isEnabled,
      @JsonProperty("localization") String localization,
      @JsonProperty("device") Device device,
      @JsonProperty("version") String version,
      @JsonProperty("experimentOptionName") String experimentOptionName,
      @JsonProperty("defaultWidget") Boolean isDefaultWidget,
      @JsonProperty("dateFrom") String dateFrom,
      @JsonProperty("dateTo") String dateTo,
      @JsonProperty("cityGroups") List<String> geo,
      @JsonProperty("orderNumber") Integer orderNumber,
      @JsonProperty("children") List<Widget> childrenWidgetsList,
      @JsonProperty("reused") Boolean isReused) {
    this.uid = uid;
    this.name = name;
    this.isEnabled = isEnabled;
    this.localization = localization;
    this.device = device;
    this.version = version;
    this.experimentOptionName = experimentOptionName;
    this.isDefaultWidget = isDefaultWidget;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.geo = geo;
    this.orderNumber = orderNumber;
    this.childrenWidgetsList = childrenWidgetsList;
    this.isReused = isReused;
  }

  public String getUid() {
    return uid;
  }

  public String getName() {
    return name;
  }

  public Integer getOrderNumber() {
    return orderNumber;
  }

  public String getDateFrom() {
    return dateFrom;
  }

  public String getDateTo() {
    return dateTo;
  }

  public Device getDevice() {
    return device;
  }

  public String getVersion() {
    return version;
  }

  public String getExperimentOptionName() {
    return experimentOptionName;
  }

  public Boolean isDefaultWidget() {
    return isDefaultWidget;
  }

  public Boolean isEnabled() {
    return isEnabled;
  }

  public String getLocalization() {
    return localization;
  }

  public List<String> getGeo() {
    return geo;
  }

  public List<Widget> getChildrenWidgetsList() {
    return childrenWidgetsList;
  }

  public List<String> getChildUids() {
    return childUids;
  }

  private Boolean isReused() {
    return isReused;
  }

  @JsonIgnoreType
  public static class Builder {

    private String uid;
    private String name;
    private Integer orderNumber;
    private String dateFrom;
    private String dateTo;
    private Device device;
    private Boolean isEnabled;
    private String localization;
    private String version;
    private String experimentOptionName;
    private Boolean defaultWidget;
    private List<String> geo;
    private List<Widget> children;
    private List<String> childUids;
    private Boolean isReused;

    public Builder setUid(String uid) {
      this.uid = uid;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setOrderNumber(Integer orderNumber) {
      this.orderNumber = orderNumber;
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

    public Builder setDevice(Device device) {
      this.device = device;
      return this;
    }

    public Builder isEnabled(Boolean enabled) {
      isEnabled = enabled;
      return this;
    }

    public Builder setLocalization(String localization) {
      this.localization = localization;
      return this;
    }

    public Builder setVersion(String version) {
      this.version = version;
      return this;
    }

    public Builder setExperimentOptionName(String experimentOptionName) {
      this.experimentOptionName = experimentOptionName;
      return this;
    }

    public Builder isDefaultWidget(Boolean defaultWidget) {
      this.defaultWidget = defaultWidget;
      return this;
    }

    public Builder setGeo(List<String> geo) {
      this.geo = geo;
      return this;
    }

    public Builder setChildren(List<Widget> children) {
      this.children = children;
      return this;
    }

    public Builder setChildUids(List<String> childUids) {
      this.childUids = childUids;
      return this;
    }

    public Builder setReused(Boolean isReused) {
      this.isReused = isReused;
      return this;
    }

    /**
     * Reusing widget.
     * @param widget widget
     * @return this
     */
    public Builder using(Widget widget) {
      this.uid = widget.uid;
      this.name = widget.name;
      this.orderNumber = widget.orderNumber;
      if (widget.dateFrom == null) {
        this.dateFrom = null;
      } else {
        this.dateFrom = widget.dateFrom;
      }
      if (widget.dateTo == null) {
        this.dateTo = null;
      } else {
        this.dateTo = widget.dateTo;
      }
      this.device = widget.device;
      this.isEnabled = widget.isEnabled;
      this.localization = widget.localization;
      this.geo = widget.geo;
      this.children = widget.childrenWidgetsList;
      this.childUids = widget.childUids;
      this.isReused = widget.isReused;
      this.version = widget.version;
      this.experimentOptionName = widget.experimentOptionName;
      this.defaultWidget = widget.isDefaultWidget;
      return this;
    }

    /**
     * Build widget.
     * @return widget
     */
    public Widget build() {
      return new Widget(this);
    }
  }

  /**
   * Check widgets properties.
   * @param expected expected Widget
   */
  public void equals(@NonNull final Widget expected) {
    LOGGER.info(String.format(
        "Сравнение виджетов:\nACTUAL:\n%s\n\nEXPECTED:\n%s",
        describeBusinessObject(this),
        describeBusinessObject(expected)));
    final var softly = new SoftAssertions();
    final var expectedChildrenCount = expected.getChildrenWidgetsList().size();
    softly
        .assertThat(this.getChildrenWidgetsList().size())
        .as("Проверка количества дочерних виджетов")
        .isEqualTo(expectedChildrenCount);
    softly
        .assertThat(this.getName())
        .as("Проверка наименований")
        .isEqualTo(expected.getName());
    softly
        .assertThat(this.getDateFrom())
        .as("Проверка даты начала действия")
        .isEqualTo(StringUtils.substringBefore(expected.getDateFrom(), "Z"));
    softly
        .assertThat(this.getDateTo())
        .as("Проверка даты окончания действия")
        .isEqualTo(StringUtils.substringBefore(expected.getDateTo(), "Z"));
    softly
        .assertThat(this.getDevice())
        .as("Проверка девайса")
        .isEqualTo(expected.getDevice());
    softly
        .assertThat(this.getVersion())
        .as("Проверка версии")
        .isEqualTo(expected.getVersion());
    softly
        .assertThat(this.getExperimentOptionName())
        .as("Проверка наименования эксперимента")
        .isEqualTo(expected.getExperimentOptionName());
    softly
        .assertThat(this.isDefaultWidget())
        .as("Проверка признака использования по-умолчанию")
        .isEqualTo(expected.isDefaultWidget());
    softly
        .assertThat(this.isEnabled())
        .as("Проверка признака видимости")
        .isEqualTo(expected.isEnabled());
    softly
        .assertThat(this.getLocalization())
        .as("Проверка локали")
        .isEqualTo(expected.getLocalization());
    softly
        .assertThat(this.getUid())
        .as("Проверка UID")
        .isEqualTo(expected.getUid());
    softly.assertAll();
    LOGGER.info(String.format("Виджет с UUID '%s' корректен", this.getUid()));
    IntStream.range(0, expectedChildrenCount).forEach(i -> this.getChildrenWidgetsList().get(i)
        .equals(expected.getChildrenWidgetsList().get(i)));
  }
}
