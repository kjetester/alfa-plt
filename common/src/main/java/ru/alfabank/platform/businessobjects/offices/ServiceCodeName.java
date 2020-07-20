package ru.alfabank.platform.businessobjects.offices;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import org.testng.TestNGException;

public enum ServiceCodeName {

  POINT_ONE_SERVICE("pointone", "Точки продаж первого уровня"),
  POINT_TWO_SERVICE("pointtwo", "Точки продаж второго уровня"),
  SERVICE_OFFICE_SERVICE("serviceoffice", "Точки сервисные"),
  CLIENT_OFFICE_SERVICE("clientoffice", "Клиентский офис"),
  CASH_MAS_SERVICE("cashmas", "В офисе есть касса"),
  SAFE_SERVICE("safe", "Аренда индивидуальных банковских сейфов"),
  DISABLED_SERVICE("disabled", "Приспособлено для обслуживания людей с ограниченными возможностями"),
  CASH_GBP_SERVICE("cashgbp", "Прием и выдача £ (GBP)"),
  CASH_CHF_SERVICE("cashchf", "Прием и выдача ₣ (CHF)"),
  MOMENT_CARD_SERVICE("momentcard", "Моментальный выпуск карт"),
  WIFI_SERVICE("wifi", "В отделении есть бесплатный Wi-Fi"),
  PARTNER_SERVICE("partner", "Оформление кредита «Партнер» (ЮЛ)"),
  OVERDRAFT_SERVICE("overdraft", "Оформление кредита «Овердрафт» (ЮЛ)"),
  CASH_OP_SERVICE("cashop", "Внесение наличных на счет юридических лиц в отделении для частных лиц"),
  OFFICE_MB_IP_SERVICE("officembip", "Обслуживание частных лиц, Малого бизнеса и ИП в одном офисе"),
  MORTGAGE_SERVICE("mortgage", "Оформление ипотеки"),
  DC_SERVICE("dc", "Выдача дебетовых карт"),
  CC_SERVICE("cc", "Выдача кредитных карт"),
  PIL_SERVICE("pil", "Выдача кредитов наличными"),
  INVESTMENT_SERVICE("investment", "Оформление инвестиционных продуктов"),
  SHARE_SERVICE("share", "Агентский пункт по размещению и выкупу паев"),
  COURIER_SERVICE("courier", "Услуга «Альфа-курьер»"),
  ERR_SERVICE("err", "Ошибка");

  private final String code;
  private final String name;

  ServiceCodeName(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  /**
   * Find value by code.
   *
   * @param code code
   * @return value
   */
  @JsonCreator
  public static ServiceCodeName findValueByCode(final String code) {
    return Arrays.stream(ServiceCodeName.values()).filter(c ->
        c.code.equals(code)).findFirst().orElseThrow(() ->
        new TestNGException(String.format(
            "Обнаружен невалидный (service.code) DepartmentFeature.Code: '%s'",
            code)));
  }

  /**
   * Find value by name.
   *
   * @param name name
   * @return value
   */
  public static ServiceCodeName findValueByName(final String name) {
    return Arrays.stream(ServiceCodeName.values()).filter(c ->
        c.code.equals(name)).findFirst().orElseThrow(() ->
        new TestNGException(String.format(
            "Обнаружен невалидный  (service.description) DepartmentFeature.Description: '%s'",
            name)));
  }
}
