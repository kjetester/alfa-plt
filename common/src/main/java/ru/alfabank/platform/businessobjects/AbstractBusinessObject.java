package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.log4j.Logger;

public abstract class AbstractBusinessObject {

  /**
   * POJO to pretty JSON.
   *
   * @param o object
   * @return json as string
   */
  public static synchronized String describeBusinessObject(final Object o) {
    try {
      return new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .writerWithDefaultPrettyPrinter().writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Log comparing objects.
   *
   * @param logger         logger
   * @param actualObject   actualObject
   * @param expectedObject expectedObject
   */
  protected void logComparingObjects(final Logger logger,
                                     final Object actualObject,
                                     final Object expectedObject) {
    logger.debug(String.format("Сравнение объектов:\nACTUAL.\t\t%s\nEXPECTED.\t%s",
        describeBusinessObject(actualObject),
        describeBusinessObject(expectedObject)));
  }

  protected void logComparingResult(final Logger logger, final String value) {
    logger.info(String.format("Объект с uuid'%s' корректен", value));
  }
}
