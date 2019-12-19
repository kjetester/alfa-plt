package ru.alfabank.platform.apitest.drafts.create;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroup;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.helpers.TestDataHelper.DRAFT_CONTROLLER_URL;
import static ru.alfabank.platform.helpers.TestDataHelper.getNewUuid;
import static ru.alfabank.platform.helpers.TestDataHelper.getRequestSpecification;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestPage;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestWidget;
import static ru.alfabank.platform.helpers.TestDataHelper.putNewChildWidgetToParentWidget;

import io.qameta.allure.Allure;
import java.util.Arrays;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.apitest.BaseTest;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Method;
import ru.alfabank.platform.businessobjects.draft.DataDraft;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;


public class CreateChildWidgetTest extends BaseTest {

  /**
   * Draft generation.
   */
  @BeforeClass(
      description = "Генерация черновика создания нового дочернего Widget'а")
  public void makeDraft() {
    newEntityUid = getNewUuid();
    DataDraft newWidgetData = new DataDraft.DataDraftBuilder()
        .name("ChildWidget_" + newEntityUid)
        .dateFrom("2019-01-01T00:00:00.000Z")
        .dateTo("2020-01-01T00:00:00")
        .device(desktop)
        .enable(true)
        .localization("RU")
        .cityGroups(getCityGroup("123"))
        .build();
    DataDraft placementData = new DataDraft.DataDraftBuilder()
        .childUids(putNewChildWidgetToParentWidget(newEntityUid))
        .build();
    operations.addAll(Arrays.asList(
        new WrapperDraft.OperationDraft(
            newWidgetData, Entity.widget, Method.create, newEntityUid),
        new WrapperDraft.OperationDraft(
            placementData, Entity.widget, Method.changeLinks, getTestWidget().getUid())));
    body = new WrapperDraft(operations);
  }

  @Test(
      description = "Проверка сохранения черновика создания нового дочернего Widget'а")
  public void saveTest() {
    given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
        .when().put(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка получения сохраненного черновика создания нового дочернего Widget'а",
      dependsOnMethods = "saveTest")
  public void getTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    // TODO JSONAssert.assertEquals(
    //  response.getBody().asString(), objMapper.writeValueAsString(body), true);
  }

  @Test(
      description = "Проверка публикации черновика создания нового дочернего Widget'а",
      dependsOnMethods = {"saveTest", "getTest"})
  public void publishTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().post(DRAFT_CONTROLLER_URL + "/execute")
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка отсутствия черновика создания нового дочернего Widget'а"
          + " после его публикации",
      dependsOnMethods = "publishTest")
  public void absentPublishedDraftTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(404)).statusCode(404);
  }

  /**
   * Attach log to Allure report.
   */
  @AfterClass(
      description = "Прикрепление лога к отчету"
  )
  public void writeToReport() {
    Allure.getLifecycle().updateTestCase((t) -> {
      t.setStatusDetails(t.getStatusDetails().setMessage(String.format(
          "Created the widget with UID %s --->>> placed to the widget with UID %s adn name %s",
          newEntityUid,
          getTestWidget().getUid(),
          getTestWidget().getName())
      ));
    });
  }
}
