package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Page {

	private String id;
	private String uri;
	private String title;
	private String description;
	private boolean isEnabled;
	private Date dateFrom;
	private Date dateTo;

	@JsonCreator
	public Page(@JsonProperty("id") String id,
	            @JsonProperty("uri") String uuid,
	            @JsonProperty("title") String title,
	            @JsonProperty("description") String description,
	            @JsonProperty("enable") boolean isEnabled,
	            @JsonProperty("dataFrom") Date dateFrom,
	            @JsonProperty("dataTo") Date dateTo) {
		this.id = id;
		this.uri = uuid;
		this.title = title;
		this.description = description;
		this.isEnabled = isEnabled;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public String getId() {
		return id;
	}

	public Page setId(String id) {
		this.id = id;
		return this;
	}

	public String getUri() {
		return uri;
	}

	public Page setUri(String uri) {
		this.uri = uri;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Page setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Page setDescription(String description) {
		this.description = description;
		return this;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public Page setEnabled(boolean enabled) {
		isEnabled = enabled;
		return this;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Page setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public Page setDateTo(Date dateTo) {
		this.dateTo = dateTo;
		return this;
	}
}
