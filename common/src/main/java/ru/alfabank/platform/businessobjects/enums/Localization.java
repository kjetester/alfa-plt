package ru.alfabank.platform.businessobjects.enums;

public enum Localization {

  RU("RU");

  private String localization;

  Localization(String localization) {
    this.localization = localization;
  }

  @Override
  public String toString() {
    return localization;
  }
}
