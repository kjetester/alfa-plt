package ru.alfabank.platform.businessobjects.geofacade;

import java.util.List;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class FiasList extends AbstractBusinessObject {

  private final List<String> fiasCodeList;

  public FiasList(Builder builder) {
    this.fiasCodeList = builder.fiasCodeList;
  }

  public List<String> getFiasCodeList() {
    return fiasCodeList;
  }

  public static class Builder {

    private List<String> fiasCodeList;

    public Builder setFiasCodeList(List<String> fiasCodeList) {
      this.fiasCodeList = fiasCodeList;
      return this;
    }
  }
}
