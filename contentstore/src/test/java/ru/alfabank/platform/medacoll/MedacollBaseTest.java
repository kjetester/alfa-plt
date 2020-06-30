package ru.alfabank.platform.medacoll;

import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL;
import static ru.alfabank.platform.businessobjects.enums.Team.SME;
import static ru.alfabank.platform.helpers.UuidHelper.getShortRandUuid;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.contentstore.Page;

public class MedacollBaseTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(MedacollBaseTest.class);

  protected static Page basePage;
  protected static Page sourcePage;
  protected static String baseUri;

  /**
   * Prepare environment before a test.
   */
  @BeforeSuite(description = "Создание объекта страницы-донора\n", alwaysRun = true)
  public void beforeSuite() {
    setUpSourcePage();
  }

  @BeforeMethod(description = """
      Выполенние предусловий:
      \t1. Создание базового объекта создоваемой страницы
      """)
  public void beforeCreationPageMethods() {
    setUpBasePage();
  }

  /**
   * Set up the Base Page.
   */
  private void setUpBasePage() {
    String pageUrl = getShortRandUuid();
    LOGGER.info("Собираю POJO базы новой страницы");
    basePage = new Page.Builder()
        .setUri(pageUrl)
        .setTitle("title_" + pageUrl)
        .setDescription("description_" + pageUrl)
        .setDateFrom(LocalDateTime.now().toString())
        .setDateTo(LocalDateTime.now().plusMinutes(30).toString())
        .setEnable(true)
        .setTeamsList(List.of(
            CREDIT_CARD,
            DEBIT_CARD,
            INVEST,
            MORTGAGE,
            PIL,
            SME,
            COMMON))
        .build();
    LOGGER.info(String.format("Скелет новой страницы:\n\t %s", basePage.toString()));
  }

  /**
   * Set up the Source Page.
   */
  private void setUpSourcePage() {
    LOGGER.info("Собираю pojo страницы-источника");
    sourcePage = new Page.Builder()
        .setUri("/about/")
        .setId(10)
        .setTitle("null")
        .setDescription("null")
        .setDateFrom(null)
        .setDateTo(null)
        .setEnable(true)
        .setChildUids(null)
        .build();
    LOGGER.info(String.format("Страница-источник:\n\t %s", sourcePage.toString()));
  }
}
