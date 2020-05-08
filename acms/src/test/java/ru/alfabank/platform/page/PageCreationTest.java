package ru.alfabank.platform.page;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Entity.PAGE;
import static ru.alfabank.platform.businessobjects.enums.Entity.WIDGET;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.Method.CHANGE_LINKS;
import static ru.alfabank.platform.businessobjects.enums.Method.CREATE;
import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL;
import static ru.alfabank.platform.businessobjects.enums.Team.SME;
import static ru.alfabank.platform.businessobjects.enums.Version.V_1_0_0;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.KeycloakHelper.getToken;
import static ru.alfabank.platform.helpers.UuidHelper.getNewUuid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.draft.DataDraft;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.Experiment;
import ru.alfabank.platform.businessobjects.enums.Geo;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.utils.TestFailureListener;

@Listeners({TestFailureListener.class})
public class PageCreationTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(PageCreationTest.class);

  @Test(
      description = "Тест создания страницы:\n"
          + "\t1. В корне\n"
          + "\t2. Все поля заполнены\n"
          + "\t3. Активна\n"
          + "\t4. Виджетов нет",
      groups = {"page", "creationPage"})
  public void activePageWithNoWidgetsCreationTest() {
    // STEPS //
    Page page =
        PageFactory.initElements(getDriver(), MainPage.class)
            .openAndAuthorize(BaseTest.baseUri, BaseTest.USER)
            .openPagesTree()
            .createNewPageWithinPage(null)
            .fillAndSubmitCreationForm(
                new Page.Builder()
                    .using(basePage)
                    .setDateTo(null)
                    .setTeamsList(List.of(
                        SME,
                        COMMON,
                        CREDIT_CARD,
                        DEBIT_CARD,
                        INVEST, MORTGAGE,
                        PIL))
                    .build());
    createdPages.put(page.getUri(), page);
    // CHECKS //
    PageFactory.initElements(getDriver(), MainPage.class)
        .checkPageOpened(page.getUri());
    checkCreatedPageAtPageController(page);
    checkCreatedPageAtContentPageController(page);
  }

  @Test(
      description = "Тест создания страницы:\n"
          + "\t1. В корне страницы '/qr/'\n"
          + "\t2. Все поля заполнены\n"
          + "\t3. Неактивна\n"
          + "\t4. Виджетов нет",
      groups = {"page", "creationPage"},
      priority = 1)
  public void inactivePageWithNoWidgetsCreationTest() {
    String parentPage = "qr";
    Page page = new Page.Builder()
        .using(basePage)
        .setDateFrom(null)
        .setDateTo(null)
        .setEnable(false)
        .build();
    // STEPS //
    page = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(BaseTest.baseUri, BaseTest.USER)
        .openPagesTree()
        .createNewPageWithinPage(parentPage)
        .fillAndSubmitCreationForm(page);
    createdPages.put(page.getUri(), page);
    // CHECKS //
    checkCreatedPageAtPageController(page);
    checkCreatedPageAtContentPageController(page);
  }

  @Test(
      description = "Тест создания страницы:\n"
          + "\t1. В корне\n"
          + "\t2. Все поля заполнены\n"
          + "\t3. Активна\n"
          + "\t4. Виджеты есть",
      groups = {"page", "creationPage"},
      priority = 2)
  public void activePageWithWidgetsCreationTest() throws JsonProcessingException {
    // STEPS //
    Page page = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(BaseTest.baseUri, BaseTest.USER)
          .openPagesTree()
          .createNewPageWithinPage(null)
          .fillAndSubmitCreationForm(
              new Page.Builder()
                  .using(BaseTest.basePage)
                  .setDateFrom(null)
                   .setTeamsList(List.of(
                       INVEST,
                       MORTGAGE))
                  .build());
    // создание виджета A
    Widget widgetA = new Widget.Builder()
        .setUid(getNewUuid())
        .setName("MetaTitle")
        .setDevice(desktop)
        .setLocalization(RU.toString())
        .isEnabled(true)
        .setVersion(V_1_0_0.toString())
        .setExperimentOptionName(Experiment.DEFAULT.toString())
        .isDefaultWidget(true)
        .build();
    WrapperDraft.OperationDraft widgetCreationOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setName(widgetA.getName())
            .setDevice(widgetA.getDevice())
            .setLocalization(widgetA.getLocalization())
            .isEnabled(widgetA.isEnabled())
            .setVersion(widgetA.getVersion())
            .setExperimentOptionName(widgetA.getExperimentOptionName())
            .isDefaultWidget(widgetA.isDefaultWidget())
            .setCityGroups(Collections.singletonList(Geo.RU.toString()))
            .build(),
        WIDGET.toString(), CREATE.toString(), widgetA.getUid());
    WrapperDraft.OperationDraft widgetPlacementOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setChildUids(Collections.singletonList(widgetA.getUid()))
            .build(),
        PAGE.toString(),
        CHANGE_LINKS.toString(),
        page.getId());
    List<Object> operations = List.of(widgetCreationOperation, widgetPlacementOperation);
    WrapperDraft draft = new WrapperDraft(operations, desktop);
    LOGGER.info(String.format(
        "Сохранение черновика\n%s",
        new ObjectMapper().writeValueAsString(draft)));
    Response savingDraftResponse =
        given()
            .spec(pageDraftControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", page.getId())
            .body(draft)
        .when().put()
        .then().extract().response();
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s",
        savingDraftResponse.getStatusLine(),
        savingDraftResponse.prettyPrint()));
    LOGGER.info("Публикация черновика");
    Response publishDraftResponse =
        given()
            .spec(pageDraftControllerPublishSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", page.getId())
            .queryParams("device", desktop)
        .when().post()
        .then().extract().response();
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s",
        publishDraftResponse.statusLine(),
        publishDraftResponse.prettyPrint()));
    page = new Page.Builder()
        .using(page)
        .setWidgets(Collections.singletonList(widgetA))
        .build();
    createdPages.put(page.getUri(), page);
    getDriver().navigate().refresh();
    // CHECKS //
    checkCreatedPageAtPageController(page);
    checkCreatedPageAtContentPageController(page);
  }

  @Test(
      description = "Тест создания страницы:\n"
          + "\t1. В корне\n"
          + "\t2. Все поля заполнены\n"
          + "\t3. Неактивна\n"
          + "\t4. Виджеты есть",
      groups = {"page", "creationPage"},
      priority = 3)
  public void inactivePageWithWidgetsCreationTest() throws JsonProcessingException {
    // STEPS //
    Page page =
        PageFactory.initElements(getDriver(), MainPage.class)
            .openAndAuthorize(BaseTest.baseUri, BaseTest.USER)
            .openPagesTree()
            .createNewPageWithinPage(null)
            .fillAndSubmitCreationForm(
                new Page.Builder()
                    .using(BaseTest.basePage)
                    .setDateFrom(null)
                    .setTeamsList(List.of(
                         INVEST,
                         MORTGAGE))
                    .setEnable(false)
                    .build());
    // создание виджета A
    Widget widgetA = new Widget.Builder()
        .setUid(getNewUuid())
        .setName("MetaTitle")
        .setDevice(desktop)
        .setLocalization(RU.toString())
        .isEnabled(true)
        .setVersion(V_1_0_0.toString())
        .setExperimentOptionName(Experiment.DEFAULT.toString())
        .isDefaultWidget(true)
        .build();
    WrapperDraft.OperationDraft widgetCreationOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setName(widgetA.getName())
            .setDevice(widgetA.getDevice())
            .setLocalization(widgetA.getLocalization())
            .isEnabled(widgetA.isEnabled())
            .setVersion(widgetA.getVersion())
            .setExperimentOptionName(widgetA.getExperimentOptionName())
            .isDefaultWidget(widgetA.isDefaultWidget())
            .setCityGroups(Collections.singletonList(Geo.RU.toString()))
            .build(),
        WIDGET.toString(), CREATE.toString(), widgetA.getUid());
    WrapperDraft.OperationDraft widgetPlacementOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setChildUids(Collections.singletonList(widgetA.getUid()))
            .build(),
        PAGE.toString(),
        CHANGE_LINKS.toString(),
        page.getId());
    List<Object> operations = List.of(widgetCreationOperation, widgetPlacementOperation);
    WrapperDraft draft = new WrapperDraft(operations, desktop);
    LOGGER.info(String.format(
        "Сохранение черновика\n%s",
        new ObjectMapper().writeValueAsString(draft)));
    Response savingDraftResponse =
        given()
            .spec(pageDraftControllerSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", page.getId())
            .body(draft)
        .when().put()
        .then().extract().response();
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s",
        savingDraftResponse.getStatusLine(),
        savingDraftResponse.prettyPrint()));
    LOGGER.info("Публикация черновика");
    Response publishDraftResponse =
        given()
            .spec(pageDraftControllerPublishSpec)
            .auth().oauth2(getToken(USER).getAccessToken())
            .pathParam("pageId", page.getId())
            .queryParams("device", desktop)
        .when().post()
        .then().extract().response();
    LOGGER.info(String.format(
        "Получен ответ:\n%s\n%s",
        publishDraftResponse.statusLine(),
        publishDraftResponse.prettyPrint()));
    page = new Page.Builder()
        .using(page)
        .setWidgets(Collections.singletonList(widgetA))
        .build();
    createdPages.put(page.getUri(), page);
    getDriver().navigate().refresh();
    // CHECKS //
    checkCreatedPageAtPageController(page);
    checkCreatedPageAtContentPageController(page);
  }
}
