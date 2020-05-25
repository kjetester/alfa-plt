package ru.alfabank.platform.businessobjects.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import org.testng.TestNGException;

public enum Device {
  desktop("desktop"),
  mobile("mobile"),
  all("all");

  private final String device;

  Device(String device) {
    this.device = device;
  }

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  @JsonCreator
  static Device findValue(@JsonProperty String device) {
    return Arrays.stream(Device.values()).filter(v ->
        v.device.equals(device)).findFirst().orElseThrow(() ->
        new TestNGException(String.format(
            "Обнаружен невалидный device: '%s'",
            device)));
  }
}
