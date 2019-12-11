package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties({"size"})
public class CityGroup {

	private static String[] allDistinctGroups;

	public CityGroup(@JsonProperty("all_distinct_groups") String[] value) {
		allDistinctGroups = value;
	}

	public static String[] getCityGroups() {
		return allDistinctGroups;
	}
}
