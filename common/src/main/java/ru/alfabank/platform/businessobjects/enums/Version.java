package ru.alfabank.platform.businessobjects.enums;

public enum Version {

  V_1_0_0("1.0.0");

  private String version;

  Version(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return version;
  }
}
