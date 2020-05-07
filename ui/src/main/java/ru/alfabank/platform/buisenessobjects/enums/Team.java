package ru.alfabank.platform.buisenessobjects.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import org.testng.TestNGException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public enum Team {

  CREDIT_CARD(1, "CreditCard", "Кредитные карты"),
  DEBIT_CARD(2, "DebitCard", "Дебетовые карты"),
  INVEST(3, "Invest", "Инвестиции"),
  MORTGAGE(4, "Mortgage", "Ипотека"),
  PIL(5, "PIL", "Кредиты наличными"),
  SME(6, "SME", "Массовый бизнес"),
  COMMON(7, "Common", "Общие страницы и сервисы");

  private int id;
  private String code;
  private String name;

  Team(int id, String code, String name) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  @JsonCreator
  static Team findValue(@JsonProperty int id) {
    return Arrays.stream(Team.values()).filter(v ->
        v.id == id).findFirst().orElseThrow(() ->
        new TestNGException(String.format("Обнаружен невалидный teamId: '%s'", id)));
  }

  @Override
  public String toString() {
    return name;
  }
}
