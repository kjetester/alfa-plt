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

  RETAIL_STANDARD_KIND("retailStandart"),
  RETAIL_VIP_KIND("retailVip"),
  VIP_KIND("vip"),
  RETAIL_CIK_KIND("retailCIK"),
  MMB_KIND("MMB"),
  SB_KIND("SB"),
  CIB_KIND("CIB"),
  NEW_KIND("new"),
  RETAIL_ACLUB_KIND("retailAclub"),
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

    VIP_CODE("vip"),
    A55_CODE("a55"),
    VIPMNGR_CODE("vipmngr"),
    VIP_CLIENT("vip-client"),
    RETAIL_CODE("retail"),
    MORTGAGE_CODE("mortgage"),
    SME_CODE("sme"),
    CORPORATE_CODE("corporate"),
    ACLUB_CODE("aclub");

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
     *
     * @param code code
     * @return value
     */
    @JsonCreator
    public static Code findValue(final String code) {
      return Arrays.stream(Code.values()).filter(c ->
          c.value.equals(code)).findFirst().orElseThrow(() ->
          new TestNGException(String.format(
              "Обнаружен невалидный (kind.code) DepartmentFeature.Code: '%s'",
              code)));
    }
  }
}
