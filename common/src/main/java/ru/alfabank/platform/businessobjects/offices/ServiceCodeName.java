package ru.alfabank.platform.businessobjects.offices;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import org.testng.TestNGException;

public enum ServiceCodeName {

  POINT_ONE("pointone", "Точки продаж первого уровня"),
  POINT_TWO("pointtwo", "Точки продаж второго уровня"),
  SERVICE_OFFICE("serviceoffice", "Точки сервисные"),
  CLIENT_OFFICE("clientoffice", "Клиентский офис"),
  SAFE("safe", "Аренда индивидуальных банковских сейфов"),
  DISABLED("disabled", "Приспособлено для обслуживания людей с ограниченными возможностями"),
  CASH_CHF("cashchf", "Прием и выдача ₣ (CHF)"),
  CASH_GBP("cashgbp", "Прием и выдача £ (GBP)"),
  CASH_MAS("cashmas", "В офисе есть касса"),
  CASH_OP("cashop", "Внесение наличных на счет юридических лиц в отделении для частных лиц"),
  MOMENT_CARD("momentcard", "Моментальный выпуск карт"),
  WIFI("wifi", "В отделении есть бесплатный Wi-Fi"),
  PARTNER("partner", "Оформление кредита «Партнер» (ЮЛ)"),
  OVERDRAFT("overdraft", "Оформление кредита «Овердрафт» (ЮЛ)"),
  OFFICE_MB_IP("officembip", "Обслуживание частных лиц, Малого бизнеса и ИП в одном офисе"),
  PILOT("pilot", "Резервное поле - \"парковка\" выдачи ипотеки (на будущее)"),
  DC("dc", "Выдача дебетовых карт"),
  CC("cc", "Выдача кредитных карт"),
  PIL("pil", "Выдача кредитов наличными"),
  ERR("err", "Ошибка");

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
   * @param code code
   * @return value
   */
  @JsonCreator
  public static ServiceCodeName findValueByCode(final String code) {
    return Arrays.stream(ServiceCodeName.values()).filter(c ->
        c.code.equals(code)).findFirst().orElseThrow(() ->
        new TestNGException(
            String.format("Обнаружен невалидный DepartmentFeature.Code: '%s'", code)));
  }

  /**
   * Find value by name.
   * @param name name
   * @return value
   */
  public static ServiceCodeName findValueByName(final String name) {
    return Arrays.stream(ServiceCodeName.values()).filter(c ->
        c.code.equals(name)).findFirst().orElseThrow(() ->
        new TestNGException(
            String.format("Обнаружен невалидный DepartmentFeature.Description: '%s'", name)));
  }
}
