package ru.alfabank.platform;

import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL;
import static ru.alfabank.platform.businessobjects.enums.Team.SME;
import static ru.alfabank.platform.helpers.UuidHelper.getShortRandUuid;

import com.epam.reportportal.annotations.ParameterKey;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import ru.alfabank.platform.businessobjects.Page;

public class BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);
  private static final String URL_ENDING = ".ci.k8s.alfa.link/";

  protected static Page basePage;
  protected static String baseUri;

  /**
   * Prepare environment before a test.
   * @param environment environment
   */
  @BeforeSuite(
      description = "Выполенние предусловий:\n"
          + "\t1. Установка тествой среды\n"
          + "\t2. Создание объекта страницы-донора\n"
          + "\t5. Настройка базовой конфигурации HTTP запросов\n"
          + "\t6. Настройка конфигурации HTTP запросов к page-controller\n"
          + "\t7. Настройка конфигурации HTTP запросов к content-page-controller\n"
          + "\t8. Настройка конфигурации HTTP запросов к page-draft-controller",
      alwaysRun = true)
  @Parameters({"environment"})
  public void beforeSuite(
      @Optional("develop")
      @ParameterKey("environment")
      final String environment) {
    setUpEnvironment(environment);
  }

  @BeforeMethod(
      description = "Выполенние предусловий:\n"
          + "\t3. Создание базового объекта создоваемой страницы\n")
  public void beforeCreationPageMethods() {
    setUpBasePage();
  }

  /**
   * Set up environment.
   * @param environment environment
   */
  private void setUpEnvironment(
      @ParameterKey("environment")
      @Optional("develop") String environment) {
    LOGGER.info(String.format("Тестовая среда - '%s'", environment));

    switch (environment) {
      case "develop": {
        baseUri = String.format("http://develop%s", URL_ENDING);
        break;
      }
      case "preprod": {
        baseUri = String.format("http://preprod%s", URL_ENDING);
        break;
      }
      case "feature": {
        baseUri = String.format(
            "http://acms-feature-alfabankru-%s.alfabankru-reviews%s", System.getProperty("feature"), URL_ENDING);
        break;
      }
      default: {
        baseUri = "http://develop" + URL_ENDING;
        break;
      }
    }
    LOGGER.info(String.format("URI '%s' установлен в качестве базового", baseUri));
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
}
