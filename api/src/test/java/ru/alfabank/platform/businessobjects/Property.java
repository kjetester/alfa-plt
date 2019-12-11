package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Property {

	private String uid;
	private String name;
	private Device device;
	private List<Value> values;

	@JsonCreator
	public Property(
			@JsonProperty("uid") String uid,
			@JsonProperty("name") String name,
			@JsonProperty("device") Device device,
			@JsonProperty("values") List<Value> values) {
		this.uid = uid;
		this.name = name;
		this.device = device;
		this.values = values;
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

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}
}
