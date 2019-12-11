package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

@JsonIgnoreProperties({"children"})
public class Widget {

	private String uid;
	private String name;
	private int orderNum;
	private Date dateFrom;
	private Date dateTo;
	private Device device;
	private boolean isEnabled;
	private String localization;
	private List<String> widgetGeo;
	private List<Property> properties;
	private boolean reused;

	@JsonCreator
	public Widget(@JsonProperty("uid") String uid,
	              @JsonProperty("name") String name,
	              @JsonProperty("orderNumber") int orderNumber,
	              @JsonProperty("dateFrom") Date dateFrom,
	              @JsonProperty("dateTo") Date dateTo,
	              @JsonProperty("device") Device device,
	              @JsonProperty("enable") boolean isEnabled,
	              @JsonProperty("localization") String localization,
	              @JsonProperty("widgetGeo") List<String> widgetGeo,
	              @JsonProperty("properties") List<Property> properties,
	              @JsonProperty("reused") boolean reused) {
		this.uid = uid;
		this.name = name;
		this.orderNum = orderNumber;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.device = device;
		this.isEnabled = isEnabled;
		this.localization = localization;
		this.widgetGeo = widgetGeo;
		this.properties = properties;
		this.reused = reused;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public List<String> getWidgetGeo() {
		return widgetGeo;
	}

	public void setWidgetGeo(List<String> widgetGeo) {
		this.widgetGeo = widgetGeo;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public boolean isReused() {
		return reused;
	}

	public void setReused(boolean reused) {
		this.reused = reused;
	}
}
