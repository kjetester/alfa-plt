package ru.alfabank.platform.businessobjects.offices;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.List;
import org.testng.TestNGException;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonFormat(shape = STRING)
public enum Kind {

  RETAIL_STANDARD("retailStandart"),
  RETAIL_VIP("retailVip"),
  VIP("vip"),
  RETAIL_CIK("retailCIK"),
  MMB("MMB"),
  SB("SB"),
  CIB("CIB"),
  NEW("new"),
  RETAIL_ACLUB("retailAclub"),
  ERR("err");

  public final String value;

  Kind(String value) {
    this.value = value;
  }

  @JsonCreator
  static Kind findValue(final String kind) {
    return Arrays.stream(Kind.values()).filter(k ->
        k.value.equals(kind)).findFirst().orElseThrow(() ->
        new TestNGException(String.format("Обнаружен невалидный Kind: '%s'", kind)));
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public enum Code {

    VIP("vip"),
    VIPMNGR("vipmngr"),
    VIP_CLIENT("vip-client"),
    RETAIL("retail"),
    MORTGAGE("mortgage"),
    SME("sme"),
    CORPORATE("corporate"),
    ACLUB("aclub");

    String value;

    Code(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    /**
     * Find value by code.
     * @param code code
     * @return value
     */
    @JsonCreator
    public static Code findValue(final String code) {
      return Arrays.stream(Code.values()).filter(c ->
          c.value.equals(code)).findFirst().orElseThrow(() ->
          new TestNGException(
              String.format("Обнаружен невалидный DepartmentFeature.Code: '%s'", code)));
    }
  }
}
