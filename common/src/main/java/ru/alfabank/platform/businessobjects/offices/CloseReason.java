package ru.alfabank.platform.businessobjects.offices;

public enum CloseReason {

  RENOVATION("Реконструкция"),
  REPAIR("Ремонт"),
  MOVING("Переезд"),
  ERR("Ошибка");

  private final String value;

  CloseReason(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
