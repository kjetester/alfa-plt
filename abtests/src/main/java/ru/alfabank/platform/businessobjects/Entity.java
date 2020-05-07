package ru.alfabank.platform.businessobjects;

public enum Entity {

  PAGE("page"),
  WIDGET("widget"),
  PROPERTY("property"),
  PROPERTY_VALUE("propertyValue");

  private String entity;

  Entity(String entity) {
    this.entity = entity;
  }

  @Override
  public String toString() {
    return entity.toLowerCase();
  }
}
