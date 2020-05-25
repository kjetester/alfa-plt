package ru.alfabank.platform.businessobjects.enums;

public enum Localization {

  RU("RU");

  private final String localization;

  Localization(String localization) {
    this.localization = localization;
  }

  public String getLocalization() {
    return localization;
  }

  @Override
  public String toString() {
    return localization;
  }
}
