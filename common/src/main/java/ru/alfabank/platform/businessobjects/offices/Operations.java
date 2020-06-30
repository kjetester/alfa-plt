package ru.alfabank.platform.businessobjects.offices;

public enum Operations {

  UPDATE("update"),
  ERR("err");

  private final String value;

  Operations(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
