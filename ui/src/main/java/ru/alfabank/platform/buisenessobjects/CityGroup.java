package ru.alfabank.platform.buisenessobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties({"size"})
public class CityGroup {

  private static String[] cityGroups;

  public CityGroup(
      @JsonProperty("all_distinct_groups") final String[] value) {
    cityGroups = value;
  }

  public static String[] getCityGroups() {
    return cityGroups;
  }

  /**
   * Getting city-groups.
   * @param strings defined city-group(s)
   * @return city-group(s)
   */
  public static String[] getCityGroup(final String... strings) {
    List<String> cityGroupList = new ArrayList<>(Arrays.stream(cityGroups).filter(cityGroup ->
        (Arrays.stream(strings).collect(Collectors.toList()))
            .contains(cityGroup)).collect(Collectors.toList()));
    int size = cityGroupList.size();
    if (size > 0) {
      return cityGroupList.toArray(new String[size]);
    } else {
      return new String[]{cityGroups[size]};
    }
  }
}
