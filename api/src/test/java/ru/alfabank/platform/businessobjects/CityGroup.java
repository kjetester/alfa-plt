package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.stream.*;

@JsonIgnoreProperties({"size"})
public class CityGroup {

	private static String[] cityGroups;

	public CityGroup(@JsonProperty("all_distinct_groups") String[] value) {
		cityGroups = value;
	}

	public static String[] getCityGroups() {
		return cityGroups;
	}

	public static String[] getCityGroup(String... strings) {
		List<String> cityGroupList = Arrays.stream(cityGroups).filter(cityGroup ->
			(Arrays.stream(strings).collect(Collectors.toList())).contains(cityGroup)).collect(Collectors.toList());
//		Arrays.stream(cityGroups).forEach(s -> {
//			for (String cityGroup : cityGroups) {
//				for (String string : strings) {
//					if (cityGroup.equalsIgnoreCase(string)) {
//						cityGroupList.add(cityGroup);
//					}
//				}
//			}
//		});
		if (cityGroupList.size() != 0){
			return cityGroupList.toArray(new String[cityGroupList.size()]);
		} else {
			return new String[]{cityGroups[0]};
		}
	}
}
