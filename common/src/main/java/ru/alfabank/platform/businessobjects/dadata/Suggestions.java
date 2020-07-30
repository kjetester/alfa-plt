package ru.alfabank.platform.businessobjects.dadata;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class Suggestions extends AbstractBusinessObject {

  private List<Suggestion> suggestions;

  @JsonAnyGetter
  public List<Suggestion> getSuggestions() {
    return suggestions;
  }

  public static class Suggestion extends AbstractBusinessObject {

    private Data data;

    @JsonAnyGetter
    public Data getData() {
      return data;
    }

    public static class Data extends AbstractBusinessObject {

      @JsonProperty("area_fias_id")
      private String areaFiasId;
      @JsonProperty("city_fias_id")
      private String cityFiasId;
      @JsonProperty("settlement_fias_id")
      private String settlementFiasId;

      @JsonAnyGetter
      public String getAreaFiasId() {
        return areaFiasId;
      }

      @JsonAnyGetter
      public String getCityFiasId() {
        return cityFiasId;
      }

      @JsonAnyGetter
      public String getSettlementFiasId() {
        return settlementFiasId;
      }
    }
  }
}