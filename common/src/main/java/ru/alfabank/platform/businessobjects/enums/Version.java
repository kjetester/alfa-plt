package ru.alfabank.platform.businessobjects.enums;

public enum Version {

  V_1_0_0("v_1.0.0");

  private final String version;

  Version(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  @Override
  public String toString() {
    return version;
  }
}
