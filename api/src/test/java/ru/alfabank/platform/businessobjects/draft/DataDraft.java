package ru.alfabank.platform.businessobjects.draft;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import ru.alfabank.platform.businessobjects.Device;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Method;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude (JsonInclude.Include.NON_NULL)
public final class DataDraft {

  private boolean enable;
  private String dateFrom;
  private String dateTo;
  private Device device;
  private String localization;
  private String state;
  private String name;
  private String propertyUid;
  private String widgetUid;
  private String[] cityGroups;
  private Object value;
  private Object[] childUids;
  private Entity entity;
  private Method method;

  @JsonCreator
  private DataDraft(final DataDraftBuilder builder) {
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
  }

  @JsonIgnoreType
  public static class DataDraftBuilder {

    private boolean enable;
    private String dateFrom;
    private String dateTo;
    private Device device;
    private String localization;
    private String state;
    private String name;
    private String propertyUid;
    private String widgetUid;
    private String[] cityGroups;
    private Object value;
    private Object[] childUids;
    private Entity entity;
    private Method method;

    public DataDraftBuilder enable(boolean enable) {
      this.enable = enable;
      return this;
    }

    public DataDraftBuilder dateFrom(String dateFrom) {
      this.dateFrom = dateFrom;
      return this;
    }

    public DataDraftBuilder dateTo(String dateTo) {
      this.dateTo = dateTo;
      return this;
    }

    public DataDraftBuilder device(Device device) {
      this.device = device;
      return this;
    }

    public DataDraftBuilder localization(String localization) {
      this.localization = localization;
      return this;
    }

    public DataDraftBuilder state(String state) {
      this.state = state;
      return this;
    }

    public DataDraftBuilder name(String name) {
      this.name = name;
      return this;
    }

    public DataDraftBuilder forProperty(String propertyUid) {
      this.propertyUid = propertyUid;
      return this;
    }

    public DataDraftBuilder forWidget(String widgetUid) {
      this.widgetUid = widgetUid;
      return this;
    }

    public DataDraftBuilder cityGroups(String[] cityGroups) {
      this.cityGroups = cityGroups;
      return this;
    }

    public DataDraftBuilder value(Object value) {
      this.value = value;
      return this;
    }

    public DataDraftBuilder childUids(Object[] childUids) {
      this.childUids = childUids;
      return this;
    }

    public DataDraftBuilder entity(Entity entity) {
      this.entity = entity;
      return this;
    }

    public DataDraftBuilder method(Method method) {
      this.method = method;
      return this;
    }

    public DataDraft build() {
      return new DataDraft(this);
    }
  }
}
