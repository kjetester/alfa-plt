package ru.alfabank.platform.businessobjects.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.testng.TestNGException;

public enum ProductType {

  CREDIT_CARD_PRODUCT_TYPE("Кредитные карты", "CreditCard"),
  PIL_PRODUCT_TYPE("Кредиты наличными", "PIL"),
  DEBIT_CARD_PRODUCT_TYPE("Дебетовые карты", "DebitCard"),
  MORTGAGE_PRODUCT_TYPE("Ипотека", "Mortgage"),
  SME_PRODUCT_TYPE("Массовый бизнес", "SME"),
  INVEST_PRODUCT_TYPE("Инвестиции", "Invest"),
  COMMON_PRODUCT_TYPE("Общие страницы и сервисы", "Common"),
  UNLISTED_PRODUCT_TYPE("Продукт не из списка", "Unlisted");

  String description;
  @JsonValue
  String key;

  ProductType(String description, String key) {
    this.description = description;
    this.key = key;
  }

  @JsonCreator
  static ProductType findValue(@JsonProperty String key) {
    return Arrays.stream(ProductType.values()).filter(v ->
        v.key.equals(key)).findFirst().orElseThrow(() ->
        new TestNGException(String.format(
            "Обнаружен невалидный productTypeKey: '%s'",
            key)));
  }
}
