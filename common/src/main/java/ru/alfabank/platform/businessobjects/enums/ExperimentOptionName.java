package ru.alfabank.platform.businessobjects.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import org.testng.TestNGException;

public enum ExperimentOptionName {

  FOR_AB_TEST("forABtest"),
  DEFAULT("default");

  private final String experimentOptionName;

  ExperimentOptionName(String experimentOptionName) {
    this.experimentOptionName = experimentOptionName;
  }

  @Override
  public String toString() {
    return experimentOptionName;
  }

  @JsonCreator
  static ExperimentOptionName findValue(@JsonProperty String experimentOptionName) {
    return Arrays.stream(ExperimentOptionName.values()).filter(v ->
        v.experimentOptionName.equals(experimentOptionName)).findFirst().orElseThrow(() ->
        new TestNGException(String.format(
            "Обнаружен невалидный experimentOptionName: '%s'",
            experimentOptionName)));
  }
}
