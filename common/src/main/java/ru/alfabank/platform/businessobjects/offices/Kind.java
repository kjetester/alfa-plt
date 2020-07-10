package ru.alfabank.platform.businessobjects.offices;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
  ERR_KIND("err"),
  EMPTY_KIND("");

  protected static final Logger LOGGER = LogManager.getLogger(Kind.class);

  public final String value;

  Kind(String value) {
    this.value = value;
  }

  @JsonCreator
  private static Kind findValue(@JsonProperty String kind) {
    LOGGER.debug(String.format("Поиск 'Kind' по kind=%s", kind));
    return Arrays.stream(Kind.values()).filter(k ->
        k.value.equals(kind)).findFirst().orElse(ERR_KIND);
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
