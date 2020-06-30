package ru.alfabank.platform.businessobjects.enums;

public enum GeoGroupType {

  MUTABLE("mutable"),
  IMMUTABLE("immutable");

  private final String type;

  GeoGroupType(final String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
