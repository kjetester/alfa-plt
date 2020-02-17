package ru.alfabank.platform.helpers;

import org.apache.log4j.*;

import java.util.*;

public class UUIDHelper {

  private static final Logger LOGGER = LogManager.getLogger(UUIDHelper.class);

  /**
   * Generates a new UID.
   * @return new UID
   */
  public static String getNewUuid() {
    String uuid = UUID.randomUUID().toString().replace("-", "");
    LOGGER.debug(String.format("Сгенерирован новый uuid: '%s'", uuid));
    return uuid;
  }

  /**
   * Generates a new UID.
   * @return new short random UID
   */
  public static String getShortRandUuid() {
    String uuid = UUID.randomUUID().toString().substring(24).toLowerCase();
    LOGGER.debug(String.format("Сгенерирован новый короткий uuid: '%s'", uuid));
    return uuid;
  }

}
