package ru.alfabank.platform;

import static ru.alfabank.platform.helpers.KeycloakHelper.logoutAllUsers;

import org.testng.annotations.AfterSuite;
import ru.alfabank.platform.steps.cs.DraftSteps;
import ru.alfabank.platform.steps.cs.PagesSteps;
import ru.alfabank.platform.steps.geofacade.CitiesSteps;
import ru.alfabank.platform.steps.geofacade.GeoSteps;

public class BaseTest {

  protected static final CitiesSteps CITIES_STEPS = new CitiesSteps();
  protected static final GeoSteps GEO_STEPS = new GeoSteps();
  protected static final PagesSteps PAGES_STEPS = new PagesSteps();
  protected static final DraftSteps DRAFT_STEPS = new DraftSteps();

  /**
   * Clean up.
   */
  @AfterSuite(description = """
      Выполнение постусловий:
      \t1. Удаление всех созданных гео-групп
      \t2. Разлогин всех пользователей""")
  public void afterSuite() {
    PAGES_STEPS.deleteAllCreatedPages();
    GEO_STEPS.deleteAllCreatedGeoGroups();
    logoutAllUsers();
  }
}
