package ru.alfabank.platform.businessobjects.draft;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import ru.alfabank.platform.businessobjects.Device;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude (JsonInclude.Include.NON_NULL)
public final class DataDraft {

  private Boolean enable;
  private String dateFrom;
  private String dateTo;
  private Device device;
  private String localization;
  private String state;
  private String name;
  private String propertyUid;
  private String widgetUid;
  private List<String> cityGroups;
  private Object value;
  private List<String> childUids;
  private String entity;
  private String method;
  private String version;
  private String experimentOptionName;
  @JsonProperty("defaultWidget")
  private Boolean isDefaultWidget;

  @JsonCreator
  private DataDraft(final Builder builder) {
    this.enable = builder.enable;
    this.dateFrom = builder.dateFrom;
    this.dateTo = builder.dateTo;
    this.device = builder.device;
    this.localization = builder.localization;
    this.state = builder.state;
    this.name = builder.name;
    this.propertyUid = builder.propertyUid;
    this.widgetUid = builder.widgetUid;
    this.cityGroups = builder.cityGroups;
    this.value = builder.value;
    this.childUids = builder.childUids;
    this.entity = builder.entity;
    this.method = builder.method;
    this.version = builder.version;
    this.experimentOptionName = builder.experimentOptionName;
    this.isDefaultWidget = builder.isDefaultWidget;
  }

  @JsonIgnoreType
  public static class Builder {

    private Boolean enable;
    private String dateFrom;
    private String dateTo;
    private Device device;
    private String localization;
    private String state;
    private String name;
    private String propertyUid;
    private String widgetUid;
    private List<String> cityGroups;
    private Object value;
    private List<String> childUids;
    private String entity;
    private String method;
    private String version;
    private String experimentOptionName;
    private Boolean isDefaultWidget;

    public Builder setVersion(String version) {
      this.version = version;
      return this;
    }

    public Builder setExperimentOptionName(String experimentOptionName) {
      this.experimentOptionName = experimentOptionName;
      return this;
    }

    public Builder isDefaultWidget(Boolean isDefaultWidget) {
      this.isDefaultWidget = isDefaultWidget;
      return this;
    }

    public Builder isEnabled(Boolean enable) {
      this.enable = enable;
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

    public Builder setLocalization(String localization) {
      this.localization = localization;
      return this;
    }

    public Builder setState(String state) {
      this.state = state;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder forProperty(String propertyUid) {
      this.propertyUid = propertyUid;
      return this;
    }

    public Builder forWidget(String widgetUid) {
      this.widgetUid = widgetUid;
      return this;
    }

    public Builder setCityGroups(List<String> cityGroups) {
      this.cityGroups = cityGroups;
      return this;
    }

    public Builder setValue(Object value) {
      this.value = value;
      return this;
    }

    public Builder setChildUids(List<String> childUids) {
      this.childUids = childUids;
      return this;
    }

    public Builder setEntity(String entity) {
      this.entity = entity;
      return this;
    }

    public Builder setMethod(String method) {
      this.method = method;
      return this;
    }

    public DataDraft build() {
      return new DataDraft(this);
    }
  }
}
