package ru.alfabank.platform.businessobjects;

import static ru.alfabank.platform.businessobjects.enums.CopyMethod.COPY;
import static ru.alfabank.platform.businessobjects.enums.CopyMethod.CURRENT;
import static ru.alfabank.platform.businessobjects.enums.CopyMethod.SHARE;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.annotations.NonNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;
import ru.alfabank.platform.businessobjects.enums.Device;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude (JsonInclude.Include.NON_NULL)
public class Widget implements Comparable<Widget> {

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
  private List<Property> properties;
  @JsonProperty("reused")
  private Boolean isReused;
  @JsonProperty("children")
  private List<Widget> childrenWidgetsList;
  @JsonIgnore private LocalDateTime localDateTimeFrom;
  @JsonIgnore private LocalDateTime localDateTimeTo;

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
    this.properties = builder.properties;
    this.isReused = builder.isReused;
    LOGGER.info(this.toString());
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
      @JsonProperty("properties") List<Property> properties,
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
    this.properties = properties;
    this.isReused = isReused;
    Collections.sort(properties);
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

  @JsonIgnore
  public LocalDateTime getLocalDateTimeFrom() {
    return localDateTimeFrom;
  }

  public String getDateFrom() {
    return dateFrom;
  }

  @JsonIgnore
  public LocalDateTime getLocalDateTimeTo() {
    return localDateTimeTo;
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

  public List<Property> getProperties() {
    return properties;
  }

  private Boolean isReused() {
    return isReused;
  }

  @Override
  @JsonIgnore
  public String toString() {
    return String.format("Widget{uid='%s', name='%s', isEnabled='%s', localization='%s',"
            + " device='%s', version= '%s', dateFrom='%s', dateTo='%s', geo='%s',"
            + " orderNumber='%s', childUids='%s', properties='%s', isReused='%s',}",
        uid,
        name,
        isEnabled,
        localization,
        device,
        version,
        dateFrom,
        dateTo,
        geo,
        orderNumber,
        childUids,
        properties,
        isReused);
  }

  /**
   * Compare widgets.
   * @param widget comparing widget
   * @param method copying method
   */
  @JsonIgnore
  public void equals(@NonNull final Widget widget, final CopyMethod method) {
    LOGGER.debug(String.format(
        "Сравнение WIDGETS:\nACTUAL.\t\t%s\nEXPECTED.\t%s",
        widget.toString(),
        this.toString()));
    final var softly = new SoftAssertions();
    final var expectedPropertiesCount = this.getProperties().size();
    final var expectedChildrenCount = this.getChildrenWidgetsList().size();
    softly
        .assertThat(widget.getProperties().size())
        .as("Проверка количества свойств")
        .isEqualTo(expectedPropertiesCount);
    softly
        .assertThat(widget.getChildrenWidgetsList().size())
        .as("Проверка количества дочерних виджетов")
        .isEqualTo(expectedChildrenCount);
    softly
        .assertThat(widget.getName())
        .as("Проверка наименований")
        .isEqualTo(this.getName());
    softly
        .assertThat(widget.getOrderNumber())
        .as("Проверка очередности")
        .isEqualTo(this.getOrderNumber());
    softly
        .assertThat(widget.getDateFrom())
        .as("Проверка даты начала действия")
        .isEqualTo(this.getDateFrom());
    softly
        .assertThat(widget.getDateTo())
        .as("Проверка даты окончания действия")
        .isEqualTo(this.getDateTo());
    softly
        .assertThat(widget.getDevice())
        .as("Проверка девайса")
        .isEqualTo(this.getDevice());
    softly
        .assertThat(widget.getVersion())
        .as("Проверка версии")
        .isEqualTo(this.getVersion());
    softly
        .assertThat(widget.getExperimentOptionName())
        .as("Проверка наименования эксперимента")
        .isEqualTo(this.getExperimentOptionName());
    softly
        .assertThat(widget.isDefaultWidget())
        .as("Проверка признака использования по-умолчанию")
        .isEqualTo(this.isDefaultWidget());
    softly
        .assertThat(widget.isEnabled())
        .as("Проверка признака видимости")
        .isEqualTo(this.isEnabled());
    softly
        .assertThat(widget.getLocalization())
        .as("Проверка локали")
        .isEqualTo(this.getLocalization());
    softly
        .assertThat(widget.getGeo())
        .as("Проверка гео")
        .isEqualTo(this.getGeo());
    if (method.equals(COPY)) {
      softly
          .assertThat(widget.isReused())
          .as("Проверка признака переиспользования при 'COPY'")
          .isFalse();
    } else {
      softly.assertThat(widget.isReused())
          .as("Проверка признака переиспользования при 'SHARE' или 'CURRENT'")
          .isEqualTo(this.isReused());
    }
    if (method.equals(SHARE) || (method.equals(CURRENT) && this.isReused())) {
      softly
          .assertThat(widget.getUid())
          .as("Проверка UID при 'SHARE' или 'CURRENT' и признаке переиспользования")
          .isEqualTo(this.getUid());
    } else {
      softly
          .assertThat(widget.getUid())
          .as("Проверка UID при 'COPY'")
          .isNotEqualTo(this.getUid());
    }
    softly.assertAll();
    IntStream.range(0, expectedChildrenCount).forEach(i -> this.getChildrenWidgetsList().get(i)
        .equals(widget.getChildrenWidgetsList().get(i), method));
    IntStream.range(0, expectedPropertiesCount).forEach(i -> this.getProperties().get(i)
        .equals(widget.getProperties().get(i), method, this.isReused()));
  }

  @Override
  public int compareTo(@NotNull Widget widget) {
    return this.getName().compareTo(widget.getName());
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
    private List<Property> properties;
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

    public Builder setProperties(List<Property> properties) {
      this.properties = properties;
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
      this.properties = widget.properties;
      this.isReused = widget.isReused;
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
}
