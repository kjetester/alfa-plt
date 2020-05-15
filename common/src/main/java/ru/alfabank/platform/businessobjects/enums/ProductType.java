package ru.alfabank.platform.businessobjects.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.testng.TestNGException;

public enum ProductType {

  CC("Кредитные карты", "CC"),
  PIL("Кредиты наличными", "PIL"),
  DC("Дебетовые карты", "DC"),
  MG("Ипотека", "MG"),
  SME("Массовый бизнес", "SME"),
  INV("Инвестиции", "INV"),
  COM("Общие страницы и сервисы", "COM"),
  ERR("Негатив", "ERR");

  String description;
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

  /**
   * Get random Product Type except ERR.
   * @return ProductType
   */
  public static ProductType getRandomProductType() {
    final var values = List.of(values());
    ProductType result;
    do {
      result = values.get(new Random().nextInt(values.size()));
    } while (result == ERR);
    return result;
  }
}
