package ru.alfabank.platform.businessobjects.enums;

public enum Experiment {

  DEFAULT("default"),
  AB("forABtest");

  private final String experiment;

  Experiment(String experiment) {
    this.experiment = experiment;
  }

  @Override
  public String toString() {
    return experiment;
  }
}
