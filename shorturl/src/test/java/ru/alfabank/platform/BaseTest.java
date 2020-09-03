package ru.alfabank.platform;

import static ru.alfabank.platform.steps.BaseSteps.LIST_OF_CREATED_ENTITIES;

import org.testng.annotations.AfterMethod;
import ru.alfabank.platform.steps.shorturl.ShortUrlSteps;

public class BaseTest {

  protected static final ShortUrlSteps STEP = new ShortUrlSteps();
  protected static final String TIME_ZONE_OFFSET = "Z";

  @AfterMethod
  public static void cleanUp() {
    STEP.deleteAllCreatedEntities(LIST_OF_CREATED_ENTITIES);
  }
}
