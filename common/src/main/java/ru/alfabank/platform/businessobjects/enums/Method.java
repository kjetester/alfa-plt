package ru.alfabank.platform.businessobjects.enums;

public enum Method {
  CREATE("create"),
  CHANGE("change"),
  CHANGE_LINKS("changeLinks"),
  DELETE("delete");

  private String method;

  Method(String method) {
    this.method = method;
  }

  @Override
  public String toString() {
    return method;
  }
}
