package ru.alfabank.platform.businessobjects.enums;

public enum Geo {

  BEZ_MSK_MO("bez_msk_mo"),
  MSK_MO("mskmo"),
  RU("ru"),
  VLADIMIR("vladimir");

  private final String geo;

  public String getGeo() {
    return geo;
  }

  Geo(String geo) {
    this.geo = geo;
  }

  @Override
  public String toString() {
    return geo;
  }
}
