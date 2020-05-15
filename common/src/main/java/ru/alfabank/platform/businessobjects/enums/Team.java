package ru.alfabank.platform.businessobjects.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import org.testng.TestNGException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(Include.NON_NULL)
public enum Team {

  CREDIT_CARD(1, "CreditCard", "Кредитные карты", ProductType.CC),
  DEBIT_CARD(2, "DebitCard", "Дебетовые карты", ProductType.DC),
  INVEST(3, "Invest", "Инвестиции", ProductType.INV),
  MORTGAGE(4, "Mortgage", "Ипотека", ProductType.MG),
  PIL(5, "PIL", "Кредиты наличными", ProductType.PIL),
  SME(6, "SME", "Массовый бизнес", ProductType.SME),
  COMMON(7, "Common", "Общие страницы и сервисы", ProductType.COM),
  UNCLAIMED(null, "Unclaimed", "Команда Ничьих Страниц", null);

  private final Integer id;
  private final String code;
  private final String name;
  private final ProductType productType;

  Team(Integer id, String code, String name, ProductType productType) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.productType = productType;
  }

  @JsonValue
  public Integer getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public ProductType getProductType() {
    return productType;
  }

  @JsonCreator
  static Team findValue(@JsonProperty Integer id) {
    return Arrays.stream(Team.values()).filter(v ->
        v.id == id).findFirst().orElseThrow(() ->
        new TestNGException(String.format("Обнаружен невалидный teamId: '%s'", id)));
  }

  @Override
  public String toString() {
    return name;
  }
}
