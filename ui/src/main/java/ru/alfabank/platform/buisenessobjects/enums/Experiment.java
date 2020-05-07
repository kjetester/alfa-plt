package ru.alfabank.platform.buisenessobjects.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Experiment {

  DEFAULT("default"),
  AB("forABtest");

  private String experiment;

  Experiment(String experiment) {
    this.experiment = experiment;
  }

  @Override
  public String toString() {
    return experiment;
  }
}
