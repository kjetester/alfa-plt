package ru.alfabank.platform.buisenessobjects.enums;

public enum Device {
  DESKTOP("desktop"),
  MOBILE("mobile"),
  ALL("all");

  private String device;

  Device(String device) {
    this.device = device;
  }

  @Override
  public String toString() {
    return device;
  }
}
