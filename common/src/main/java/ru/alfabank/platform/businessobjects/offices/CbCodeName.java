package ru.alfabank.platform.businessobjects.offices;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.TestNGException;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonFormat(shape = OBJECT)
public enum CbCodeName {

  DO("DO", "Дополнительный офис", 1),
  KKO("KKO", "Кредитно-кассовый офис", 2),
  RO("RO", "Представительство", 8),
  BRANCH("BRANCH", "Филиал", 9),
  OO("OO", "Операционный офис", 11),
  OKVKU("OKVKU", "Операционная касса вне кассового узла", null),
  ERR("ERR", "Ошибка", null),
  NO_CODE(null, "Дополнительный офис", null),
  NO_NAME("DO", null, null),
  EMPTY(null, null, null);

  protected static final Logger LOGGER = LogManager.getLogger(CbCodeName.class);

  private String code;
  private String name;
  @JsonIgnore
  private Integer typeID;

  @JsonCreator
  CbCodeName() {}

  @JsonCreator
  CbCodeName(@JsonProperty("code") final String code,
             @JsonProperty("code") final String name) {
    this.code = code;
    this.name = name;
  }

  CbCodeName(String code, String name, Integer typeID) {
    this.code = code;
    this.name = name;
    this.typeID = typeID;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  @JsonIgnore
  public Integer getTypeID() {
    return typeID;
  }

  /**
   * Find value by typeId.
   * @param typeId typeId
   * @return value
   */
  public static CbCodeName findValueByTypeId(Integer typeId) {
    LOGGER.info("Поиск 'statusCB' по 'typeId'=" + typeId);
    return Arrays.stream(CbCodeName.values()).filter(v ->
        v.getTypeID().equals(typeId)).findFirst().orElseThrow(() ->
        new TestNGException(String.format("Несуществующий CbCodeName.typeId: '%s'", typeId)));
  }

  /**
   * Find value by code.
   * @param code code
   * @return value
   */
  @JsonCreator
  public static CbCodeName findValue(@JsonProperty("code") String code) {
    LOGGER.debug(String.format("Поиск 'statusCB' по code=%s", code));
    return Arrays.stream(CbCodeName.values()).filter(v ->
        v.getCode().equals(code)).findFirst().orElse(EMPTY);
  }
}
